package com.open_data_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity @Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class DataProviderOrganisation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonIgnore
    private Long id;
    @Column(nullable = false, unique = true)
    private UUID uuid;
    private String name;
    private String description;
    private String createdBy;
    private String updatedBy;
    @CreationTimestamp
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    @PreUpdate
    protected void onUpdate() {
        this.updatedOn = LocalDateTime.now();
    }
    @Column(nullable = false) @JsonIgnore
    private boolean deleted = false;
//    @OneToMany @JsonIgnore
//    private List<DataSet> dataSetList;

    @OneToMany(mappedBy = "dataProviderOrganisation") @JsonIgnore
//    @OneToMany(mappedBy = "dataProviderOrganisation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataProviderOrganisationMember> dataProviderOrganisationMembers = new ArrayList<>();

    // Liste des datasets publiés par cette organisation
    @OneToMany(mappedBy = "dataProviderOrganisation")
    private List<DataSet> dataSets;

    @Lob @Column(columnDefinition = "LONGBLOB")
    private byte[] iconData;
    private String iconPath;
    private String icon;
}
