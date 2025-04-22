package com.open_data_backend.dtos.dataProviderOrganisationMember;

import com.open_data_backend.dtos.dataProviderOrganisation.SimplifiedDataProviderOrganisationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DataProviderOrganisationMemberResponse {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private SimplifiedDataProviderOrganisationResponse dataProviderOrganisation;
}
