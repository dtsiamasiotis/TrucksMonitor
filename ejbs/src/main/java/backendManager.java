import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.websocket.Session;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;



@Startup
@Singleton
public class backendManager {

    private List<truck> trucks = new ArrayList<truck>();

    private List<order> orders = new ArrayList<order>();
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;


    @PostConstruct
    public void init()
    {
        System.out.print("backendManager created");
        EntityManager em = entityManagerFactory.createEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM trucks",truck.class);
        trucks = q.getResultList();


    }

    public void receiveNewOrder(@Observes(during = TransactionPhase.AFTER_SUCCESS) @transaction order newOrder)
    {
        orders.add(newOrder);
        pollTrucksForPosition();
        //call google api to find the distance for all of them
        //send the order to the truck with the minimum distance
    }

    public void connectFromTruck(Session session,String message)
    {
        String parts[] = message.split(":");
        for (truck Truck:trucks) {
            if(Truck.getId()==Integer.parseInt(parts[1]))
                Truck.setSession(session);
        }
    }

    public void pollTrucksForPosition()
    {
        for(truck Truck:trucks)
        {
            Session session = Truck.getSession();
            if(session!=null && session.isOpen())
                session.getAsyncRemote().sendText("sharePosition");
        }
    }

    public void testUnmarshall()  throws Exception
    {
        String googleJson = sendRequestToGoogleApi();
        //String googleJson = "{\"destination_addresses\": [\"Kountouriotou 253, Pireas 185 36, Greece\"],\"origin_addresses\": [\"Patriarchou Grigoriou E 5, Ag. Paraskevi 153 41, Greece\"],\"rows\": [{\"elements\": [{\"distance\": {\"text\": \"20.8 mi\",\"value\": 33486},\"duration\": {\"text\": \"33 mins\",\"value\": 1974},\"status\": \"OK\"}],}],\"status\": \"OK\"}";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        googleResponse res = gson.fromJson(googleJson,googleResponse.class);


    }

    public void setCoordinatesFromClient(Session session,String message)
    {
        for(truck Truck:trucks)
        {
            Session truckSession = Truck.getSession();
            if(truckSession==session)
            {
                String[] coordinatesParts = message.split(":");
                String lat = (coordinatesParts[1].split(","))[0];
                String lng = (coordinatesParts[1].split(","))[1];
                Truck.setLat(lat);
                Truck.setLng(lng);
            }
        }
    }

    public String sendRequestToGoogleApi(String originsCoordinates)
    {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+originsCoordinates+"&destinations=Kountouriotou 253,Pireas&departure_time=now&key=");
        Response response = target.request().get();
        //Read output in string format
        String responseStr = response.readEntity(String.class);
        System.out.println(responseStr);
        response.close();
        return responseStr;
    }
}
