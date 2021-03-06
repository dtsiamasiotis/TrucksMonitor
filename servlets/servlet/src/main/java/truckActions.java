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
        Truck.setStatus(newTruck.getStatus());
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
            truckJson.addProperty("id",Truck.getId());
            truckJson.addProperty("licence_plate",Truck.getLicenceplate());
            truckJson.addProperty("status",Truck.getStatus());
            truckJson.addProperty("current_order_id",Truck.getCurrenorderid());
            responseJson.add(truckJson);
        }
        return Response.ok(responseJson.toString()).build();
    }

    @POST
    @Path("/deleteTruck")
    public Response handleDeleteTruck(truck Truck)
    {
        int id = Truck.getId();
        dbManager.removeTruck(id);
        return Response.ok("").build();
    }

    @POST
    @Path("/validateLogin")
    public Response handleValidateLogin(LoginRequest loginRequest)
    {
        String licencePlate = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        truck resTruck = dbManager.findTruckByLicencePlate(licencePlate);
        if(resTruck==null)
            return Response.ok("{\"result\":\"wrong\"}").build();
        else{
            Driver driver = dbManager.findDriverByPassword(password);
            if(driver==null)
                return Response.ok("{\"result\":\"wrong\"}").build();
            else
                return Response.ok("{\"result\":\"correct\"}").build();
        }

    }

}
