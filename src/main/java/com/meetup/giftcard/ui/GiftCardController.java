package com.meetup.giftcard.ui;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meetup.giftcard.coreapi.IssueCardCommand;
import com.meetup.giftcard.coreapi.RedeemCardCommand;

@RestController
public class GiftCardController {

    private final CommandGateway commandGateway;

    public GiftCardController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
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

}
