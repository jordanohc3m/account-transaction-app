package com.accounttransactions.repository;

import com.accounttransactions.entity.Account;
import com.accounttransactions.entity.OperationType;
import com.accounttransactions.entity.Transaction;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional <Transaction> findFirstByEventDateIsBetweenAndAccountAndAumountEqualsAndOperationType(Date startDate, Date endDate, Account account, Double aumount, OperationType operationType);

}
