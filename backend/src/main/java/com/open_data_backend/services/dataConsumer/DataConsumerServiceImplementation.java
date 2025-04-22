package com.open_data_backend.services.dataConsumer;

import com.open_data_backend.dtos.dataConsumer.DataConsumerResponse;
import com.open_data_backend.entities.DataConsumer;
import com.open_data_backend.mappers.DataConsumerMapper;
import com.open_data_backend.repositories.DataConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class DataConsumerServiceImplementation implements DataConsumerService {

    private final DataConsumerRepository dataConsumerRepository;

    private final DataConsumerMapper dataConsumerMapper;

    @Override
    public List<DataConsumerResponse> getAllDataConsumers() {
        List<DataConsumerResponse> dataConsumersResponse = new ArrayList<>();
        List<DataConsumer> dataConsumers = dataConsumerRepository.findByDeletedFalse();
        if (dataConsumers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The data consumer table is empty");
        }
        for (DataConsumer dataConsumer : dataConsumers) {
            dataConsumersResponse.add(dataConsumerMapper.convertToResponse(dataConsumer));
        }
        return dataConsumersResponse;
    }

    @Override
    public DataConsumerResponse getDataConsumerById(UUID id) {
        DataConsumer dataConsumer = dataConsumerRepository.findByUuidAndDeletedFalse(id);
        if (dataConsumer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"DataConsumer not found with id: " + id);
        }
        return dataConsumerMapper.convertToResponse(dataConsumer);
    }

    @Override
    public DataConsumerResponse getDataConsumerByName(String name) {
        DataConsumer dataConsumer = dataConsumerRepository.findByNameAndDeletedFalse(name);
        if (dataConsumer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"DataConsumer not found with name: " + name);
        }
        return dataConsumerMapper.convertToResponse(dataConsumer);
    }

    @Override
    public DataConsumerResponse getDataConsumerByEmail(String email) {
        DataConsumer dataConsumer = dataConsumerRepository.findByEmailAndDeletedFalse(email);
        if (dataConsumer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"DataConsumer not found with email: " + email);
        }
        return dataConsumerMapper.convertToResponse(dataConsumer);
    }

    @Override
    public Boolean deleteDataConsumerById(UUID id) {
        DataConsumer dataConsumer = dataConsumerRepository.findByUuidAndDeletedFalse(id);
        if (dataConsumer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"DataConsumer not found with id: " + id);
        }
        dataConsumer.setDeleted(true);
        dataConsumerRepository.save(dataConsumer);
        return true;
    }

    @Override
    public DataConsumerResponse updateDataConsumerById(UUID id, String name,String email) {
        DataConsumer dataConsumer = dataConsumerRepository.findByUuidAndDeletedFalse(id);
        if (dataConsumer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"DataConsumer not found with id: " + id);
        }
        checkExistanceOfConsumer2(name,email);
        dataConsumer.setName(name);
        dataConsumer.setEmail(email);
        dataConsumer= dataConsumerRepository.save(dataConsumer);
        return dataConsumerMapper.convertToResponse(dataConsumer);
    }

    @Override
    public DataConsumerResponse addDataConsumer(String name,String email) {
        checkExistanceOfConsumer2(name,email);
        DataConsumer dataConsumer=new DataConsumer();
        dataConsumer.setUuid(UUID.randomUUID());
        dataConsumer.setName(name);
        dataConsumer.setEmail(email);
        dataConsumer= dataConsumerRepository.save(dataConsumer);
        return dataConsumerMapper.convertToResponse(dataConsumer);

    }

    void checkExistanceOfConsumer2(String name, String email) {
        List<String> errors = new ArrayList<>();

        if (dataConsumerRepository.existsByNameAndDeletedFalse(name)) {
            errors.add("DataConsumer with name: " + name + " already exists");
        }

        if (dataConsumerRepository.existsByEmailAndDeletedFalse(email)) {
            errors.add("DataConsumer with email: " + email + " already exists");
        }

        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.IM_USED,"There are errors: " + String.join(", ", errors));
        }
    }


}

