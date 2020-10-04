package com.accounttransactions.service.impl;

import com.accounttransactions.constants.Constants;
import com.accounttransactions.entity.Account;
import com.accounttransactions.entity.OperationType;
import com.accounttransactions.entity.Transaction;
import com.accounttransactions.exception.AccountNotFoundException;
import com.accounttransactions.exception.GenericValidateRuntimeException;
import com.accounttransactions.exception.InvalidOperationTypeException;
import com.accounttransactions.repository.TransactionRepository;
import com.accounttransactions.service.ITransactionService;
import java.util.Date;
import java.util.Objects;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class.getName());

    private TransactionRepository repository;
    private AccountService accountService;
    private OperationTypeService operationTypeService;

    public TransactionService(final TransactionRepository repository,
                              final AccountService accountService,
                              final OperationTypeService operationTypeService) {
        this.repository = repository;
        this.accountService = accountService;
        this.operationTypeService = operationTypeService;
    }

    @Transactional
    @Override
    public Transaction create(Transaction transaction) {
        prepareTransaction(transaction);
        LOGGER.info("Registring new Transaction for account: {}. ", transaction.getAccount().getId());
        return repository.saveAndFlush(transaction);
    }

    private void prepareTransaction(Transaction transaction){
        validateZeroValue(transaction.getAumount());
        fillAccount(transaction.getAccount().getId());
        transaction.setOperationType(fillValidateOperationType(transaction.getOperationType().getId()));
        validateValuesWithOperation(transaction);
        validateSameOperation(transaction);
    }

    private void validateValuesWithOperation(Transaction transaction) {
        if (Objects.equals(transaction.getOperationType().getType(), Constants.NEGATIVE)) {
            if (isPositiveValue(transaction.getAumount()))
                transaction.setAumount(transaction.getAumount() * -1);
        } else {
            if (!isPositiveValue(transaction.getAumount()))
                transaction.setAumount(Math.abs(transaction.getAumount()));
        }
    }

    private boolean isPositiveValue(Double aumont) {
        return aumont > 0;
    }

    private Account fillAccount(Long id) {
        return accountService.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
    }

    private OperationType fillValidateOperationType(Long operationTypeId) {
        return operationTypeService.findByOperationTypeId(operationTypeId).orElseThrow(InvalidOperationTypeException::new);
    }

    private void validateZeroValue(Double aumount) {
        if (aumount == 0) throw new GenericValidateRuntimeException("Value must be different of zero");
    }

    private void validateSameOperation(Transaction transaction) {
        if (repository.findFirstByEventDateIsBetweenAndAccountAndAumountEqualsAndOperationType(
                new Date(System.currentTimeMillis() - Constants.DEFAULT_TIME_NEW_OPERATION),
                new Date(),
                transaction.getAccount(),
                transaction.getAumount(),
                transaction.getOperationType()).isPresent())
            throw new GenericValidateRuntimeException("The same operation for the same Account is processing less than 5 seconds");
    }
}
