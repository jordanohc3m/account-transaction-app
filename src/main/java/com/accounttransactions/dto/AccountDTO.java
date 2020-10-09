package com.accounttransactions.dto;

import com.accounttransactions.entity.Account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public AccountDTO() {
    }

    public AccountDTO(String documentNumber, Double creditLimit) {
        this.documentNumber = documentNumber;
        this.creditLimit = creditLimit;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Fill document_number field")
    @JsonProperty("document_number")
    private String documentNumber;

    @NotNull
    private Double creditLimit;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Account toEntity() {
        Account account = new Account();
        account.setId(this.id);
        account.setDocumentNumber(this.documentNumber);
        account.setCreditLimit(this.creditLimit);
        return account;
    }
}
