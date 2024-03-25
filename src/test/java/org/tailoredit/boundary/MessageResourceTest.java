package org.tailoredit.boundary;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.tailoredit.control.DistributionListEmptyException;
import org.tailoredit.control.EmptyMessageException;
import org.tailoredit.control.MessageController;
import org.tailoredit.entity.OutboundMessage;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class MessageResourceTest {

    @InjectMock
    MessageController messageController;

    @InjectMock
    MessageClient messageClient;

    @Test
    void testGetQueuedMessages() {
        final Set<OutboundMessage> queuedMessages = Set.of(
                OutboundMessage.builder()
                        .number("0123456789")
                        .message("Test Message")
                        .build(),
                OutboundMessage.builder()
                        .number("9876543210")
                        .message("Test Message")
                        .build()
        );
        Mockito.when(messageController.getMessageQueue()).thenReturn(queuedMessages);

        given()
                .when()
                .contentType(ContentType.JSON)
                .get("/message/queued")
                .then()
                .statusCode(200);
        // TODO - Validate the actual response body here
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
        // TODO - Validate the actual response body here
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

    @Test
    void testDistributionListEmptyExceptionsAreCorrectlyMapped() {
        final String message = "Test message";
        Mockito.doThrow(new DistributionListEmptyException())
                .when(messageController).sendInstantMessageToAll(ArgumentMatchers.eq(message));

        given()
                .when()
                .body(message)
                .contentType(ContentType.JSON)
                .post("/message/send/all/instant")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("Unable to send message: " + DistributionListEmptyException.EXCEPTION_MESSAGE));
    }

    @Test
    void testEmptyMessageExceptionsAreCorrectlyMapped() {
        final String message = "Test message";
        Mockito.doThrow(new EmptyMessageException())
                .when(messageController).sendInstantMessageToAll(ArgumentMatchers.eq(message));

        given()
                .when()
                .body(message)
                .contentType(ContentType.JSON)
                .post("/message/send/all/instant")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo(MessageResource.EXCEPTION_PREFIX + EmptyMessageException.EXCEPTION_MESSAGE));
    }

}
