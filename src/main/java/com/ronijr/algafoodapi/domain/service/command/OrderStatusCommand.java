package com.ronijr.algafoodapi.domain.service.command;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Order;
import com.ronijr.algafoodapi.domain.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class OrderStatusCommand {
    private final OrderRepository orderRepository;
    private final AppMessageSource messageSource;

    public void confirm(String orderCode) {
        Order order = findByCode(orderCode);
        order.confirm();
    }

    public void cancel(String orderCode) {
        Order order = findByCode(orderCode);
        order.cancel();
    }

    public void delivery(String orderCode) {
        Order order = findByCode(orderCode);
        order.delivery();
    }

    private Order findByCode(String orderCode) throws EntityNotFoundException {
        return orderRepository.findByCode(orderCode).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("order.not.found", orderCode)));
    }
}