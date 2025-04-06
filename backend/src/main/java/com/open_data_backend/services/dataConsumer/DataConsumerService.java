package com.open_data_backend.services.dataConsumer;

import com.open_data_backend.entities.DataConsumer;
import java.util.List;
import java.util.UUID;

public interface DataConsumerService {
    List<DataConsumer> getAllDataConsumers();
    DataConsumer getDataConsumerById(UUID id);
    DataConsumer getDataConsumerByName(String name);
    DataConsumer getDataConsumerByEmail(String email);

    Boolean deleteDataConsumerById(UUID id);
    DataConsumer updateDataConsumerById(UUID id, String name,String email);
    DataConsumer addDataConsumer(String name,String email);
}
