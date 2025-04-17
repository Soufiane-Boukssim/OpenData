package com.open_data_backend.repositories;

import com.open_data_backend.entities.DataProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface DataProviderRepository extends JpaRepository<DataProvider, Long> {
    List<DataProvider> findByDeletedFalse();
    DataProvider findByUuidAndDeletedFalse(UUID uuid);
    DataProvider findByNameAndDeletedFalse(String name);
    long countByDeletedFalse();
}
