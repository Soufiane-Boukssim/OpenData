package com.open_data_backend.services.dataProviderOrganisation;

import com.open_data_backend.entities.DataProviderOrganisation;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DataProviderOrganisationService {
    List<DataProviderOrganisation> getAllDataProviderOrganisations();
    DataProviderOrganisation getDataProviderOrganisationById(UUID uuid);
    DataProviderOrganisation getDataProviderOrganisationByName(String name);
    DataProviderOrganisation updateDataProviderOrganisationById(UUID uuid, String name, String description, MultipartFile icon) throws IOException;
    DataProviderOrganisation saveDataProviderOrganisation(String name, String description, MultipartFile file) throws IOException;
    Boolean deleteDataProviderOrganisationById(UUID uuid);
    Long getNumberOfDataProviderOrganisations();
    byte[] getDataProviderOrganisationImage(String fileName) throws IOException;
    void assignUserToOrganisation(UUID organisationId, UUID userId);

}
