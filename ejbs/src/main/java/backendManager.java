import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Startup
@Singleton
public class backendManager {

    private List<truck> trucks = new ArrayList<truck>();

    @EJB
    private orderProcessor orderProcessor;

    @Inject
    private messageDecoder messageDecoder;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Resource
    TimerService timerService;

    @EJB
    private dbManager dbManager;


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

   /* public void connectFromTruck(Session session,String message)
    {
        String parts[] = message.split(":");
        for (truck Truck:trucks) {
            if(Truck.getId()==Integer.parseInt(parts[1])){
                Truck.setSession(session);
                System.out.println("Truck:"+Truck.getId()+"connected");
            }

        }
    }*/

    public void connectFromTruck(Session session,String licencePlate)
    {
       truck resTruck = dbManager.findTruckByLicencePlate(licencePlate);
       resTruck.setSession(session);
       trucks.add(resTruck);
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


    public truck setCoordinatesFromClient(Session session,message decodedMessage)
    {
        for(truck Truck:trucks)
        {
            Session truckSession = Truck.getSession();
            if(truckSession==session)
            {
                String[] coordinatesParts = decodedMessage.getCoordinates().split(",");
                String lat = coordinatesParts[0];
                String lng = coordinatesParts[1];
                Truck.setLat(lat);
                Truck.setLng(lng);

                return Truck;
            }
        }

        return null;
    }


    public void pollTrucksForPosition(int orderId)
    {
        message Message = new message();
        Message.setOperation("sharePosition");
        messageOrder MessageOrder = new messageOrder();
        MessageOrder.setOrderId(String.valueOf(orderId));
        MessageOrder.setAddress("");
        MessageOrder.setQuantity("");
        Message.setOrder(MessageOrder);

        for(truck Truck:trucks)
        {
            Session session = Truck.getSession();
            if(session!=null && session.isOpen()){
                try{
                    session.getAsyncRemote().sendObject(Message);
                }catch (Exception e){System.out.println("error sto send");};
            }

        }
    }

    public void handleResponseForOrder(Session session,String message)
    {

        message decodedMessage = null;
        try {
            decodedMessage = messageDecoder.decode(message);
        }catch(Exception e){return;}
        if(decodedMessage.getOperation().equals("completeOrder")) {
            int orderId = Integer.parseInt(decodedMessage.getOrder().getOrderId());
            setOrderAsCompleted(orderId);
            System.out.println("completeOrder received");
        }
        else if(decodedMessage.getOperation().equals("sharePosition")){
            truck Truck = setCoordinatesFromClient(session,decodedMessage);
            int orderId = Integer.parseInt(decodedMessage.getOrder().getOrderId());
            orderProcessor.addCandidateTruck(Truck, orderId);
        }

    }

    @Timeout
    public void calculateDistances()
    {
        orderProcessor.findClosestTruck();
    }

    public void setOrderAsCompleted(int orderId)
    {
        order Order = dbManager.findOrderById(orderId);
        Date now = new Date();
        Order.setStatus("COMPLETED");
        Order.setDeliveryTime(now);
        dbManager.updateOrder(Order);
    }

}
