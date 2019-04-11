import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.*;
import java.util.List;

@Stateful
public class dbManager {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Inject
    @transaction
    private Event<order> incomingOrder;

    public EntityManager getEntityManager()
    {
        EntityManager em = entityManagerFactory.createEntityManager();
        return  em;
    }

    public void addTruck(truck Truck)
    {
        EntityManager em = getEntityManager();
        em.persist(Truck);
    }

    public void removeTruck(Integer id)
    {
        EntityManager em = getEntityManager();
        em.remove(em.find(truck.class,id));
    }

    public void addOrder(order Order)
    {
        EntityManager em = getEntityManager();
        incomingOrder.fire(Order);
        em.persist(Order);
    }

    public List<truck> getTrucks()
    {
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM trucks",truck.class);
        List<truck> trucks = q.getResultList();
        return trucks;
    }
}
