package com.accounttransactions.entity;

import com.accounttransactions.dto.TransactionDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.NumberFormat;

@Entity(name = "TRANSACTION")
public class Transaction {

    public Transaction() {
    }

    public Transaction(Account account, OperationType operationType, Double aumount) {
        this.account = account;
        this.operationType = operationType;
        this.aumount = aumount;
    }

    @Id
    @Column(name = "TRANSACTION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ACCOUNT_ID", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_OPERATION_TYPE_ID", nullable = false)
    private OperationType operationType;

    @NotNull
    @NumberFormat(pattern = "#,###,###,###.##")
    @Column(name = "AMOUNT")
    private Double aumount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EVENT_DATE")
    private Date eventDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Double getAumount() {
        return aumount;
    }

    public void setAumount(Double aumount) {
        this.aumount = aumount;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public TransactionDTO toDto() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId(this.account.getId());
        transactionDTO.setOperationTypeId(this.operationType.getId());
        transactionDTO.setAumount(this.aumount);
        return transactionDTO;
    }
}
