package com.ronijr.algafoodapi.domain.service.query;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Order;
import com.ronijr.algafoodapi.domain.model.OrderItem;
import com.ronijr.algafoodapi.domain.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class OrderQuery {
    private final OrderRepository orderRepository;
    private final AppMessageSource messageSource;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findAll(Specification<Order> specification) {
        return orderRepository.findAll(specification);
    }

    public Page<Order> findAll(Specification<Order> specification, Pageable pageable) {
        return orderRepository.findAll(specification, pageable);
    }

    public Order findByCodeOrElseThrow(String code) throws EntityNotFoundException {
        return findByCode(code).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("order.not.found", code)));
    }

    public Set<OrderItem> getOrderItemList(String code) {
        Order order = findByCodeOrElseThrow(code);
        return order.getItems();
    }

    public boolean userManageOrder(Long userId, String orderCode) {
        return orderRepository.userManageOrder(userId, orderCode);
    }

    private Optional<Order> findByCode(String code) {
        return orderRepository.findByCode(code);
    }
}