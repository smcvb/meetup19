package com.meetup.order.coreapi

data class OrderPlacedEvent(
    val orderId: String,
    val cardId: String,
    val giftCardAmount: Int
)