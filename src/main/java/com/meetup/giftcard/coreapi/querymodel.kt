package com.meetup.giftcard.coreapi

import java.time.Instant
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class CardSummary @JvmOverloads constructor(
    @Id val cardId: String? = null,
    val initialValue: Int? = null,
    val issuedAt: Instant? = null,
    var remainingValue: Int? = null,
    var numberOfTransactions: Int = 0
)