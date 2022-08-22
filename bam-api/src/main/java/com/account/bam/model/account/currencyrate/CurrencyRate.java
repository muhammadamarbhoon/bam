package com.account.bam.model.account.currencyrate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "BaseCcy", length = 3, nullable = false)
    private String baseCcy;

    @Column(name = "TargetCcy", length = 3, nullable = false)
    private String targetCcy;

    @Column(name = "Rate", precision = 18, scale = 4, nullable = false)
    private BigDecimal rate;

    @Column(name = "CreationTime")
    private LocalDateTime creationTime;

    @Column(name = "UpdateTime")
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseCcy() {
        return baseCcy;
    }

    public void setBaseCcy(String baseCcy) {
        this.baseCcy = baseCcy;
    }

    public String getTargetCcy() {
        return targetCcy;
    }

    public void setTargetCcy(String targetCcy) {
        this.targetCcy = targetCcy;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
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
}