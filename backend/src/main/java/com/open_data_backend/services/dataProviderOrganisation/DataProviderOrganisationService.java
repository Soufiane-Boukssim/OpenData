package com.open_data_backend.services.dataProviderOrganisation;

import com.open_data_backend.dtos.dataProviderOrganisation.DataProviderOrganisationResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DataProviderOrganisationService {
    List<DataProviderOrganisationResponse> getAllDataProviderOrganisations();
    DataProviderOrganisationResponse getDataProviderOrganisationById(UUID uuid);
    DataProviderOrganisationResponse getDataProviderOrganisationByName(String name);
    DataProviderOrganisationResponse updateDataProviderOrganisationById(UUID uuid, String name, String description, MultipartFile icon) throws IOException;
    DataProviderOrganisationResponse saveDataProviderOrganisation(String name, String description, MultipartFile icon) throws IOException;
    Boolean deleteDataProviderOrganisationById(UUID uuid);
    Long getNumberOfDataProviderOrganisations();
    byte[] getDataProviderOrganisationImage(String fileName) throws IOException;
    void assignUserToOrganisation(UUID organisationId, UUID userId);

}
