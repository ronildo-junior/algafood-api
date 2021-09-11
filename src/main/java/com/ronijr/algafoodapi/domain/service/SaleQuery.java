package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.filter.DailySaleFilter;
import com.ronijr.algafoodapi.domain.model.dto.DailySale;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleQuery {
    List<DailySale> queryDailySales(DailySaleFilter filter);
}