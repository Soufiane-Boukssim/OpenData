package com.open_data_backend.services.dataProviderMember;

import com.open_data_backend.entities.DataProviderOrganisationMember;
import java.util.List;
import java.util.UUID;

public interface DataProviderOrganisationMemberService {
    List<DataProviderOrganisationMember> getAllDataProviderMembers();
    DataProviderOrganisationMember getDataProviderMemberById(UUID uuid);
    DataProviderOrganisationMember getDataProviderMemberByName(String firstname,String lastname);
    DataProviderOrganisationMember getDataProviderMemberByEmail(String email);
    DataProviderOrganisationMember saveDataProviderMember(String firstName, String lastName, String email);
    Boolean deleteDataProviderMemberById(UUID uuid);
    DataProviderOrganisationMember updateDataProviderMember(UUID uuid, String firstName, String lastName, String email);


//    DataProviderUser updateDataProviderUserById(UUID uuid, String name, String description, MultipartFile icon) throws IOException;
//    Boolean deleteDataProviderUserById(UUID uuid);
//    Long getNumberOfDataProviderUsers();
//    byte[] getDataProviderUserImage(String fileName) throws IOException;

}
