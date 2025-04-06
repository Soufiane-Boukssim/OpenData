package com.open_data_backend.services.dataSetTheme;

import com.open_data_backend.entities.DataSetTheme;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DataSetThemeService {
    List<DataSetTheme> getAllThemes();
    DataSetTheme getThemeById(UUID uuid);
    DataSetTheme getThemeByName(String name);
    DataSetTheme saveTheme(String name, String description, MultipartFile file) throws IOException;
    DataSetTheme updateThemeById(UUID uuid, String name, String description, MultipartFile icon) throws IOException;
    Boolean deleteThemeById(UUID uuid);
    Long getNumberOfTheme();
    byte[] getImage(String fileName) throws IOException;
}
