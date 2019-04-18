import com.sun.org.apache.xpath.internal.operations.Or;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Local
@Path("/orderActions")
public class orderActions {

    private order Order;
    @EJB
    private dbManager dbManager;


    @POST
    @Path("addOrder")
    public Response handleAddOrder(order newOrder)
    {


        Order = new order();
        Order.setQuantity(newOrder.getQuantity());
        dbManager.addOrder(Order);

        return Response.ok("").build();


    }
}
