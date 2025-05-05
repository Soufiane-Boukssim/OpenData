package com.open_data_backend.services.dataSet;

import com.open_data_backend.dtos.dataSet.DataSetResponse;
import com.open_data_backend.entities.DataProviderOrganisationMember;
import com.open_data_backend.entities.DataSet;
import com.open_data_backend.entities.DataSetTheme;
import com.open_data_backend.entities.DataProviderOrganisation;
import com.open_data_backend.mappers.DataSetMapper;
import com.open_data_backend.repositories.DataProviderOrganisationMemberRepository;
import com.open_data_backend.repositories.DataProviderOrganisationRepository;
import com.open_data_backend.repositories.DataSetRepository;
import com.open_data_backend.repositories.DataSetThemeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class DataSetServiceImplementation implements DataSetService {

    private final DataSetRepository dataSetRepository;
    private final DataProviderOrganisationRepository dataProviderOrganisationRepository;
    private final DataProviderOrganisationMemberRepository dataProviderOrganisationMemberRepository;
    private final DataSetThemeRepository dataSetThemeRepository;

    private final DataSetMapper dataSetMapper;

    private static final String UPLOAD_DIR = System.getProperty("user.dir").replace("\\", "/") + "/uploads/documents/datasets/";
    private static final String fileUrl = "http://localhost:8080/api/datasets/upload/file/";

    @Override
    public List<DataSetResponse> getAllDataSet() {
        List<DataSetResponse> dataSetResponseList = new ArrayList<>();
        List<DataSet> dataSetList = dataSetRepository.findByDeletedFalse();
        if (dataSetList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DataSet Is Empty");
        }
        for (DataSet dataSet : dataSetList) {
            dataSetResponseList.add(dataSetMapper.convertToResponse(dataSet));
        }
        return dataSetResponseList;
    }

    @Override
    public List<DataSetResponse> getAllDataSetByTheme(String themeName) {
        List<DataSetResponse> dataSetResponseList = new ArrayList<>();
        List<DataSet> dataSets = dataSetRepository.findByTheme_NameAndDeletedFalse(themeName);
        if (dataSets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DataSet Not Found with theme " + themeName);
        }
        for (DataSet dataSet : dataSets) {
            dataSetResponseList.add(dataSetMapper.convertToResponse(dataSet));
        }
        return dataSetResponseList;
    }

    @Override
    public List<DataSetResponse> getAllDataSetByProvider(String provider) {
        List<DataSetResponse> dataSetResponseList = new ArrayList<>();
        List<DataSet> dataSetList = dataSetRepository.findByDataProviderOrganisation_NameAndDeletedFalse(provider);
        if (dataSetList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DataSet Not Found with provider " + provider);
        }
        for (DataSet dataSet : dataSetList) {
            dataSetResponseList.add(dataSetMapper.convertToResponse(dataSet));
        }
        return dataSetResponseList;
    }

    @Override
    public DataSetResponse getDataSetById(UUID uuid) {
        DataSet dataSet= dataSetRepository.findByUuidAndDeletedFalse(uuid);
        if (dataSet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DataSet Not Found with uuid " + uuid);
        }
        return dataSetMapper.convertToResponse(dataSet);
    }

    @Override
    public DataSetResponse getDataSetByName(String name) {
        DataSet dataSet= dataSetRepository.findByNameAndDeletedFalse(name);
        if (dataSet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DataSet Not Found with name " + name);
        }
        return dataSetMapper.convertToResponse(dataSet);
    }

    @Override
    public DataSetResponse saveDataSet(String name, String description, UUID themeUuid, UUID dataProviderOrganisationMemberUuid, MultipartFile file) throws IOException {
        validateDataSetInput(name, description, themeUuid, dataProviderOrganisationMemberUuid, file);
        DataSetTheme theme = checkIfThemeExists(themeUuid);
        DataProviderOrganisationMember provider = checkIfDataProviderOrganisationMemberExists(dataProviderOrganisationMemberUuid);
        checkUniqueDataSetName(name);
        ensureUploadDirectoryExists();
        DataSet dataSet = createDataSet(name, description, theme, provider, file);
        dataSet= dataSetRepository.save(dataSet);
        return dataSetMapper.convertToResponse(dataSet);
    }

    @Override
    public DataSetResponse updateDataSetById(UUID uuid, String name, String description, UUID themeUuid, UUID dataProviderOrganisationMemberUuid, MultipartFile file) throws IOException {

        if (dataProviderOrganisationMemberUuid == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le champ 'dataProviderOrganisationMemberUuid' est vide.");
        }

        DataSet existingDataSet= dataSetRepository.findByUuidAndDeletedFalse(uuid);
        if (existingDataSet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DataSet Not Found with uuid " + uuid);
        }

        DataProviderOrganisationMember member = dataProviderOrganisationMemberRepository.findByUuidAndDeletedFalse(dataProviderOrganisationMemberUuid);
        if (member == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DataProviderOrganisationMember Not Found with uuid " + dataProviderOrganisationMemberUuid);
        }

        if (name != null) {
            existingDataSet.setName(name);
        }
        if (description != null) {
            existingDataSet.setDescription(description);
        }
        if (themeUuid != null) {
            existingDataSet.setTheme(dataSetThemeRepository.findByUuidAndDeletedFalse(themeUuid));
        }
        existingDataSet.setDataProviderOrganisationMember(dataProviderOrganisationMemberRepository.findByUuidAndDeletedFalse(dataProviderOrganisationMemberUuid));
        existingDataSet.setDataProviderOrganisation(dataProviderOrganisationMemberRepository.findByUuidAndDeletedFalse(dataProviderOrganisationMemberUuid).getDataProviderOrganisation());

        if (file != null && !file.isEmpty()) {
            String uniqueFileName = saveFileToDisk(file);
            existingDataSet.setFileData(file.getBytes());
            existingDataSet.setFilePath(UPLOAD_DIR + uniqueFileName);
            existingDataSet.setFile(fileUrl + uniqueFileName);
            existingDataSet.setFileType(file.getContentType());
            existingDataSet.setFileSize(file.getSize());
        }
        existingDataSet.setUpdatedBy(member.getEmail());
        existingDataSet= dataSetRepository.save(existingDataSet);
        return dataSetMapper.convertToResponse(existingDataSet);
    }

    @Override
    public Boolean deleteDataSetById(UUID uuid) {
        DataSet existingDataSet= dataSetRepository.findByUuidAndDeletedFalse(uuid);
        if (existingDataSet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DataSet Not Found with uuid " + uuid);
        }
        existingDataSet.setDeleted(true);
        dataSetRepository.save(existingDataSet);
        return true;
    }

    @Override
    public Long getNumberOfDataSet() {
        return dataSetRepository.countByDeletedFalse();
    }

    @Override
    public byte[] getFile(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR+'/'+fileName);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Le fichier " + fileName + " n'existe pas.");
        }
        return Files.readAllBytes(filePath);
    }

    private void validateDataSetInput(String name, String description, UUID themeUuid, UUID dataProviderOrganisationMemberUuid, MultipartFile file) {
        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) errors.add("Le champ 'name' est vide");
        if (description == null || description.trim().isEmpty()) errors.add("Le champ 'description' est vide");
        if (themeUuid == null) errors.add("Le champ 'theme' est vide");
        if (dataProviderOrganisationMemberUuid == null) errors.add("Le champ 'dataProviderOrganisationMemberUuid' est vide");
        if (file == null || file.isEmpty()) errors.add("Le champ 'file' est vide");
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur(s): " + String.join(", ", errors) + ".");
        }
    }

    private DataSetTheme checkIfThemeExists(UUID themeUuid) {
        DataSetTheme theme = dataSetThemeRepository.findByUuidAndDeletedFalse(themeUuid);
        if (theme == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Le thème spécifié n'existe pas ou a été supprimé.");
        }
        return theme;
    }

    private DataProviderOrganisationMember checkIfDataProviderOrganisationMemberExists(UUID dataProviderOrganisationMemberUuid) {
        DataProviderOrganisationMember provider = dataProviderOrganisationMemberRepository.findByUuidAndDeletedFalse(dataProviderOrganisationMemberUuid);
        if (provider == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Le provider spécifié n'existe pas ou a été supprimé.");
        }
        return provider;
    }

    private void ensureUploadDirectoryExists() throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    private void checkUniqueDataSetName(String name) {
        if (dataSetRepository.findByNameAndDeletedFalse(name) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un DataSet avec le nom '" + name + "' existe déjà.");
        }
    }

    private DataSet createDataSet(String name, String description, DataSetTheme theme, DataProviderOrganisationMember provider, MultipartFile file) throws IOException {
        DataSet dataSet = new DataSet();
        dataSet.setUuid(UUID.randomUUID());
        dataSet.setName(name);
        dataSet.setDescription(description);
        dataSet.setTheme(theme);
        dataSet.setDataProviderOrganisationMember(provider);
        dataSet.setDataProviderOrganisation(provider.getDataProviderOrganisation());
        dataSet.setCreatedBy(provider.getEmail());
        if (file != null && !file.isEmpty()) {
            String uniqueFileName = saveFileToDisk(file);
            dataSet.setFileData(file.getBytes());
            dataSet.setFilePath(UPLOAD_DIR + uniqueFileName);
            dataSet.setFile(fileUrl + uniqueFileName);

            dataSet.setFileType(file.getContentType());
            dataSet.setFileSize(file.getSize());
        }
        return dataSet;
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
