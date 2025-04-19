package com.open_data_backend.controllers;

import com.open_data_backend.entities.DataProviderOrganisationMember;
import com.open_data_backend.services.dataProviderMember.DataProviderOrganisationMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController @RequiredArgsConstructor @RequestMapping("/api/data-provider/organisation-members")
public class DataProviderOrganisationMemberController {
    
    private final DataProviderOrganisationMemberService dataProviderOrganisationMemberService;

    @GetMapping("/get/all")
    public List<DataProviderOrganisationMember> getAllDataProviderOrganisationMembers() {
        return dataProviderOrganisationMemberService.getAllDataProviderMembers();
    }

    @GetMapping("/get/byId/{uuid}")
    public DataProviderOrganisationMember getDataProviderOrganisationMemberById(@PathVariable UUID uuid) {
        return dataProviderOrganisationMemberService.getDataProviderMemberById(uuid);
    }

    @GetMapping("/get/byName")
    public DataProviderOrganisationMember getDataProviderOrganisationMemberByName(@RequestParam String firstname, @RequestParam String lastname) {
        return dataProviderOrganisationMemberService.getDataProviderMemberByName(firstname, lastname);
    }

    @GetMapping("/get/byEmail")
    public DataProviderOrganisationMember getDataProviderOrganisationMemberByEmail(@RequestParam String email) {
        return dataProviderOrganisationMemberService.getDataProviderMemberByEmail(email);
    }


    @PostMapping("/save")
    public ResponseEntity<DataProviderOrganisationMember> saveDataProviderOrganisationMember(
            @RequestParam(value = "firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("email") String email) {
        DataProviderOrganisationMember dataProviderOrganisationMember = dataProviderOrganisationMemberService.saveDataProviderMember(firstname, lastname,email);
        return ResponseEntity.ok(dataProviderOrganisationMember);
    }

    @DeleteMapping("/delete/byId/{uuid}")
    public Boolean deleteDataProviderOrganisationMemberById(@PathVariable UUID uuid) {
        return dataProviderOrganisationMemberService.deleteDataProviderMemberById(uuid);
    }

    @PutMapping("/update/byId/{uuid}")
    public ResponseEntity<DataProviderOrganisationMember> updateDataProviderOrganisationMember(
            @PathVariable UUID uuid,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("email") String email) {
        DataProviderOrganisationMember dataProviderOrganisationMember = dataProviderOrganisationMemberService.updateDataProviderMember(uuid, firstname, lastname, email);
        return ResponseEntity.ok(dataProviderOrganisationMember);
    }


}
