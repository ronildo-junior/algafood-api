package com.ronijr.algafoodapi.domain.service.report;

import com.ronijr.algafoodapi.domain.filter.DailySaleFilter;

public interface SaleReport {
    byte[] issueDailySales(DailySaleFilter filter, String timeOffset);
}