package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.model.StatisticsModel;
import com.ronijr.algafoodapi.domain.filter.DailySaleFilter;
import com.ronijr.algafoodapi.domain.model.dto.DailySale;
import com.ronijr.algafoodapi.domain.service.query.SaleQuery;
import com.ronijr.algafoodapi.domain.service.report.SaleReport;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ronijr.algafoodapi.api.hateoas.AlgaLinks.*;

@RestController
@RequestMapping(value = "/statistics", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class StatisticsController {
    private final SaleQuery saleQuery;
    private final SaleReport saleReport;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public StatisticsModel statistics() {
        var model = new StatisticsModel();
        model.add(linkToStatisticsDailySales("daily-sales"));
        return model;
    }

    @GetMapping(value = "/daily-sales", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DailySale> list(
            DailySaleFilter filter, @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
         return saleQuery.queryDailySales(filter, timeOffset);
    }

    @GetMapping(value = "/daily-sales", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> listPDF(
            DailySaleFilter filter, @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        byte[] pdf = saleReport.issueDailySales(filter, timeOffset);
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily-sales.pdf");
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_PDF).
                headers(headers).
                body(pdf);
    }
}