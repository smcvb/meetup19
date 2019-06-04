package com.meetup.giftcard.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import com.meetup.giftcard.coreapi.CardRedeemedEvent;
import com.meetup.giftcard.coreapi.RedeemCardCommand;
import com.meetup.order.coreapi.ConfirmGiftCardPaymentCommand;
import com.meetup.order.coreapi.OrderPlacedEvent;
import com.meetup.order.coreapi.RejectGiftCardPaymentCommand;

@Profile("saga")
@Saga
public class GiftCardPaymentSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    private String orderId;

    @StartSaga
    @SagaEventHandler(associationProperty = "cardId")
    public void on(OrderPlacedEvent event) {
        orderId = event.getOrderId();
        try {
            commandGateway.send(new RedeemCardCommand(event.getCardId(), orderId, event.getGiftCardAmount())).join();
        } catch (Exception e) {
            commandGateway.send(new RejectGiftCardPaymentCommand(orderId, event.getCardId()));
        }
    }

    @SagaEventHandler(associationProperty = "cardId")
    public void on(CardRedeemedEvent event) {
        if (event.getTransactionId().equals(orderId)) {
            commandGateway.send(new ConfirmGiftCardPaymentCommand(orderId, event.getCardId(), event.getAmount()));
            SagaLifecycle.end();
        }
    }

}
