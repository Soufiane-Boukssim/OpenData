package com.open_data_backend.services.dataProvider;

import com.open_data_backend.entities.DataProvider;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProviderService {
    List<DataProvider> getAllProviders();
    DataProvider getProviderById(UUID uuid);
    DataProvider getProviderByName(String name);
    DataProvider updateProviderById(UUID uuid, String name, String description, MultipartFile icon) throws IOException;
    DataProvider saveProvider(String name, String description, MultipartFile file) throws IOException;
    Boolean deleteProviderById(UUID uuid);
    Long getNumberOfProvider();
    byte[] getImage(String fileName) throws IOException;


}
