package com.meetup.giftcard.query

import com.meetup.giftcard.coreapi.CardSummary
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository

@Profile("query")
interface CardSummaryRepository : JpaRepository<CardSummary, String>