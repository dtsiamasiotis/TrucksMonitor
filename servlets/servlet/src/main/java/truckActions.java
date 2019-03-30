import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ws.rs.*;
import javax.persistence.*;
import javax.ws.rs.core.Response;

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
        //return "OK";
        return Response.ok("")
              //  .header("Access-Control-Allow-Origin", "*")
              //  .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
               // .header("Access-Control-Allow-Credentials", "true")
                //.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                //.header("Access-Control-Max-Age", "1209600")
                .build();

       // return Response.status(200).build();
    }

   /* @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok("")
            //    .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }*/

    @GET
    @Path("/deleteTruck")
    public String handleDeleteTruck(@QueryParam("id") Integer id)
    {
        dbManager.removeTruck(id);
        return "OK";
    }

}
