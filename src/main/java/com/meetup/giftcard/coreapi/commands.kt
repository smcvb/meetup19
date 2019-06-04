package com.meetup.giftcard.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class IssueCardCommand(
    @TargetAggregateIdentifier val cardId: String,
    val amount: Int
)

data class RedeemCardCommand(
    @TargetAggregateIdentifier val cardId: String,
    val transactionId: String,
    val amount: Int
)