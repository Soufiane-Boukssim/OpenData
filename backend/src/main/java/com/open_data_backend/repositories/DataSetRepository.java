package com.open_data_backend.repositories;

import com.open_data_backend.entities.DataSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface DataSetRepository extends JpaRepository<DataSet, Long> {
    List<DataSet> findByDeletedFalse();
    List<DataSet> findByTheme_NameAndDeletedFalse(String name);
    //findByTheme_Name → Filtre sur la table DataSetTheme (theme.name = ?)
    //AndDeletedFalse → Filtre sur la table DataSet (dataset.deleted = false)

    //List<DataSet> findByProvider_NameAndDeletedFalse(String provider);
    List<DataSet> findByDataProviderOrganisation_NameAndDeletedFalse(String provider);
    DataSet findByUuidAndDeletedFalse(UUID uuid);
    DataSet findByNameAndDeletedFalse(String name);
    long countByDeletedFalse();


}
