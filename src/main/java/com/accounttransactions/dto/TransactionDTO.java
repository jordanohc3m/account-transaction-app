package com.accounttransactions.dto;

import com.accounttransactions.entity.Account;
import com.accounttransactions.entity.OperationType;
import com.accounttransactions.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

public class TransactionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @JsonProperty("account_id")
    private Long accountId;

    @NotNull
    @JsonProperty("operation_type_id")
    private Long operationTypeId;

    @NotNull
    private Double aumount;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(Long operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public Double getAumount() {
        return aumount;
    }

    public void setAumount(Double aumount) {
        this.aumount = aumount;
    }

    public Transaction toEntity() {
        Account account = new Account();
        account.setId(this.accountId);
        OperationType operationType = new OperationType();
        operationType.setId(this.operationTypeId);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setOperationType(operationType);
        transaction.setAumount(this.aumount);
        return transaction;
    }
}
