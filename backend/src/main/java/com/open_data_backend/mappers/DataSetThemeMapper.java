package com.open_data_backend.mappers;

import com.open_data_backend.dtos.dataSetTheme.DataSetThemeRequest;
import com.open_data_backend.dtos.dataSetTheme.DataSetThemeResponse;
import com.open_data_backend.entities.DataSetTheme;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class DataSetThemeMapper {

    private final ModelMapper modelMapper;

    public DataSetTheme convertToEntity(DataSetThemeRequest dataSetThemeRequest) {
        return modelMapper.map(dataSetThemeRequest, DataSetTheme.class);
    }

    public DataSetThemeResponse convertToResponse(DataSetTheme dataSetTheme) {
        return modelMapper.map(dataSetTheme, DataSetThemeResponse.class);
    }
}
