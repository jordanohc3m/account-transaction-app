package com.accounttransactions.service;

import com.accounttransactions.entity.Account;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAccountService {

    Account create(Account account);

    Page<Account> findAll(final Pageable pageable);

    Optional<Account> findByAccountId(Long accountId);
}
