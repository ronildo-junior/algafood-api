package com.ronijr.algafoodapi.domain.model.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.util.Date;

@Value
public class DailySale {
    Date date;
    Long amount;
    BigDecimal total;
}
