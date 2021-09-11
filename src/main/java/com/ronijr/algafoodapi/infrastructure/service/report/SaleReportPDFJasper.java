package com.ronijr.algafoodapi.infrastructure.service.report;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.filter.DailySaleFilter;
import com.ronijr.algafoodapi.domain.service.query.SaleQuery;
import com.ronijr.algafoodapi.domain.service.report.SaleReport;
import com.ronijr.algafoodapi.infrastructure.exception.ReportException;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class SaleReportPDFJasper implements SaleReport {
    private final SaleQuery saleQuery;
    private final AppMessageSource messageSource;

    @Override
    public byte[] issueDailySales(DailySaleFilter filter, String timeOffset) {
        try {
            var inputStream = this.getClass().getResourceAsStream("/reports/daily-sales.jasper");
            var parameters = new HashMap<String, Object>();
            parameters.put("REPORT_LOCALE", LocaleContextHolder.getLocale());
            var dataSource = new JRBeanCollectionDataSource(saleQuery.queryDailySales(filter, timeOffset));
            var jasperPrint = JasperFillManager.fillReport(inputStream, parameters, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            e.printStackTrace();
            throw new ReportException(messageSource.getMessage("exception.internal.error"), e);
        }
    }
}