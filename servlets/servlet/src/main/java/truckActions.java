import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.persistence.*;
import javax.ws.rs.QueryParam;

@Local @Path("/truckActions")
public class truckActions{


    private truck Truck;
    @EJB
    private dbManager dbManager;

    @GET
    @Path("/registerTruck")
    public String handleRegisterTruck(@QueryParam("licencePlate") String licencePlate)
    {
        System.out.println("in service");
        Truck = new truck();
        Truck.setLicenceplate(licencePlate);
        dbManager.addTruck(Truck);
        return "OK";
       // return Response.status(200).build();
    }

    @GET
    @Path("/deleteTruck")
    public String handleDeleteTruck(@QueryParam("id") Integer id)
    {
        dbManager.removeTruck(id);
        return "OK";
    }

}
