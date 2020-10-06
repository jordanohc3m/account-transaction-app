package com.accounttransactions.dto;

import com.accounttransactions.entity.Account;
import com.accounttransactions.entity.OperationType;
import com.accounttransactions.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public Transaction toEntity() {
        final Account account = Account.builder().id(this.accountId).build();

        OperationType operationType = new OperationType();
        operationType.setId(this.operationTypeId);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setOperationType(operationType);
        transaction.setAumount(this.aumount);
        return transaction;
    }
}
