package com.meetup.order.coreapi

import org.axonframework.commandhandling.RoutingKey

data class ConfirmGiftCardPaymentCommand(
    @RoutingKey val orderId: String,
    val cardId: String,
    val giftCardAmount: Int
)

data class RejectGiftCardPaymentCommand(
    @RoutingKey val orderId: String,
    val cardId: String
)