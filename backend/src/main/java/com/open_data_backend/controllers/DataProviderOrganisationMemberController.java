package com.open_data_backend.controllers;

import com.open_data_backend.dtos.dataProviderOrganisationMember.DataProviderOrganisationMemberResponse;
import com.open_data_backend.services.dataProviderOrganisationMember.DataProviderOrganisationMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController @RequiredArgsConstructor @RequestMapping("/api/data-provider/organisation-members")
public class DataProviderOrganisationMemberController {
    
    private final DataProviderOrganisationMemberService dataProviderOrganisationMemberService;

    @GetMapping("/get/all")
    public List<DataProviderOrganisationMemberResponse> getAllDataProviderOrganisationMembers() {
        return dataProviderOrganisationMemberService.getAllDataProviderMembers();
    }

    @GetMapping("/get/byId/{uuid}")
    public DataProviderOrganisationMemberResponse getDataProviderOrganisationMemberById(@PathVariable UUID uuid) {
        return dataProviderOrganisationMemberService.getDataProviderMemberById(uuid);
    }

    @GetMapping("/get/byName")
    public DataProviderOrganisationMemberResponse getDataProviderOrganisationMemberByName(@RequestParam String firstname, @RequestParam String lastname) {
        return dataProviderOrganisationMemberService.getDataProviderMemberByName(firstname, lastname);
    }

    @GetMapping("/get/byEmail")
    public DataProviderOrganisationMemberResponse getDataProviderOrganisationMemberByEmail(@RequestParam String email) {
        return dataProviderOrganisationMemberService.getDataProviderMemberByEmail(email);
    }


    @PostMapping("/save")
    public ResponseEntity<DataProviderOrganisationMemberResponse> saveDataProviderOrganisationMember(
            @RequestParam(value = "firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("email") String email) {
        DataProviderOrganisationMemberResponse dataProviderOrganisationMemberResponse = dataProviderOrganisationMemberService.saveDataProviderMember(firstname, lastname,email);
        return ResponseEntity.ok(dataProviderOrganisationMemberResponse);
    }

    @DeleteMapping("/delete/byId/{uuid}")
    public Boolean deleteDataProviderOrganisationMemberById(@PathVariable UUID uuid) {
        return dataProviderOrganisationMemberService.deleteDataProviderMemberById(uuid);
    }

    @PutMapping("/update/byId/{uuid}")
    public ResponseEntity<DataProviderOrganisationMemberResponse> updateDataProviderOrganisationMember(
            @PathVariable UUID uuid,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("email") String email) {
        DataProviderOrganisationMemberResponse dataProviderOrganisationMemberResponse = dataProviderOrganisationMemberService.updateDataProviderMember(uuid, firstname, lastname, email);
        return ResponseEntity.ok(dataProviderOrganisationMemberResponse);
    }


}
