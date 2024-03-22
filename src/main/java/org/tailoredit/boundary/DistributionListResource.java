package org.tailoredit.boundary;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.tailoredit.control.DistributionListController;
import org.tailoredit.entity.DistributionListEntry;

@Path("/distribution-list")
public class DistributionListResource {

    @Inject
    DistributionListController distributionListController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDistributionList() {
        return Response.ok().entity(this.distributionListController.getAllEntries()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEntryToDistributionList(DistributionListEntry distributionListEntry) {
        this.distributionListController.addDistributionListEntry(distributionListEntry);
        return Response.ok().entity(this.distributionListController.getAllEntries()).build();
    }

}
