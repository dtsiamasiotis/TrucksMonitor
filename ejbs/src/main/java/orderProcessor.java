import java.util.ArrayList;
import java.util.List;

public class orderProcessor {
    private List<order> orders = new ArrayList<order>();

    public void addOrder(order newOrder)
    {
        orders.add(newOrder);
    }

    public void addCandidateTruck(truck Truck, int orderId)
    {
        for(order Order:orders)
        {
            if(Order.getId()==orderId)
                Order.addToCandidateTrucks(Truck);
        }
    }
}
