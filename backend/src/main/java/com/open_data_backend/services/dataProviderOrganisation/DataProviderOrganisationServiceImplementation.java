package com.open_data_backend.services.dataProviderOrganisation;

import com.open_data_backend.entities.DataProviderOrganisation;
import com.open_data_backend.entities.DataProviderOrganisationMember;
import com.open_data_backend.repositories.DataProviderOrganisationMemberRepository;
import com.open_data_backend.repositories.DataProviderOrganisationRepository;
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
public class DataProviderOrganisationServiceImplementation implements DataProviderOrganisationService {

    private final DataProviderOrganisationRepository dataProviderOrganisationRepository;
    private final DataProviderOrganisationMemberRepository dataProviderOrganisationMemberRepository;

    private static final String UPLOAD_DIR = System.getProperty("user.dir").replace("\\", "/") + "/uploads/images/data-provider/organisations";
    private static final String imageUrl = "http://localhost:8080/api/data-provider/organisations/upload/image";

    @Override
    public List<DataProviderOrganisation> getAllDataProviderOrganisations() {
        List<DataProviderOrganisation> providers = dataProviderOrganisationRepository.findByDeletedFalse();
        if (providers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The data provider organisation table is empty");
        }
        return providers;
    }
    @Override
    public DataProviderOrganisation getDataProviderOrganisationById(UUID uuid) {
        DataProviderOrganisation provider= dataProviderOrganisationRepository.findByUuidAndDeletedFalse(uuid);
        if (provider == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No data provider organisation found with id: "+uuid);
        }
        return provider;
    }
    @Override
    public DataProviderOrganisation getDataProviderOrganisationByName(String name) {
        DataProviderOrganisation provider=dataProviderOrganisationRepository.findByNameAndDeletedFalse(name);
        if (provider == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No data provider organisation found with name: "+name);
        }
        return provider;
    }
    @Override
    public Boolean deleteDataProviderOrganisationById(UUID uuid) {
        DataProviderOrganisation provider= getDataProviderOrganisationById(uuid);
        if (provider != null) {
            provider.setDeleted(true);
            dataProviderOrganisationRepository.save(provider);
            return true;
        }
        return false;
    }
    @Override
    public Long getNumberOfDataProviderOrganisations() {
        return dataProviderOrganisationRepository.countByDeletedFalse();
    }

    @Override
//    public byte[] getDataProviderOrganisationImage(String fileName) throws IOException {
//        Path filePath = Paths.get(UPLOAD_DIR+'/'+ fileName);
//        return Files.readAllBytes(filePath);
//    }

    public byte[] getDataProviderOrganisationImage(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR + '/' + fileName);

        if (!Files.exists(filePath)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Image "+fileName+" not found in the storage"
            );
        }

        return Files.readAllBytes(filePath);
    }

    @Override
    public DataProviderOrganisation updateDataProviderOrganisationById(UUID uuid, String name, String description, MultipartFile icon) throws IOException {
        DataProviderOrganisation existingProvider = getDataProviderOrganisationById(uuid);
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
        return dataProviderOrganisationRepository.save(existingProvider);
    }

    @Override
    public DataProviderOrganisation saveDataProviderOrganisation(String name, String description, MultipartFile file) throws IOException {
        validateProviderInputs(name, description, file);
        checkIfProviderNameExists(name);
        ensureUploadDirectoryExists();
        DataProviderOrganisation provider = createProviderObject(name, description, file);
        return dataProviderOrganisationRepository.save(provider);
    }


    @Override
    public void assignUserToOrganisation(UUID organisationId, UUID userId) {
        List<String> errors = new ArrayList<>();

        DataProviderOrganisation dataProviderOrganisation = dataProviderOrganisationRepository.findByUuidAndDeletedFalse(organisationId);
        if (dataProviderOrganisation == null) {
            errors.add("Organisation not found with id: "+organisationId);
        }

        DataProviderOrganisationMember dataProviderOrganisationMember = dataProviderOrganisationMemberRepository.findByUuidAndDeletedFalse(userId);
        if (dataProviderOrganisationMember == null) {
            errors.add("Member not found with the id: "+userId);
        }

        if (dataProviderOrganisationMemberRepository.findByUuidAndDeletedFalse(userId).getDataProviderOrganisation()!=null ){
            errors.add("Member with id: "+userId+" already assign to an organisation");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Erreur(s): " + String.join(", ", errors) );
        }

       dataProviderOrganisationMember.setDataProviderOrganisation(dataProviderOrganisation);

        dataProviderOrganisationMemberRepository.save(dataProviderOrganisationMember);
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
        if (dataProviderOrganisationRepository.findByNameAndDeletedFalse(name) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un provider avec ce nom existe déjà.");
        }
    }
    private void ensureUploadDirectoryExists() throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }
    private DataProviderOrganisation createProviderObject(String name, String description, MultipartFile file) throws IOException {
        DataProviderOrganisation provider = new DataProviderOrganisation();
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
