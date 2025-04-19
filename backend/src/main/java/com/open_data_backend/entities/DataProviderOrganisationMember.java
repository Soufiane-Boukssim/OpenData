package com.open_data_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity @Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class DataProviderOrganisationMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonIgnore
    private Long id;
    @Column(nullable = false, unique = true)
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
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

    @ManyToOne @JoinColumn(name = "data_provider_organisation_id") @JsonIgnoreProperties({"description","createdBy","updatedBy", "createdOn","updatedOn", "deleted","dataSets", "iconData","iconPath", "icon"})
    private DataProviderOrganisation dataProviderOrganisation;

    @OneToMany(mappedBy = "createdBy") @JsonIgnore
    private List<DataSet> dataSetsCreated;

}
