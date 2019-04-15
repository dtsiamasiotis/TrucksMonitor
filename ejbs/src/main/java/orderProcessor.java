import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class orderProcessor {
    @EJB
    private dbManager dbManager;

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
                String googleJson = sendRequestToGoogleApi("38.006726,23.863777");
                //String googleJson = "{\"destination_addresses\": [\"Kountouriotou 253, Pireas 185 36, Greece\"],\"origin_addresses\": [\"Patriarchou Grigoriou E 5, Ag. Paraskevi 153 41, Greece\"],\"rows\": [{\"elements\": [{\"distance\": {\"text\": \"20.8 mi\",\"value\": 33486},\"duration\": {\"text\": \"33 mins\",\"value\": 1974},\"status\": \"OK\"}],}],\"status\": \"OK\"}";
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                googleResponse res = gson.fromJson(googleJson,googleResponse.class);
                String duration = res.getRows()[0].getElements()[0].getDuration().get("value");
                long durationLong = Long.parseLong(duration);
                System.out.println(duration);
                if(durationLong<smallestDuration)
                {
                    smallestDuration = durationLong;
                    closestTruck = Truck;
                }
            }

            closestTruck.setCurrentorderid(Order.getId());
            dbManager.updateTruck(closestTruck);
        }
    }

    public String sendRequestToGoogleApi(String originsCoordinates)
    {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:4000/google_api");//"https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+originsCoordinates+"&destinations=Kountouriotou 253,Pireas&departure_time=now&key=AIzaSyDW7z9-4rKR6W5kXTQ1AjiKB_2JSNsl3ko");

        Response response = target.request().get();
        //Read output in string format
        String responseStr = response.readEntity(String.class);


        System.out.println(responseStr);
        response.close();
        return responseStr;
        //return "";
    }
}
