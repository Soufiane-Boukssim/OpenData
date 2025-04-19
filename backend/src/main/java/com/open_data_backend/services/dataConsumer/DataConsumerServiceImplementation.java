package com.open_data_backend.services.dataConsumer;

import com.open_data_backend.entities.DataConsumer;
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

    @Override
    public List<DataConsumer> getAllDataConsumers() {
        return dataConsumerRepository.findByDeletedFalse();
    }

    @Override
    public DataConsumer getDataConsumerById(UUID id) {
        DataConsumer dataConsumer = dataConsumerRepository.findByUuidAndDeletedFalse(id);
        if (dataConsumer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"DataConsumer not found with id: " + id);
        }
        return dataConsumer;
    }

    @Override
    public DataConsumer getDataConsumerByName(String name) {
        DataConsumer dataConsumer = dataConsumerRepository.findByNameAndDeletedFalse(name);
        if (dataConsumer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"DataConsumer not found with name: " + name);
        }
        return dataConsumer;
    }

    @Override
    public DataConsumer getDataConsumerByEmail(String email) {
        DataConsumer dataConsumer = dataConsumerRepository.findByEmailAndDeletedFalse(email);
        if (dataConsumer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"DataConsumer not found with email: " + email);
        }
        return dataConsumer;
    }

    @Override
    public Boolean deleteDataConsumerById(UUID id) {
        DataConsumer dataConsumer = getDataConsumerById(id);
        if (dataConsumer != null) {
            dataConsumer.setDeleted(true);
            dataConsumerRepository.save(dataConsumer);
            return true;
        }
        return false;
    }

    @Override
    public DataConsumer updateDataConsumerById(UUID id, String name,String email) {
        DataConsumer dataConsumer= getDataConsumerById(id);
        checkExistanceOfConsumer2(name,email);
        dataConsumer.setName(name);
        dataConsumer.setEmail(email);
        return dataConsumerRepository.save(dataConsumer);
    }

    @Override
    public DataConsumer addDataConsumer(String name,String email) {
        checkExistanceOfConsumer2(name,email);
        DataConsumer dataConsumer=new DataConsumer();
        dataConsumer.setUuid(UUID.randomUUID());
        dataConsumer.setName(name);
        dataConsumer.setEmail(email);
        return dataConsumerRepository.save(dataConsumer);
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

