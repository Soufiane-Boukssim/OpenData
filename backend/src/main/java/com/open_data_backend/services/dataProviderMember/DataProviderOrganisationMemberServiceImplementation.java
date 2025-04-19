package com.open_data_backend.services.dataProviderMember;

import com.open_data_backend.entities.DataProviderOrganisationMember;
import com.open_data_backend.repositories.DataProviderOrganisationMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class DataProviderOrganisationMemberServiceImplementation implements DataProviderOrganisationMemberService {

    private final DataProviderOrganisationMemberRepository dataProviderOrganisationMemberRepository;

    @Override
    public List<DataProviderOrganisationMember> getAllDataProviderMembers() {
        List<DataProviderOrganisationMember> dataProviderUsers = dataProviderOrganisationMemberRepository.findByDeletedFalse();
        if (dataProviderUsers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The data provider members table is empty");
        }
        return dataProviderUsers;
    }

    @Override
    public DataProviderOrganisationMember getDataProviderMemberById(UUID uuid) {
        DataProviderOrganisationMember dataProviderOrganisationMember = findDataProviderMemberById(uuid);
        if ( dataProviderOrganisationMember == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No data provider member found with id: "+uuid);
        }
        return dataProviderOrganisationMember;

    }


    @Override
    public DataProviderOrganisationMember getDataProviderMemberByName(String firstname,String lastname) {
        DataProviderOrganisationMember dataProviderUser= dataProviderOrganisationMemberRepository.findByFirstNameAndLastNameAndDeletedFalse(firstname,lastname);
        if (dataProviderUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No data provider member found with name: "+firstname+" "+lastname);
        }
        return dataProviderUser;

    }

    @Override
    public DataProviderOrganisationMember getDataProviderMemberByEmail(String email) {
        DataProviderOrganisationMember dataProviderUser= dataProviderOrganisationMemberRepository.findByEmailAndDeletedFalse(email);
        if (dataProviderUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No data provider member found with email: "+email);
        }
        return dataProviderUser;
    }

    @Override
    public DataProviderOrganisationMember saveDataProviderMember(String firstName, String lastName, String email) {
        validateProviderMemberInputs(firstName, lastName, email);
        validateEmailSyntax(email);
        checkIfProviderExists(firstName, lastName, email);
        DataProviderOrganisationMember dataProviderOrganisationMember = createDataProviderOrganisationMemberObject(firstName, lastName, email);
        return dataProviderOrganisationMemberRepository.save(dataProviderOrganisationMember);

    }

    @Override
    public Boolean deleteDataProviderMemberById(UUID uuid) {
        DataProviderOrganisationMember dataProviderOrganisationMember= dataProviderOrganisationMemberRepository.findByUuidAndDeletedFalse(uuid);
        if (dataProviderOrganisationMember != null) {
            dataProviderOrganisationMember.setDeleted(true);
            dataProviderOrganisationMemberRepository.save(dataProviderOrganisationMember);
            return true;
        }
        return false;
    }

    @Override
    public DataProviderOrganisationMember updateDataProviderMember(UUID uuid, String firstName, String lastName, String email) {
        validateProviderMemberInputs(firstName, lastName, email);
        validateEmailSyntax(email);
        checkIfProviderExists(firstName, lastName, email);
        DataProviderOrganisationMember dataProviderOrganisationMember= findDataProviderMemberById(uuid);
        if (dataProviderOrganisationMember!=null){
            dataProviderOrganisationMember.setFirstName(firstName);
            dataProviderOrganisationMember.setLastName(lastName);
            dataProviderOrganisationMember.setEmail(email);
            return dataProviderOrganisationMemberRepository.save(dataProviderOrganisationMember);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No data provider member found with id: "+uuid);
        }

    }

    private DataProviderOrganisationMember findDataProviderMemberById(UUID uuid){
        return dataProviderOrganisationMemberRepository.findByUuidAndDeletedFalse(uuid);
    }


    private void validateProviderMemberInputs(String firstName, String lastName, String email) {
        List<String> errors = new ArrayList<>();
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add("Le champ 'firstName' est vide");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.add("Le champ 'lastName' est vide");
        }
        if (email == null || email.isEmpty()) {
            errors.add("Le champ 'email' est vide");
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Erreur(s): " + String.join(", ", errors) + ".");
        }
    }

    private void validateEmailSyntax(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Le format de l'email est invalide.");
        }
    }



    private void checkIfProviderExists(String firstName, String lastName, String email) {
        boolean nameExists = dataProviderOrganisationMemberRepository
                .findByFirstNameAndLastNameAndDeletedFalse(firstName, lastName) != null;

        boolean emailExists = dataProviderOrganisationMemberRepository
                .findByEmailAndDeletedFalse(email) != null;

        if (nameExists && emailExists) {
            throw new IllegalArgumentException("Erreur(s): Un provider avec ce nom existe et cet email existe déjà.");
        } else if (nameExists) {
            throw new IllegalArgumentException("Erreur(s): Un provider avec ce nom existe.");
        } else if (emailExists) {
            throw new IllegalArgumentException("Erreur(s): Un provider avec cet email existe déjà.");
        }
    }

//    private void findMember(String firstName, String lastName, String email) {
//        boolean nameExists = dataProviderOrganisationMemberRepository
//                .findByFirstNameAndLastNameAndDeletedFalse(firstName, lastName) != null;
//
//        boolean emailExists = dataProviderOrganisationMemberRepository
//                .findByEmailAndDeletedFalse(email) != null;
//
//        if (nameExists && emailExists) {
//            throw new IllegalArgumentException("Erreur(s): Un provider avec ce nom existe et cet email existe déjà.");
//        } else if (nameExists) {
//            throw new IllegalArgumentException("Erreur(s): Un provider avec ce nom existe.");
//        } else if (emailExists) {
//            throw new IllegalArgumentException("Erreur(s): Un provider avec cet email existe déjà.");
//        }
//    }


    private DataProviderOrganisationMember createDataProviderOrganisationMemberObject(String firstName, String lastName, String email) {
        DataProviderOrganisationMember dataProviderOrganisation = new DataProviderOrganisationMember();
        dataProviderOrganisation.setUuid(UUID.randomUUID());
        dataProviderOrganisation.setFirstName(firstName.trim());
        dataProviderOrganisation.setLastName(lastName.trim());
        dataProviderOrganisation.setEmail(email.trim());
        return dataProviderOrganisation;
    }


}
