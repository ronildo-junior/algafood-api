package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.domain.filter.DailySaleFilter;
import com.ronijr.algafoodapi.domain.model.dto.DailySale;
import com.ronijr.algafoodapi.domain.service.SaleQuery;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class StatisticsController {
    private final SaleQuery saleQuery;

    @GetMapping("/daily-sales")
    public List<DailySale> list(DailySaleFilter filter) {
        return saleQuery.queryDailySales(filter);
    }
}