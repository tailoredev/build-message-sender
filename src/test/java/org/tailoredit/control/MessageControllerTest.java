package org.tailoredit.control;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.tailoredit.boundary.MessageClient;
import org.tailoredit.entity.DistributionList;
import org.tailoredit.entity.DistributionListEntry;
import org.tailoredit.entity.OutboundMessage;

import java.util.List;

@QuarkusTest
public class MessageControllerTest {

    @InjectMock
    DistributionListController mockDistributionListController;

    @InjectMock
    MessageClient messageClient;

    @Inject
    MessageController messageController;

    @Test
    void testSendScheduledMessageToAllShouldThrowExceptionWhenMessageIsNullOrEmpty() {
        Assertions.assertThrows(EmptyMessageException.class, () -> messageController.sendScheduledMessageToAll(null));
        Assertions.assertThrows(EmptyMessageException.class, () -> messageController.sendScheduledMessageToAll(""));
    }

    @Test
    void testSendScheduledMessageToAllShouldThrowExceptionWhenTheDistributionListIsEmpty() {
        Mockito.when(mockDistributionListController.getAllEntries()).thenReturn(new DistributionList());

        Assertions.assertThrows(DistributionListEmptyException.class, () -> messageController.sendScheduledMessageToAll("Test message"));
    }

    @Test
    void testSendScheduledMessageToAllShouldSendMessagesWhenTheDistributionListIsPopulated() {
        final String message = "Test message";
        final DistributionList distributionList = generateDistributionList();
        Mockito.when(mockDistributionListController.getAllEntries()).thenReturn(distributionList);

        messageController.sendScheduledMessageToAll(message);

        Assertions.assertEquals(messageController.getMessageQueue().entrySet().size(), 2);
        Assertions.assertTrue(messageController.getMessageQueue().containsKey(distributionList.get(0).getNumber()));
        Assertions.assertEquals(messageController.getMessageQueue().get(distributionList.get(0).getNumber()), message);
        Assertions.assertTrue(messageController.getMessageQueue().containsKey(distributionList.get(1).getNumber()));
        Assertions.assertEquals(messageController.getMessageQueue().get(distributionList.get(1).getNumber()), message);
    }

    @Test
    void testSendInstantMessageToAllShouldThrowExceptionWhenMessageIsNullOrEmpty() {
        Assertions.assertThrows(EmptyMessageException.class, () -> messageController.sendInstantMessageToAll(null));
        Assertions.assertThrows(EmptyMessageException.class, () -> messageController.sendInstantMessageToAll(""));
    }

    @Test
    void testSendInstantMessageToAllShouldSendMessage() {
        final String message = "Test message";
        final DistributionList distributionList = generateDistributionList();
        Mockito.when(mockDistributionListController.getAllEntries()).thenReturn(distributionList);

        messageController.sendInstantMessageToAll(message);

        final ArgumentCaptor<OutboundMessage> outboundMessageArgumentCaptor = ArgumentCaptor.forClass(OutboundMessage.class);
        Mockito.verify(messageClient, Mockito.times(2)).sendMessage(outboundMessageArgumentCaptor.capture());

        final List<OutboundMessage> capturedMessages = outboundMessageArgumentCaptor.getAllValues();
        Assertions.assertEquals(capturedMessages.size(), 2);
        Assertions.assertTrue(capturedMessages.contains(new OutboundMessage(distributionList.get(0).getNumber(), message)));
        Assertions.assertTrue(capturedMessages.contains(new OutboundMessage(distributionList.get(1).getNumber(), message)));
    }

    @Test
    void testClearMessageQueue() {
        final String message = "Test message";
        final DistributionList distributionList = generateDistributionList();
        Mockito.when(mockDistributionListController.getAllEntries()).thenReturn(distributionList);

        messageController.sendScheduledMessageToAll(message);

        Assertions.assertEquals(messageController.getMessageQueue().entrySet().size(), 2);

        messageController.clearMessageQueue();

        Assertions.assertEquals(messageController.getMessageQueue().entrySet().size(), 0);
    }

    @Test
    void testSendMessageQueue() {
        final String message = "Test message";
        final DistributionList distributionList = generateDistributionList();
        Mockito.when(mockDistributionListController.getAllEntries()).thenReturn(distributionList);

        messageController.sendScheduledMessageToAll(message);

        Assertions.assertEquals(messageController.getMessageQueue().entrySet().size(), 2);

        messageController.sendMessageQueue();

        final ArgumentCaptor<OutboundMessage> outboundMessageArgumentCaptor = ArgumentCaptor.forClass(OutboundMessage.class);
        Mockito.verify(messageClient, Mockito.times(2)).sendMessage(outboundMessageArgumentCaptor.capture());

        final List<OutboundMessage> capturedMessages = outboundMessageArgumentCaptor.getAllValues();
        Assertions.assertEquals(capturedMessages.size(), 2);
        Assertions.assertTrue(capturedMessages.contains(new OutboundMessage(distributionList.get(0).getNumber(), message)));
        Assertions.assertTrue(capturedMessages.contains(new OutboundMessage(distributionList.get(1).getNumber(), message)));
    }

    private DistributionList generateDistributionList() {
        return new DistributionList(List.of(DistributionListEntry.builder()
                .name("Test User One")
                .number("0123456789")
                        .build(),
                DistributionListEntry.builder()
                        .name("Test User Two")
                        .number("9876543210")
                        .build()));
    }

}
