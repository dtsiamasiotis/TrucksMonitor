import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.org.apache.xpath.internal.operations.Or;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

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

    @GET
    @Path("/getOrders")
    public Response handleGetOrders()
    {
        List<order> orders = dbManager.getOrders();
        JsonArray responseJson = new JsonArray();
        for(order Order:orders)
        {
            JsonObject orderJson = new JsonObject();
            orderJson.addProperty("id",Order.getId());
            orderJson.addProperty("quantity",Order.getQuantity());
            orderJson.addProperty("status",Order.getStatus());
            responseJson.add(orderJson);
        }
        return Response.ok(responseJson.toString()).build();
    }

    @POST
    @Path("/deleteOrder")
    public Response handleDeleteTruck(order Order)
    {
        int id = Order.getId();
        dbManager.removeOrder(id);
        return Response.ok("").build();
    }
}
