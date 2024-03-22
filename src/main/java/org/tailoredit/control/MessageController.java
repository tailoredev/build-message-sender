package org.tailoredit.control;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.tailoredit.boundary.MessageClient;
import org.tailoredit.entity.OutboundMessage;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class MessageController {

    @Inject
    DistributionListController distributionListController;

    @Inject
    MessageClient messageClient;

    @Getter
    private final Set<OutboundMessage> messageQueue;

    public MessageController() {
        messageQueue = new HashSet<>();
    }

    public void sendScheduledMessageToAll(final String message) {
        validateMessageCanBeSent(message);

        distributionListController.getAllEntries().stream()
                .map(listEntry -> OutboundMessage.builder().number(listEntry.getNumber()).message(message).build())
                .filter(outboundMessage -> !messageQueue.contains(outboundMessage))
                .forEach(messageQueue::add);
    }

    public void sendInstantMessageToAll(final String message) {
        validateMessageCanBeSent(message);

        distributionListController.getAllEntries().forEach(
                listEntry -> messageClient.sendMessage(new OutboundMessage(listEntry.getNumber(), message)));
    }

    public void clearMessageQueue() {
        messageQueue.clear();
    }

    private void validateMessageCanBeSent(final String message) {
        if (message == null || message.isEmpty()) {
            throw new EmptyMessageException();
        }

        if (distributionListController.getAllEntries().isEmpty()) {
            throw new DistributionListEmptyException();
        }
    }

    @Scheduled(every = "5m")
    void sendMessageQueue() {
        messageQueue.forEach(outboundMessage -> messageClient.sendMessage(outboundMessage));
        clearMessageQueue();
    }

}