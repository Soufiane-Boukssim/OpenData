package com.open_data_backend.mappers;

import com.open_data_backend.dtos.dataConsumer.DataConsumerRequest;
import com.open_data_backend.dtos.dataConsumer.DataConsumerResponse;
import com.open_data_backend.entities.DataConsumer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class DataConsumerMapper {

    private final ModelMapper modelMapper;

    public DataConsumer convertToEntity(DataConsumerRequest dataConsumerRequest) {
        return modelMapper.map(dataConsumerRequest, DataConsumer.class);
    }

    public DataConsumerResponse convertToResponse(DataConsumer dataConsumer) {
        return modelMapper.map(dataConsumer, DataConsumerResponse.class);
    }
}
