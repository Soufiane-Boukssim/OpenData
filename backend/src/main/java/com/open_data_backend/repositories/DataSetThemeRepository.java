package com.open_data_backend.repositories;

import com.open_data_backend.entities.DataSetTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface DataSetThemeRepository extends JpaRepository<DataSetTheme, Long> {
    DataSetTheme findByNameAndDeletedFalse(String name);
    DataSetTheme findByUuidAndDeletedFalse(UUID uuid); // Rechercher par UUID et isDeleted = false
    long countByDeletedFalse();
    List<DataSetTheme> findByDeletedFalse(); // Récupérer les thèmes non supprimés

}
