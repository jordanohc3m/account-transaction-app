package com.accounttransactions.entity;

import com.accounttransactions.dto.AccountDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity(name = "ACCOUNT")
public class Account {

    @Id
    @Column(name = "ACCOUNT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    public AccountDTO toDto() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(this.id);
        accountDTO.setDocumentNumber(this.documentNumber);
        return accountDTO;
    }
}
