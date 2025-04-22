package com.open_data_backend.dtos.dataSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DataSetRequest {
    private UUID uuid;
    private String name;
    private String description;
    private String createdBy;
    private String updatedBy;
    private byte[] fileData;
    private String filePath;
    private String file;
    private String fileType;
    private Long fileSize;

    private Long themeId;
    private Long dataProviderOrganisationId;
    private Long dataProviderOrganisationMemberId;


}
