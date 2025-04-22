package com.open_data_backend.mappers;

import com.open_data_backend.dtos.dataSet.DataSetRequest;
import com.open_data_backend.dtos.dataSet.DataSetResponse;
import com.open_data_backend.entities.DataSet;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class DataSetMapper {

    private final ModelMapper modelMapper;

    public DataSet convertToEntity(DataSetRequest dataSetRequest) {
        return modelMapper.map(dataSetRequest, DataSet.class);
    }

    public DataSetResponse convertToResponse(DataSet dataSet) {
        return modelMapper.map(dataSet, DataSetResponse.class);
    }



}
