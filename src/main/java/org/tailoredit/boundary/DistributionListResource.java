package org.tailoredit.boundary;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.tailoredit.control.DistributionListController;
import org.tailoredit.control.DuplicateEntryException;
import org.tailoredit.entity.DistributionListEntry;

@Path("/distribution-list")
public class DistributionListResource {

    public static final String EXCEPTION_PREFIX = "Unable to add list entry: ";

    @Inject
    DistributionListController distributionListController;

    @ServerExceptionMapper
    public RestResponse<String> mapException(DuplicateEntryException ex) {
        return RestResponse.status(Response.Status.BAD_REQUEST, EXCEPTION_PREFIX + ex.getMessage());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDistributionList() {
        return Response.ok().entity(distributionListController.getAllEntries()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEntryToDistributionList(DistributionListEntry distributionListEntry) {
        distributionListController.addDistributionListEntry(distributionListEntry);
        return Response.ok().entity(distributionListController.getAllEntries()).build();
    }

}
