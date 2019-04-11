import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ws.rs.*;
import javax.persistence.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Local @Path("/truckActions")
public class truckActions{


    private truck Truck;
    @EJB
    private dbManager dbManager;

    @POST
    @Path("/registerTruck")
    public Response handleRegisterTruck(truck newTruck)
    {


        Truck = new truck();
        Truck.setLicenceplate(newTruck.getLicenceplate());
        dbManager.addTruck(Truck);

        return Response.ok("").build();


    }

    @GET
    @Path("/getTrucks")
    public Response handleGetTrucks()
    {
        List<truck> trucks = dbManager.getTrucks();
        JsonArray responseJson = new JsonArray();
        for(truck Truck:trucks)
        {
            JsonObject truckJson = new JsonObject();
            truckJson.addProperty("licence_plate",Truck.getLicenceplate());
            truckJson.addProperty("status",Truck.getStatus());
            truckJson.addProperty("current_order_id",Truck.getCurrenorderid());
            responseJson.add(truckJson);
        }
        return Response.ok(responseJson.toString()).build();
    }

    @GET
    @Path("/deleteTruck")
    public String handleDeleteTruck(@QueryParam("id") Integer id)
    {
        dbManager.removeTruck(id);
        return "OK";
    }

}
