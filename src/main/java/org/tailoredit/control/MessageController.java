package org.tailoredit.control;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.tailoredit.boundary.MessageClient;
import org.tailoredit.entity.OutboundMessage;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MessageController {

    @Inject
    DistributionListController distributionListController;

    @Inject
    MessageClient messageClient;

    @Getter
    private final Map<String, String> messageQueue;

    public MessageController() {
        messageQueue = new HashMap<>();
    }

    public void sendScheduledMessageToAll(final String message) {
        if (message == null || message.isEmpty()) {
            throw new EmptyMessageException();
        }

        if (distributionListController.getAllEntries().isEmpty()) {
            throw new DistributionListEmptyException();
        }

        distributionListController.getAllEntries().stream()
                .filter(listEntry -> !messageQueue.containsKey(listEntry.getNumber()) || !messageQueue.get(listEntry.getNumber()).equals(message))
                .forEach(listEntry -> messageQueue.put(listEntry.getNumber(), message));
    }

    public void sendInstantMessageToAll(final String message) {
        if (message == null || message.isEmpty()) {
            throw new EmptyMessageException();
        }

        distributionListController.getAllEntries().forEach(
                listEntry -> messageClient.sendMessage(new OutboundMessage(listEntry.getNumber(), message)));
    }

    public void clearMessageQueue() {
        messageQueue.clear();
    }

    @Scheduled(every = "5m")
    void sendMessageQueue() {
        messageQueue.forEach((number, message) -> messageClient.sendMessage(new OutboundMessage(number, message)));
        clearMessageQueue();
    }

}
