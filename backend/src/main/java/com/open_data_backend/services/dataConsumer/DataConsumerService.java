package com.open_data_backend.services.dataConsumer;

import com.open_data_backend.dtos.dataConsumer.DataConsumerResponse;
import java.util.List;
import java.util.UUID;

public interface DataConsumerService {
    List<DataConsumerResponse> getAllDataConsumers();
    DataConsumerResponse getDataConsumerById(UUID id);
    DataConsumerResponse getDataConsumerByName(String name);
    DataConsumerResponse getDataConsumerByEmail(String email);

    Boolean deleteDataConsumerById(UUID id);
    DataConsumerResponse updateDataConsumerById(UUID id, String name,String email);
    DataConsumerResponse addDataConsumer(String name,String email);
}
