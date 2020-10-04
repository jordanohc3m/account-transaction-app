package com.accounttransactions.service;

import com.accounttransactions.entity.OperationType;
import java.util.Optional;

public interface IOperationType {

    Optional<OperationType> findByOperationTypeId(Long id);


}
