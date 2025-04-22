package com.open_data_backend.services.dataSetTheme;

import com.open_data_backend.dtos.dataSetTheme.DataSetThemeResponse;
import com.open_data_backend.entities.DataSetTheme;
import com.open_data_backend.mappers.DataSetThemeMapper;
import com.open_data_backend.repositories.DataSetThemeRepository;
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
public class DataSetThemeServiceImplementation implements DataSetThemeService {

    private final DataSetThemeRepository dataSetThemeRepository;
    private static final String UPLOAD_DIR = System.getProperty("user.dir").replace("\\", "/") + "/uploads/images/dataset-theme";
    private static final String imageUrl = "http://localhost:8080/api/themes/upload/image";
    private final DataSetThemeMapper dataSetThemeMapper;

    @Override
    public List<DataSetThemeResponse> getAllThemes() {
        List<DataSetThemeResponse> themesResponse = new ArrayList<>();
        List<DataSetTheme> themes = dataSetThemeRepository.findByDeletedFalse();
        if (themes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There are no themes in the database");
        }
        else {
            for (DataSetTheme theme : themes) {
                themesResponse.add(dataSetThemeMapper.convertToResponse(theme));
            }
        }
        return themesResponse;
    }

    @Override
    public DataSetThemeResponse getThemeById(UUID uuid) {
        DataSetTheme dataSetTheme= dataSetThemeRepository.findByUuidAndDeletedFalse(uuid);
        if (dataSetTheme == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No theme found with id: "+uuid);
        }
        return dataSetThemeMapper.convertToResponse(dataSetTheme);
    }

    @Override
    public DataSetThemeResponse getThemeByName(String name) {
        DataSetTheme dataSetTheme= dataSetThemeRepository.findByNameAndDeletedFalse(name);
        if (dataSetTheme == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No theme found with name: "+name);
        }
        return dataSetThemeMapper.convertToResponse(dataSetTheme);
    }

    @Override
    public DataSetThemeResponse saveTheme(String name, String description, MultipartFile file) throws IOException {
        validateThemeInputs(name, description, file);
        checkIfThemeNameExists(name);
        ensureUploadDirectoryExists();
        DataSetTheme theme = createThemeObject(name, description, file);
        theme= dataSetThemeRepository.save(theme);
        return dataSetThemeMapper.convertToResponse(theme);
    }

    @Override
    public DataSetThemeResponse updateThemeById(UUID uuid, String name, String description, MultipartFile icon) throws IOException {
        DataSetTheme dataSetTheme= dataSetThemeRepository.findByUuidAndDeletedFalse(uuid);
        if (dataSetTheme == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No theme found with id: "+uuid);
        }
        if (name != null) {
            dataSetTheme.setName(name);
        }
        if (description != null) {
            dataSetTheme.setDescription(description);
        }
        if (icon != null && !icon.isEmpty()) {
            String uniqueFileName = saveFileToDisk(icon);
            dataSetTheme.setIconData(icon.getBytes());
            dataSetTheme.setIconPath(UPLOAD_DIR+'/'+uniqueFileName);
            dataSetTheme.setIcon(imageUrl+'/'+uniqueFileName);
        }
        dataSetTheme= dataSetThemeRepository.save(dataSetTheme);
        return dataSetThemeMapper.convertToResponse(dataSetTheme);
    }

    @Override
    public Boolean deleteThemeById(UUID uuid) {
        DataSetTheme dataSetTheme= dataSetThemeRepository.findByUuidAndDeletedFalse(uuid);
        if (dataSetTheme == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No theme found with id: "+uuid);
        }
        else  {
            dataSetTheme.setDeleted(true);
            dataSetThemeRepository.save(dataSetTheme);
            return true;
        }
    }

    @Override
    public Long getNumberOfTheme() {
        return dataSetThemeRepository.countByDeletedFalse();
    }

    @Override
    public byte[] getImage(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR+'/'+ fileName);
        return Files.readAllBytes(filePath);
    }

    private void validateThemeInputs(String name, String description, MultipartFile file) {
        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) errors.add("Le champ 'name' est vide");
        if (description == null || description.trim().isEmpty()) errors.add("Le champ 'description' est vide");
        if (file == null || file.isEmpty()) errors.add("Le champ 'icon' est vide");
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Erreur(s): " + String.join(", ", errors) + ".");
        }
    }

    private void checkIfThemeNameExists(String name) {
        if (dataSetThemeRepository.findByNameAndDeletedFalse(name) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un thème avec ce nom existe déjà.");
        }
    }

    private void ensureUploadDirectoryExists() throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    private DataSetTheme createThemeObject(String name, String description, MultipartFile file) throws IOException {
        DataSetTheme theme = new DataSetTheme();
        theme.setUuid(UUID.randomUUID());
        theme.setName(name.trim());
        theme.setDescription(description.trim());
        String uniqueFileName = saveFileToDisk(file);
        theme.setIconData(file.getBytes());
        theme.setIcon(imageUrl+'/'+uniqueFileName);

        theme.setIconPath(UPLOAD_DIR + uniqueFileName);
        return theme;
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


