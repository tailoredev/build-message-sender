package org.tailoredit.messaging.poc.boundary;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.tailoredit.messaging.poc.entity.OutboundMessage;

import java.util.ArrayList;
import java.util.List;

@Getter
@ApplicationScoped
public class MessageClient {

    private final List<OutboundMessage> sentMessages;

    public MessageClient() {
        sentMessages = new ArrayList<>();
    }

    public void sendMessage(final OutboundMessage outboundMessage) {
        sentMessages.add(outboundMessage);
    }

}
