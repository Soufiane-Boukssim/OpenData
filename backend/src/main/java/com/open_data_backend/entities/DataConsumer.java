package com.open_data_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class DataConsumer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonIgnore
    private Long id;
    @Column(nullable = false, unique = true)
    private UUID uuid;
    private String name;
    private String email;
    @Column(nullable = false)
    private boolean deleted = false;
    //se connecter pour laisser des commentaire
}
