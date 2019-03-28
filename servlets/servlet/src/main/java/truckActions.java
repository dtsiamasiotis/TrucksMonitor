import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.persistence.*;
import javax.ws.rs.QueryParam;

@Local @Path("/truckActions")
public class truckActions{


    private truck Truck;
    @EJB
    private dbManager dbManager;

    @POST
    @Path("/registerTruck")
    public String handleRegisterTruck(truck newTruck)
    {


        //Truck = new truck();
       // Truck.setLicenceplate(newTruck.getLicenceplate());
        System.out.println(newTruck.getLicenceplate());
        //dbManager.addTruck(Truck);
        return "OK";
       // return Response.status(200).build();
    }

    @POST
    @Path("/deleteTruck")
    public String handleDeleteTruck(@QueryParam("id") Integer id)
    {
        dbManager.removeTruck(id);
        return "OK";
    }

}
