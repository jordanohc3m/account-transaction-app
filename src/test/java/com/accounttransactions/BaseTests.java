package com.accounttransactions;

import com.accounttransactions.dto.TransactionDTO;
import com.accounttransactions.entity.Account;
import com.accounttransactions.repository.TransactionRepository;
import com.accounttransactions.service.impl.AccountService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = DatabaseInitializer.class)
public class BaseTests {

    @Autowired
    protected AccountService accountService;

    @Autowired
    protected TransactionRepository transactionRepository;

    protected TransactionDTO createRandomTransactionDto(Long operationTypeId, Double aumount) {
        TransactionDTO transaction = new TransactionDTO();
        transaction.setAccountId(accountService.create(new Account(UUID.randomUUID().toString(), (aumount * 2))).getId());
        transaction.setOperationTypeId(operationTypeId);
        transaction.setAumount(aumount);
        return transaction;
    }



}
