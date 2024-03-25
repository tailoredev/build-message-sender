package org.tailoredit.messaging.poc.boundary;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.tailoredit.messaging.poc.control.DistributionListController;
import org.tailoredit.messaging.poc.control.DistributionListEntryNotFoundException;
import org.tailoredit.messaging.poc.control.DuplicateEntryException;
import org.tailoredit.messaging.poc.entity.DistributionListEntry;

@Path("/distribution-list")
public class DistributionListResource {

    public static final String ADD_EXCEPTION_PREFIX = "Unable to add list entry: ";
    public static final String DELETE_EXCEPTION_PREFIX = "Unable to delete list entry: ";

    @Inject
    DistributionListController distributionListController;

    @ServerExceptionMapper
    public RestResponse<String> mapException(DuplicateEntryException ex) {
        return RestResponse.status(Response.Status.BAD_REQUEST, ADD_EXCEPTION_PREFIX + ex.getMessage());
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(DistributionListEntryNotFoundException ex) {
        return RestResponse.status(Response.Status.BAD_REQUEST, DELETE_EXCEPTION_PREFIX + ex.getMessage());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDistributionList() {
        return Response.ok().entity(distributionListController.getAllEntries()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEntryToDistributionList(final DistributionListEntry distributionListEntry) {
        distributionListController.addDistributionListEntry(distributionListEntry);
        return Response.ok().entity(distributionListController.getAllEntries()).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEntryFromDistributionList(final DistributionListEntry distributionListEntry) {
        distributionListController.deleteEntry(distributionListEntry);
        return Response.ok().entity(distributionListController.getAllEntries()).build();
    }

    @DELETE
    @Path("/all")
    public Response deleteAllEntriesFromDistributionList() {
        distributionListController.deleteAll();
        return Response.ok().build();
    }


}
