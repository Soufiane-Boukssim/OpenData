package com.open_data_backend.services.dataProvider;

import com.open_data_backend.entities.DataProviderOrganisation;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DataProviderService {
    List<DataProviderOrganisation> getAllProviders();
    DataProviderOrganisation getProviderById(UUID uuid);
    DataProviderOrganisation getProviderByName(String name);
    DataProviderOrganisation updateProviderById(UUID uuid, String name, String description, MultipartFile icon) throws IOException;
    DataProviderOrganisation saveProvider(String name, String description, MultipartFile file) throws IOException;
    Boolean deleteProviderById(UUID uuid);
    Long getNumberOfProvider();
    byte[] getImage(String fileName) throws IOException;


}
