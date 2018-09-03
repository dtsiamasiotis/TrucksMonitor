import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Local @Path("/actions")
public class registerTruck{

    @EJB
    private truck Truck;


    @GET
    @Path("/registerTruck")
    public String handleRequest()
    {
        System.out.println("in service");
        Truck.setLicencePlate("sfdfds");

        return "OK";
       // return Response.status(200).build();
    }


}
