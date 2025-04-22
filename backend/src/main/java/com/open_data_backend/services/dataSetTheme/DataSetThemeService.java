package com.open_data_backend.services.dataSetTheme;

import com.open_data_backend.dtos.dataSetTheme.DataSetThemeResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DataSetThemeService {
    List<DataSetThemeResponse> getAllThemes();
    DataSetThemeResponse getThemeById(UUID uuid);
    DataSetThemeResponse getThemeByName(String name);
    DataSetThemeResponse saveTheme(String name, String description, MultipartFile file) throws IOException;
    DataSetThemeResponse updateThemeById(UUID uuid, String name, String description, MultipartFile icon) throws IOException;
    Boolean deleteThemeById(UUID uuid);
    Long getNumberOfTheme();
    byte[] getImage(String fileName) throws IOException;
}
