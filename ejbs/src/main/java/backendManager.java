import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;



@Startup
@Singleton
public class backendManager {

    private List<truck> trucks = new ArrayList<truck>();

    @EJB
    private orderProcessor orderProcessor;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Resource
    TimerService timerService;

    @PostConstruct
    public void init()
    {

        //EntityManager em = entityManagerFactory.createEntityManager();
        //Query q = em.createNativeQuery("SELECT * FROM trucks",truck.class);
        //trucks = q.getResultList();

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

    public void connectFromTruck(Session session)
    {
       truck Truck = new truck();
       Truck.setSession(session);
       trucks.add(Truck);
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
