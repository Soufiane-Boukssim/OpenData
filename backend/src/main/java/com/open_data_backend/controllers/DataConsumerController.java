package com.open_data_backend.controllers;

import com.open_data_backend.dtos.dataConsumer.DataConsumerResponse;
import com.open_data_backend.services.dataConsumer.DataConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController @RequiredArgsConstructor @RequestMapping("/api/dataConsumer")
public class DataConsumerController {
    private final DataConsumerService dataConsumerService;

    @GetMapping("/get/all")
    public List<DataConsumerResponse> getAllDataConsumers() {
        return dataConsumerService.getAllDataConsumers();
    }

    @GetMapping("/get/byId/{uuid}")
    public DataConsumerResponse getDataConsumerById(@PathVariable UUID uuid) {
        return dataConsumerService.getDataConsumerById(uuid);
    }

    @GetMapping("/get/byName/{name}")
    public DataConsumerResponse getDataConsumerByName(@PathVariable String name) {
        return dataConsumerService.getDataConsumerByName(name);
    }

    @GetMapping("/get/byEmail/{email}")
    public DataConsumerResponse getDataConsumerByEmail(@PathVariable String email) {
        return dataConsumerService.getDataConsumerByEmail(email);
    }

    @DeleteMapping("/delete/byId/{id}")
    public Boolean deleteDataConsumer(@PathVariable UUID id) {
        return dataConsumerService.deleteDataConsumerById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<DataConsumerResponse> saveDataConsumer(
            @RequestParam("name") String name,
            @RequestParam("email") String email) throws IOException {
        DataConsumerResponse dataConsumerResponse = dataConsumerService.addDataConsumer(name,email);
        return ResponseEntity.ok(dataConsumerResponse);
    }

    @PutMapping(value = "/update/byId/{id}")
    public ResponseEntity<DataConsumerResponse> updateDataConsumerById(
            @PathVariable UUID id,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "email",required = false) String email) throws IOException {
        DataConsumerResponse dataConsumerResponse= dataConsumerService.updateDataConsumerById(id, name,email);
        return ResponseEntity.ok(dataConsumerResponse);
    }

}
