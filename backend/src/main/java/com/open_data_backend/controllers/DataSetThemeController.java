package com.open_data_backend.controllers;

import com.open_data_backend.entities.DataSetTheme;
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
    public List<DataSetTheme> getAllThemes() {
        return dataSetThemeService.getAllThemes();
    }

    @GetMapping("/get/byId/{id}")
    public DataSetTheme getThemeById(@PathVariable UUID id) {
        return dataSetThemeService.getThemeById(id);
    }

    @GetMapping("/get/byName/{name}")
    public DataSetTheme getThemeByName(@PathVariable String name) {
        return dataSetThemeService.getThemeByName(name);
    }

    @PostMapping("/save")
    public ResponseEntity<DataSetTheme> saveTheme(
            @RequestParam(value = "name") String name, //by default required = true
            @RequestParam("description") String description,
            @RequestParam(value = "icon", required = true) MultipartFile file) throws IOException {
        DataSetTheme savedTheme = dataSetThemeService.saveTheme(name,description,file);
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
    public ResponseEntity<DataSetTheme> updateTheme(
            @PathVariable UUID id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "icon", required = false) MultipartFile icon) throws IOException {
        DataSetTheme updatedTheme = dataSetThemeService.updateThemeById(id, name, description, icon);
        return ResponseEntity.ok(updatedTheme);
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
