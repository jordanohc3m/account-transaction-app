package com.accounttransactions.entity;

import com.accounttransactions.dto.AccountDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

@Entity(name = "ACCOUNT")
public class Account {

    public Account() {
    }

    public Account(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @Id
    @Column(name = "ACCOUNT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @NotNull
    @NumberFormat(pattern = "#,###,###,###.##")
    @Column(name = "CREDIT_LIMIT")
    private Double creditLimit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public AccountDTO toDto() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(this.id);
        accountDTO.setDocumentNumber(this.documentNumber);
        accountDTO.setCreditLimit(this.creditLimit);
        return accountDTO;
    }
}
