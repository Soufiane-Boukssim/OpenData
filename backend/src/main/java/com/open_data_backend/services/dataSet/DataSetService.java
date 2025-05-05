package com.open_data_backend.services.dataSet;

import com.open_data_backend.dtos.dataSet.DataSetResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DataSetService {
    List<DataSetResponse> getAllDataSet();
    List<DataSetResponse> getAllDataSetByTheme(String themeName);
    List<DataSetResponse> getAllDataSetByProvider(String provider);
    DataSetResponse getDataSetById(UUID uuid);

    DataSetResponse getDataSetByName(String name);

    DataSetResponse saveDataSet(String name, String description, UUID themeUuid, UUID dataProviderOrganisationMemberUuid, MultipartFile file) throws IOException;
    DataSetResponse updateDataSetById(UUID uuid, String name, String description, UUID themeUuid, UUID dataProviderOrganisationMemberUuid, MultipartFile file) throws IOException;


    Boolean deleteDataSetById(UUID uuid);
    Long getNumberOfDataSet();
    byte[] getFile(String fileName) throws IOException;


}
