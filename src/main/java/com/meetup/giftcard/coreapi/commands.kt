package com.meetup.giftcard.coreapi

data class IssueCardCommand(
    val cardId: String,
    val amount: Int
)

data class RedeemCardCommand(
    val cardId: String,
    val transactionId: String,
    val amount: Int
)