package net.andreabattista.InOutFlow.rs;

import net.andreabattista.InOutFlow.business.PersistData;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/data")
public class PersistDataResource {

    @POST
    @Path("/")
    public static void persist() {
        PersistData.persist();
    }
}
