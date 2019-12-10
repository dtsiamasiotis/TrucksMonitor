import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class orderProcessor {
    @EJB
    private dbManager dbManager;

    private List<order> orders = new ArrayList<order>();

    @Inject
    private googleService googleService;

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

    public void findClosestTruck()
    {
        synchronized (this)
        {
            order Order = orders.remove(0);
            truck closestTruck = null;
            long smallestDuration = 1000000L;
            for(truck Truck:Order.getCandidateTrucks())
            {
                String truckCoordinates = Truck.getLat()+","+Truck.getLng();
                String destinationAddress = Order.getAddress();
                String googleJsonResp = googleService.sendDistanceRequest(truckCoordinates,destinationAddress);

                try {
                    googleResponseDistance res = googleService.unMarshallGoogleRespDistance(googleJsonResp);

                    String duration = res.getRows()[0].getElements()[0].getDuration().get("value");

                    long durationLong = Long.parseLong(duration);
                    System.out.println(duration);
                    if(durationLong<smallestDuration)
                    {
                        smallestDuration = durationLong;
                        closestTruck = Truck;
                    }
                }catch(Exception e){}
            }

            closestTruck.setCurrentorderid(Order.getId());
            dbManager.updateTruck(closestTruck);
        }
    }

}
