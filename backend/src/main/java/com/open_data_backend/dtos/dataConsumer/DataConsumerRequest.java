package com.open_data_backend.dtos.dataConsumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DataConsumerRequest {
    private String name;
    private String email;
}
