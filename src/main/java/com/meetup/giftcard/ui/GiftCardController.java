package com.meetup.giftcard.ui;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.meetup.giftcard.coreapi.CardSummary;
import com.meetup.giftcard.coreapi.FindAllCardSummariesQuery;
import com.meetup.giftcard.coreapi.FindCardSummaryQuery;
import com.meetup.giftcard.coreapi.IssueCardCommand;
import com.meetup.giftcard.coreapi.RedeemCardCommand;

@Profile("ui")
@RestController
public class GiftCardController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public GiftCardController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @GetMapping("/command/issue")
    public void issueCard() {
        String cardId = UUID.randomUUID().toString();

        commandGateway.send(new IssueCardCommand(cardId, 250)).thenRun(
                () -> commandGateway.send(new RedeemCardCommand(cardId, "normal transaction", 50))
        );
    }

    @GetMapping("/command/funds")
    public void issueInsufficientFunds() {
        String cardId = UUID.randomUUID().toString();

        commandGateway.sendAndWait(new IssueCardCommand(cardId, 250));
        commandGateway.sendAndWait(new RedeemCardCommand(cardId, "normal transaction", 50));
        commandGateway.sendAndWait(new RedeemCardCommand(cardId, "not enough funds transaction", 220));
    }

    @GetMapping("/command/negative")
    public void issueNegativeAmount() {
        String cardId = UUID.randomUUID().toString();

        commandGateway.sendAndWait(new IssueCardCommand(cardId, 250));
        commandGateway.sendAndWait(new RedeemCardCommand(cardId, "normal transaction", 50));
        commandGateway.sendAndWait(new RedeemCardCommand(cardId, "negative transaction", -10));
    }

    @GetMapping("/query/cardsummary/{cardId}")
    public CompletableFuture<CardSummary> retrieve(@PathVariable("cardId") String cardId) {
        return queryGateway.query(new FindCardSummaryQuery(cardId), ResponseTypes.instanceOf(CardSummary.class));
    }

    @GetMapping("/query/cardsummaries")
    public CompletableFuture<List<CardSummary>> findAll() {
        return queryGateway.query(new FindAllCardSummariesQuery(), ResponseTypes.multipleInstancesOf(CardSummary.class));
    }
}
