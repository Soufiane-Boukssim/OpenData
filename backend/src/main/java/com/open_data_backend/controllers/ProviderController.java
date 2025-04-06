package com.open_data_backend.controllers;

import com.open_data_backend.entities.DataProvider;
import com.open_data_backend.services.dataProvider.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController @RequiredArgsConstructor @RequestMapping("/api/providers")
public class ProviderController {
    private final ProviderService providerService;

    @GetMapping("/get/all")
    public List<DataProvider> getAllProviders() {
        return providerService.getAllProviders();
    }

    @GetMapping("/get/byId/{uuid}")
    public DataProvider getProviderById(@PathVariable UUID uuid) {
        return providerService.getProviderById(uuid);
    }

    @GetMapping("/get/byName/{name}")
    public DataProvider getProviderByName(@PathVariable String name) {
        return providerService.getProviderByName(name);
    }

    @PutMapping(value = "/update/byId/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<DataProvider> updateProviderById(
            @PathVariable UUID id,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "icon",required = false) MultipartFile icon) throws IOException {
        DataProvider updatedProvider= providerService.updateProviderById(id, name, description, icon);
        return ResponseEntity.ok(updatedProvider);
    }

    @PostMapping("/save")
    public ResponseEntity<DataProvider> saveProvider(
            @RequestParam(value = "name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "icon") MultipartFile file) throws IOException {
        DataProvider savedProvider = providerService.saveProvider(name,description,file);
        return ResponseEntity.ok(savedProvider);
    }

    @DeleteMapping("/delete/byId/{id}")
    public Boolean deleteProvider(@PathVariable UUID id) {
        return providerService.deleteProviderById(id);
    }

    @GetMapping("/count")
    public Long getNumberOfTheme() {
        return providerService.getNumberOfProvider();
    }

    @GetMapping("/upload/image/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        byte[] image = providerService.getImage(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }


}
