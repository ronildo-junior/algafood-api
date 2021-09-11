package com.ronijr.algafoodapi.domain.model.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Value
public class DailySale {
    Date date;
    Long amount;
    BigDecimal avg;
    BigDecimal total;

    public DailySale (Date date, Long amount, BigDecimal total) {
        this.date = date;
        this.amount = amount;
        this.total = total;
        this.avg = total.divide(BigDecimal.valueOf(amount), RoundingMode.HALF_EVEN);
    }
}