package com.accounttransactions.controller;

import com.accounttransactions.dto.AccountDTO;
import com.accounttransactions.entity.Account;
import com.accounttransactions.exception.AccountNotFoundException;
import com.accounttransactions.service.IAccountService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    private IAccountService service;

    public AccountController(final IAccountService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDTO> create(@Valid @RequestBody AccountDTO accountDTO) {
        Account account = service.create(accountDTO.toEntity());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(account.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(account.toDto());
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AccountDTO> findAll(@RequestParam(defaultValue = "10", required = false) int size,
                                    @RequestParam(defaultValue = "0", required = false) int page) {

        return service.findAll(PageRequest.of(page, size)).getContent().stream().map(Account::toDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public AccountDTO findAccountByAccountId(@PathVariable final Long accountId) {
        return service.findByAccountId(accountId).map(Account::toDto).orElseThrow(AccountNotFoundException::new);
    }

}

