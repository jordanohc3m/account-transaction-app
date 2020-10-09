package com.accounttransactions.service.impl;

import com.accounttransactions.entity.Account;
import com.accounttransactions.repository.AccountRepository;
import com.accounttransactions.service.IAccountService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class.getName());

    private AccountRepository repository;

    public AccountService(final AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account create(Account account) {
        LOGGER.info("Creating new Account...");
        return repository.saveAndFlush(account);
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Account> findByAccountId(Long accountId) {
        return repository.findById(accountId);
    }


}
