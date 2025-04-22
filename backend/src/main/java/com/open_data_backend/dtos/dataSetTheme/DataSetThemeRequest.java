package com.open_data_backend.dtos.dataSetTheme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DataSetThemeRequest {
    private String name;
    private String description;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private byte[] iconData;
    private String iconPath;
    private String icon;
}
