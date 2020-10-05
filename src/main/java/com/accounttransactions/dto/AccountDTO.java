package com.accounttransactions.dto;

import com.accounttransactions.entity.Account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public AccountDTO() {
    }

    public AccountDTO(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Fill document_number field")
    @JsonProperty("document_number")
    private String documentNumber;

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


    public Account toEntity() {
        Account account = new Account();
        account.setId(this.id);
        account.setDocumentNumber(this.documentNumber);
        return account;
    }
}
