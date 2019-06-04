package com.meetup.giftcard.query

import com.meetup.giftcard.coreapi.CardSummary
import org.springframework.data.jpa.repository.JpaRepository

interface CardSummaryRepository : JpaRepository<CardSummary, String>