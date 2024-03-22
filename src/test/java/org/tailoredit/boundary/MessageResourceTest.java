package org.tailoredit.boundary;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tailoredit.control.MessageController;
import org.tailoredit.entity.OutboundMessage;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class MessageResourceTest {

    @InjectMock
    MessageController messageController;

    @InjectMock
    MessageClient messageClient;

    @Test
    void testGetQueuedMessages() {
        final Map<String, String> queuedMessages = Map.of("0123456789", "Test Message",
                "9876543210", "Test Message");
        Mockito.when(messageController.getMessageQueue()).thenReturn(queuedMessages);

        given()
                .when()
                .contentType(ContentType.JSON)
                .get("/message/queued")
                .then()
                .statusCode(200);
        // TODO - Validate the actual response here
    }

    @Test
    void testGetSentMessages() {
        final List<OutboundMessage> sentMessages = List.of(
                OutboundMessage.builder()
                        .number("0123456789")
                        .message("Test Message")
                        .build(),
                OutboundMessage.builder()
                        .number("9876543210")
                        .message("Test Message")
                        .build()
        );
        Mockito.when(messageClient.getSentMessages()).thenReturn(sentMessages);

        given()
                .when()
                .contentType(ContentType.JSON)
                .get("/message/sent")
                .then()
                .statusCode(200);
        // TODO - Validate the actual response here
    }

    @Test
    void testSendInstantMessageToAll() {
        final String message = "Test message";

        given()
                .when()
                .body(message)
                .contentType(ContentType.JSON)
                .post("/message/send/all/instant")
                .then()
                .statusCode(200);

        Mockito.verify(messageController, Mockito.times(1)).sendInstantMessageToAll(message);
    }

    @Test
    void testSendQueuedMessageToAll() {
        final String message = "Test message";

        given()
                .when()
                .body(message)
                .contentType(ContentType.JSON)
                .post("/message/send/all/queued")
                .then()
                .statusCode(200);

        Mockito.verify(messageController, Mockito.times(1)).sendScheduledMessageToAll(message);
    }

}
