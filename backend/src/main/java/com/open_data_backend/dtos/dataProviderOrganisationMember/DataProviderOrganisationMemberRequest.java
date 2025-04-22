package com.open_data_backend.dtos.dataProviderOrganisationMember;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DataProviderOrganisationMemberRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
