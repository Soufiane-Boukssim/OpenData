package com.open_data_backend.services.dataProviderOrganisationMember;

import com.open_data_backend.dtos.dataProviderOrganisationMember.DataProviderOrganisationMemberResponse;
import java.util.List;
import java.util.UUID;

public interface DataProviderOrganisationMemberService {
    List<DataProviderOrganisationMemberResponse> getAllDataProviderMembers();
    DataProviderOrganisationMemberResponse getDataProviderMemberById(UUID uuid);
    DataProviderOrganisationMemberResponse getDataProviderMemberByName(String firstname,String lastname);
    DataProviderOrganisationMemberResponse getDataProviderMemberByEmail(String email);
    DataProviderOrganisationMemberResponse saveDataProviderMember(String firstName, String lastName, String email);
    Boolean deleteDataProviderMemberById(UUID uuid);
    DataProviderOrganisationMemberResponse updateDataProviderMember(UUID uuid, String firstName, String lastName, String email);


//    DataProviderUser updateDataProviderUserById(UUID uuid, String name, String description, MultipartFile icon) throws IOException;
//    Boolean deleteDataProviderUserById(UUID uuid);
//    Long getNumberOfDataProviderUsers();
//    byte[] getDataProviderUserImage(String fileName) throws IOException;

}
