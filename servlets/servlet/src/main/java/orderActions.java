import com.sun.org.apache.xpath.internal.operations.Or;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Local
@Path("/orderActions")
public class orderActions {

    private order Order;
    @EJB
    private dbManager dbManager;

    @GET
    @Path("addOrder")
    public String addOrder(@QueryParam("quantity") String quantity)
    {
        order Order = new order();
        Order.setQuantity(Integer.parseInt(quantity));
        dbManager.addOrder(Order);
        return "OK";
    }
}
