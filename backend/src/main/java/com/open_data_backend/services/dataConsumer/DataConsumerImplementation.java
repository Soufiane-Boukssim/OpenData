package com.open_data_backend.services.dataConsumer;

import com.open_data_backend.entities.DataConsumer;
import com.open_data_backend.repositories.DataConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class DataConsumerImplementation implements DataConsumerService {
    private final DataConsumerRepository DataConsumerRepository;

    @Override
    public List<DataConsumer> getAllDataConsumers() {
        return DataConsumerRepository.findByDeletedFalse();
    }

    @Override
    public DataConsumer getDataConsumerById(UUID id) {
        DataConsumer dataConsumer = DataConsumerRepository.findByUuidAndDeletedFalse(id);
        if (dataConsumer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"DataConsumer not found with id: " + id);
        }
        return dataConsumer;
    }

    @Override
    public DataConsumer getDataConsumerByName(String name) {
        DataConsumer dataConsumer = DataConsumerRepository.findByNameAndDeletedFalse(name);
        if (dataConsumer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"DataConsumer not found with name: " + name);
        }
        return dataConsumer;
    }

    @Override
    public DataConsumer getDataConsumerByEmail(String email) {
        DataConsumer dataConsumer = DataConsumerRepository.findByEmailAndDeletedFalse(email);
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
            DataConsumerRepository.save(dataConsumer);
            return true;
        }
        return false;
    }

    @Override
    public DataConsumer updateDataConsumerById(UUID id, String name,String email) {
        DataConsumer dataConsumer= getDataConsumerById(id);
        checkExistanceOfConsumer(name,email);
        dataConsumer.setName(name);
        dataConsumer.setEmail(email);
        return DataConsumerRepository.save(dataConsumer);
    }

    @Override
    public DataConsumer addDataConsumer(String name,String email) {
        checkExistanceOfConsumer(name,email);
        DataConsumer dataConsumer=new DataConsumer();
        dataConsumer.setUuid(UUID.randomUUID());
        dataConsumer.setName(name);
        dataConsumer.setEmail(email);
        return DataConsumerRepository.save(dataConsumer);
    }


    void checkExistanceOfConsumer(String name,String email) {
        DataConsumer existingDataConsumerName = getDataConsumerByName(name);
        if (existingDataConsumerName!=null) {
            throw new ResponseStatusException(HttpStatus.IM_USED,"This name is already in use");
        }

        DataConsumer existingDataConsumerEmail = getDataConsumerByEmail(email);
        if (existingDataConsumerEmail!=null) {
            throw new ResponseStatusException(HttpStatus.IM_USED,"This email is already in use");
        }
    }


}

