import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.json.simple.JSONObject;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
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

    private orderProcessor orderProcessor = new orderProcessor();;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Resource
    TimerService timerService;

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
        orderProcessor.addOrder(newOrder);
        pollTrucksForPosition(newOrder.getId());

        long duration = 10000;
        Timer timer = timerService.createSingleActionTimer(duration, new TimerConfig());
        //poll ta trucks alla me to orderid to opoio apantane pisw mazi me ti thesi tous. Kathe order exei mia lista
        //me ta fortiga kai tis theseis tous kai kaneis ekei add otan apantisei to fortigo. OrderProcessor antikeimeno
        //pou exei oura me ta orders. Otan erxetai neo order to kaneis push ekei. Molis ginei to push, trexei
        //synchronized block pou pairnei to pio palio apo tin oura kai psaxnei kalitero sindiasmo apostasi-xrono
    }

    public void connectFromTruck(Session session,String message)
    {
        String parts[] = message.split(":");
        for (truck Truck:trucks) {
            if(Truck.getId()==Integer.parseInt(parts[1])){
                Truck.setSession(session);
                System.out.println("Truck:"+Truck.getId()+"connected");
            }

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
        String googleJson = sendRequestToGoogleApi("38.006726,23.863777");
        //String googleJson = "{\"destination_addresses\": [\"Kountouriotou 253, Pireas 185 36, Greece\"],\"origin_addresses\": [\"Patriarchou Grigoriou E 5, Ag. Paraskevi 153 41, Greece\"],\"rows\": [{\"elements\": [{\"distance\": {\"text\": \"20.8 mi\",\"value\": 33486},\"duration\": {\"text\": \"33 mins\",\"value\": 1974},\"status\": \"OK\"}],}],\"status\": \"OK\"}";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        googleResponse res = gson.fromJson(googleJson,googleResponse.class);
        return;

    }

    public truck setCoordinatesFromClient(Session session,String message)
    {
        for(truck Truck:trucks)
        {
            Session truckSession = Truck.getSession();
            if(truckSession==session)
            {
                String[] coordinatesParts = message.split("\\|")[0].split(":");
                String lat = (coordinatesParts[1].split(","))[0];
                String lng = (coordinatesParts[1].split(","))[1];
                Truck.setLat(lat);
                Truck.setLng(lng);
                return Truck;
            }
        }

        return null;
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


    public void pollTrucksForPosition(int orderId)
    {
        for(truck Truck:trucks)
        {
            Session session = Truck.getSession();
            if(session!=null && session.isOpen())
                session.getAsyncRemote().sendText("sharePosition|orderId:"+orderId);
        }
    }

    public void handleResponseForOrder(Session session,String message)
    {
        truck Truck = setCoordinatesFromClient(session,message);
        String orderIdStr = message.split("\\|")[1].split(":")[1];
        int orderId = Integer.parseInt(orderIdStr);
        orderProcessor.addCandidateTruck(Truck,orderId);

    }

    @Timeout
    public void calculateDistances()
    {
        orderProcessor.findClosestTruck();
    }
}
