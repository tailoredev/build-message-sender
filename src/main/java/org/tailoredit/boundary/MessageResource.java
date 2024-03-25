package org.tailoredit.boundary;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.tailoredit.control.DistributionListEmptyException;
import org.tailoredit.control.EmptyMessageException;
import org.tailoredit.control.MessageController;

@Path("/message")
public class MessageResource {

    public static final String EXCEPTION_PREFIX = "Unable to send message: ";

    @Inject
    MessageController messageController;

    @Inject
    MessageClient messageClient;

    @ServerExceptionMapper
    public RestResponse<String> mapException(DistributionListEmptyException ex) {
        return RestResponse.status(Response.Status.BAD_REQUEST, EXCEPTION_PREFIX + ex.getMessage());
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(EmptyMessageException ex) {
        return RestResponse.status(Response.Status.BAD_REQUEST, EXCEPTION_PREFIX + ex.getMessage());
    }

    @GET
    @Path("/queued")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQueuedMessages() {
        return Response.ok().entity(messageController.getMessageQueue()).build();
    }

    @GET
    @Path("/sent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSentMessages() {
        return Response.ok().entity(messageClient.getSentMessages()).build();
    }

    @POST
    @Path("/send/all/instant")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendInstantMessageToAll(final String messageToSend) {
        messageController.sendInstantMessageToAll(messageToSend);
        return Response.ok().build();
    }

    @POST
    @Path("/send/all/queued")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendQueuedMessageToAll(final String messageToSend) {
        messageController.sendScheduledMessageToAll(messageToSend);
        return Response.ok().build();
    }

    @DELETE
    @Path("/queued")
    public Response deleteQueueMessages() {
        messageController.clearMessageQueue();
        return Response.ok().build();
    }

}
