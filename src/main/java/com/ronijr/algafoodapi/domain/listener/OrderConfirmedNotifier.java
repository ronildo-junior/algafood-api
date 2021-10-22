package com.ronijr.algafoodapi.domain.listener;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.events.OrderConfirmedEvent;
import com.ronijr.algafoodapi.domain.model.Order;
import com.ronijr.algafoodapi.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderConfirmedNotifier {
    private final EmailService emailService;
    private final AppMessageSource messageSource;

    @TransactionalEventListener
    public void notifyConfirmation(OrderConfirmedEvent event) {
        Order order = event.getOrder();
        notifyByEmail(order);
    }

    private void notifyByEmail(Order order) {
        var message = EmailService.Message.builder().
                subject(order.getRestaurant().getName() + " - " + messageSource.getMessage("order.confirmed")).
                recipient(order.getCustomer().getEmail()).
                body("emails/order-confirmed.html").
                variable("order", order).
                build();

        emailService.send(message);
    }
}