package com.meetup.giftcard.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.meetup.giftcard.coreapi.CannotRedeemNegativeAmountException;
import com.meetup.giftcard.coreapi.CardIssuedEvent;
import com.meetup.giftcard.coreapi.CardRedeemedEvent;
import com.meetup.giftcard.coreapi.IssueCardCommand;
import com.meetup.giftcard.coreapi.NotEnoughFundsException;
import com.meetup.giftcard.coreapi.RedeemCardCommand;

@Aggregate
public class GiftCard {

    @AggregateIdentifier
    private String cardId;
    private int balance;

    public GiftCard() {
    }

    @CommandHandler
    public GiftCard(IssueCardCommand command) {
        AggregateLifecycle.apply(new CardIssuedEvent(command.getCardId(), command.getAmount()));
    }

    @CommandHandler
    public void handle(RedeemCardCommand command) throws NotEnoughFundsException, CannotRedeemNegativeAmountException {
        int redeemAmount = command.getAmount();
        if (redeemAmount > balance) {
            throw new NotEnoughFundsException();
        }
        if (redeemAmount < 0) {
            throw new CannotRedeemNegativeAmountException();
        }

        AggregateLifecycle.apply(new CardRedeemedEvent(cardId, command.getTransactionId(), redeemAmount));
    }

    @EventSourcingHandler
    public void on(CardIssuedEvent event) {
        cardId = event.getCardId();
        balance = event.getAmount();
    }

    @EventSourcingHandler
    public void on(CardRedeemedEvent event) {
        balance -= event.getAmount();
    }
}
