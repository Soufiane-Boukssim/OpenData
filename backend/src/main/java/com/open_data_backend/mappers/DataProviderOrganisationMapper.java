package com.open_data_backend.mappers;

import com.open_data_backend.dtos.dataProviderOrganisation.DataProviderOrganisationRequest;
import com.open_data_backend.dtos.dataProviderOrganisation.DataProviderOrganisationResponse;
import com.open_data_backend.dtos.dataProviderOrganisation.SimplifiedDataProviderOrganisationResponse;
import com.open_data_backend.entities.DataProviderOrganisation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class DataProviderOrganisationMapper {

    private final ModelMapper modelMapper;

    public DataProviderOrganisation convertToEntity(DataProviderOrganisationRequest dataProviderOrganisationRequest) {
        return modelMapper.map(dataProviderOrganisationRequest, DataProviderOrganisation.class);
    }

    public DataProviderOrganisationResponse convertToResponse(DataProviderOrganisation dataProviderOrganisation) {
        return modelMapper.map(dataProviderOrganisation, DataProviderOrganisationResponse.class);
    }

    public SimplifiedDataProviderOrganisationResponse convertToSimplifiedResponse(DataProviderOrganisation dataProviderOrganisation) {
        return modelMapper.map(dataProviderOrganisation, SimplifiedDataProviderOrganisationResponse.class);
    }
}
