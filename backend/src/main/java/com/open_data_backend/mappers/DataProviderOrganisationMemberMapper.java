package com.open_data_backend.mappers;

import com.open_data_backend.dtos.dataProviderOrganisationMember.DataProviderOrganisationMemberRequest;
import com.open_data_backend.dtos.dataProviderOrganisationMember.DataProviderOrganisationMemberResponse;
import com.open_data_backend.entities.DataProviderOrganisationMember;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class DataProviderOrganisationMemberMapper {

    private final ModelMapper modelMapper;
    private final DataProviderOrganisationMapper dataProviderOrganisationMapper;


    public DataProviderOrganisationMember convertToEntity(DataProviderOrganisationMemberRequest dataProviderOrganisationMemberRequest) {
        return modelMapper.map(dataProviderOrganisationMemberRequest, DataProviderOrganisationMember.class);
    }

    public DataProviderOrganisationMemberResponse convertToResponse(DataProviderOrganisationMember dataProviderOrganisationMember) {
        DataProviderOrganisationMemberResponse response = modelMapper.map(dataProviderOrganisationMember, DataProviderOrganisationMemberResponse.class);

        if (dataProviderOrganisationMember.getDataProviderOrganisation() != null) {
            response.setDataProviderOrganisation(
                    dataProviderOrganisationMapper.convertToSimplifiedResponse(dataProviderOrganisationMember.getDataProviderOrganisation())
            );
        }

        return response;
    }


}
