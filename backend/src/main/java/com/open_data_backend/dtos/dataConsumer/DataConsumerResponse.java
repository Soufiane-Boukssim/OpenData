package com.open_data_backend.dtos.dataConsumer;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DataConsumerResponse {
    private UUID uuid;
    private String name;
    private String email;
}
