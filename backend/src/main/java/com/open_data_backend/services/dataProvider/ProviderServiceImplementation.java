package com.open_data_backend.services.dataProvider;

import com.open_data_backend.entities.DataProvider;
import com.open_data_backend.repositories.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class ProviderServiceImplementation implements ProviderService {
    private final ProviderRepository providerRepository;
    private static final String UPLOAD_DIR = System.getProperty("user.dir").replace("\\", "/") + "/uploads/images/providers";
    private static final String imageUrl = "http://localhost:8080/api/providers/upload/image";

    @Override
    public List<DataProvider> getAllProviders() {
        List<DataProvider> providers = providerRepository.findByDeletedFalse();
        if (providers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The provider list is empty");
        }
        return providers;
    }
    @Override
    public DataProvider getProviderById(UUID uuid) {
        DataProvider provider= providerRepository.findByUuidAndDeletedFalse(uuid);
        if (provider == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No provider found with id: "+uuid);
        }
        return provider;
    }
    @Override
    public DataProvider getProviderByName(String name) {
        DataProvider provider=providerRepository.findByNameAndDeletedFalse(name);
        if (provider == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No provider found with name: "+name);
        }
        return provider;
    }
    @Override
    public Boolean deleteProviderById(UUID uuid) {
        DataProvider provider= getProviderById(uuid);
        if (provider != null) {
            provider.setDeleted(true);
            providerRepository.save(provider);
            return true;
        }
        return false;
    }
    @Override
    public Long getNumberOfProvider() {
        return providerRepository.countByDeletedFalse();
    }

    @Override
    public byte[] getImage(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR+'/'+ fileName);
        return Files.readAllBytes(filePath);
    }

    @Override
    public DataProvider updateProviderById(UUID uuid, String name, String description, MultipartFile icon) throws IOException {
        DataProvider existingProvider = getProviderById(uuid);
        if (name != null) {
            existingProvider.setName(name);
        }
        if (description != null) {
            existingProvider.setDescription(description);
        }
        if (icon != null && !icon.isEmpty()) {
            String uniqueFileName = saveFileToDisk(icon);
            existingProvider.setIconData(icon.getBytes());
            existingProvider.setIconPath(UPLOAD_DIR+'/'+uniqueFileName);
            existingProvider.setIcon(imageUrl+'/'+uniqueFileName);
        }
        return providerRepository.save(existingProvider);
    }
    @Override
    public DataProvider saveProvider(String name, String description, MultipartFile file) throws IOException {
        validateProviderInputs(name, description, file);
        checkIfProviderNameExists(name);
        ensureUploadDirectoryExists();
        DataProvider provider = createProviderObject(name, description, file);
        return providerRepository.save(provider);
    }


    private void validateProviderInputs(String name, String description, MultipartFile file) {
        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            errors.add("Le champ 'name' est vide");
        }
        if (description == null || description.trim().isEmpty()) {
            errors.add("Le champ 'description' est vide");
        }
        if (file == null || file.isEmpty()) {
            errors.add("Le champ 'icon' est vide");
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Erreur(s): " + String.join(", ", errors) + ".");
        }
    }
    private void checkIfProviderNameExists(String name) {
        if (providerRepository.findByNameAndDeletedFalse(name) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un provider avec ce nom existe déjà.");
        }
    }
    private void ensureUploadDirectoryExists() throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }
    private DataProvider createProviderObject(String name, String description, MultipartFile file) throws IOException {
        DataProvider provider = new DataProvider();
        provider.setUuid(UUID.randomUUID());
        provider.setName(name.trim());
        provider.setDescription(description.trim());
        String uniqueFileName = saveFileToDisk(file);
        provider.setIconData(file.getBytes());
        provider.setIcon(imageUrl+'/'+uniqueFileName);
        provider.setIconPath(UPLOAD_DIR+'/'+uniqueFileName);
        return provider;
    }
    private String saveFileToDisk(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IOException("Nom de fichier invalide.");
        }

        String sanitizedFileName = StringUtils.cleanPath(originalFileName);
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(sanitizedFileName);
        int counter = 1;
        while (Files.exists(filePath)) {
            String fileNameWithoutExt = FilenameUtils.getBaseName(sanitizedFileName);
            String extension = FilenameUtils.getExtension(sanitizedFileName);
            String newFileName = fileNameWithoutExt + "_" + counter + (extension.isEmpty() ? "" : "." + extension);
            filePath = uploadPath.resolve(newFileName);
            counter++;
        }

        Files.write(filePath, file.getBytes());

        return filePath.getFileName().toString();
    }

}
