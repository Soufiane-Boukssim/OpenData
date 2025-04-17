package com.open_data_backend.repositories;

import com.open_data_backend.entities.DataProviderOrganisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface DataProviderRepository extends JpaRepository<DataProviderOrganisation, Long> {
    List<DataProviderOrganisation> findByDeletedFalse();
    DataProviderOrganisation findByUuidAndDeletedFalse(UUID uuid);
    DataProviderOrganisation findByNameAndDeletedFalse(String name);
    long countByDeletedFalse();
}
