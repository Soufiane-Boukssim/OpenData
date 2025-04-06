package com.open_data_backend.repositories;

import com.open_data_backend.entities.DataConsumer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DataConsumerRepository extends JpaRepository<DataConsumer, Integer> {
    List<DataConsumer> findByDeletedFalse();
    DataConsumer findByUuidAndDeletedFalse(UUID id);
    DataConsumer findByNameAndDeletedFalse(String name);
    DataConsumer findByEmailAndDeletedFalse(String email);

}
