package com.open_data_backend.controllers;

import com.open_data_backend.dtos.dataSetTheme.DataSetThemeResponse;
import com.open_data_backend.services.dataSetTheme.DataSetThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/api/themes") @RequiredArgsConstructor
public class DataSetThemeController {
    private final DataSetThemeService dataSetThemeService;

    @GetMapping("/get/all")
    public List<DataSetThemeResponse> getAllThemes() {
        return dataSetThemeService.getAllThemes();
    }

    @GetMapping("/get/byId/{id}")
    public DataSetThemeResponse getThemeById(@PathVariable UUID id) {
        return dataSetThemeService.getThemeById(id);
    }

    @GetMapping("/get/byName/{name}")
    public DataSetThemeResponse getThemeByName(@PathVariable String name) {
        return dataSetThemeService.getThemeByName(name);
    }

    @PostMapping("/save")
    public ResponseEntity<DataSetThemeResponse> saveTheme(
            @RequestParam(value = "name") String name, //by default required = true
            @RequestParam("description") String description,
            @RequestParam(value = "icon") MultipartFile file) throws IOException {
        DataSetThemeResponse savedTheme = dataSetThemeService.saveTheme(name,description,file);
        return ResponseEntity.ok(savedTheme);
    }

    @GetMapping("/upload/image/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        byte[] image = dataSetThemeService.getImage(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    @PutMapping(value = "/update/byId/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<DataSetThemeResponse> updateTheme(
            @PathVariable UUID id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "icon", required = false) MultipartFile icon) throws IOException {
        DataSetThemeResponse updatedThemeResponse = dataSetThemeService.updateThemeById(id, name, description, icon);
        return ResponseEntity.ok(updatedThemeResponse);
    }

    @DeleteMapping("/delete/byId/{id}")
    public Boolean deleteTheme(@PathVariable UUID id) {
        return dataSetThemeService.deleteThemeById(id);
    }

    @GetMapping("/count")
    public Long getNumberOfTheme() {
        return dataSetThemeService.getNumberOfTheme();
    }
}
