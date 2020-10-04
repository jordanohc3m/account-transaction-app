package com.accounttransactions.service.impl;

import com.accounttransactions.entity.OperationType;
import com.accounttransactions.repository.OperationTypeRepository;
import com.accounttransactions.service.IOperationType;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OperationTypeService implements IOperationType {

    private OperationTypeRepository repository;

    public OperationTypeService(final OperationTypeRepository repository) {
        this.repository = repository;
    }


    @Override
    public Optional<OperationType> findByOperationTypeId(Long id) {
        return repository.findById(id);
    }
}
