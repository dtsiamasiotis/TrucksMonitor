import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
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
        //poll trucks for their current position
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
}
