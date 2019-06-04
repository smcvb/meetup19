package com.meetup.giftcard.query;

import java.time.Instant;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.stereotype.Component;

import com.meetup.giftcard.coreapi.CardIssuedEvent;
import com.meetup.giftcard.coreapi.CardRedeemedEvent;
import com.meetup.giftcard.coreapi.CardSummary;

@Component
public class CardSummaryProjector {

    private final CardSummaryRepository cardSummaryRepository;

    public CardSummaryProjector(CardSummaryRepository cardSummaryRepository) {
        this.cardSummaryRepository = cardSummaryRepository;
    }

    @EventHandler
    public void on(CardIssuedEvent event, @Timestamp Instant issuedAt) {
        cardSummaryRepository.save(new CardSummary(event.getCardId(), event.getAmount(), issuedAt, event.getAmount()));
    }

    @EventHandler
    public void on(CardRedeemedEvent event) {
        cardSummaryRepository.findById(event.getCardId()).ifPresent(cardSummary -> {
            cardSummary.setRemainingValue(cardSummary.getRemainingValue() - event.getAmount());
            cardSummary.setNumberOfTransactions(cardSummary.getNumberOfTransactions() + 1);
        });
    }
}
