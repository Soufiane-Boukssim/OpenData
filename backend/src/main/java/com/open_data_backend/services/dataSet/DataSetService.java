package com.open_data_backend.services.dataSet;

import com.open_data_backend.entities.DataSet;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DataSetService {
    List<DataSet> getAllDataSet();
    List<DataSet> getAllDataSetByTheme(String themeName);
    List<DataSet> getAllDataSetByProvider(String provider);
    DataSet getDataSetById(UUID uuid);
    DataSet getDataSetByName(String name);
    DataSet saveDataSet(String name, String description, UUID themeUuid, UUID providerUuid, MultipartFile file) throws IOException;
    DataSet updateDataSetById(UUID uuid, String name, String description, UUID themeUuid, UUID providerUuid, MultipartFile file) throws IOException;


    Boolean deleteDataSetById(UUID uuid);
    Long getNumberOfDataSet();
    byte[] getFile(String fileName) throws IOException;


}
