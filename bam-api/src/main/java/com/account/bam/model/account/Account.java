package com.account.bam.model.account;

import com.account.bam.model.account.balance.Balance;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "AccountId", length = 50, nullable = false)
    private String accountId;

    @Column(name = "AccountNumber", length = 50, unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "AccountTitle", length = 50, nullable = false)
    private String accountTitle;

    @Column(name = "CreationTime")
    private LocalDateTime creationTime;

    @Column(name = "UpdateTime")
    private LocalDateTime updateTime;

    @OneToMany(mappedBy ="account", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Balance> balances;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }
}