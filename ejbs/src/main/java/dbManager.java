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

    public void updateTruck(truck Truck)
    {
        EntityManager em = getEntityManager();
        em.merge(Truck);
    }

    public void addOrder(order Order)
    {
        EntityManager em = getEntityManager();
        incomingOrder.fire(Order);
        em.persist(Order);
    }

    public void removeOrder(Integer id)
    {
        EntityManager em = getEntityManager();
        em.remove(em.find(order.class,id));
    }

    public void updateOrder(order Order)
    {
        EntityManager em = getEntityManager();
        em.merge(Order);
    }

    public List<truck> getTrucks()
    {
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM trucks",truck.class);
        List<truck> trucks = q.getResultList();
        return trucks;
    }

    public List<order> getOrders()
    {
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM orders",order.class);
        List<order> orders = q.getResultList();
        return orders;
    }

    public truck findTruckByLicencePlate(String licencePlate)
    {
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM trucks WHERE licence_plate = ?",truck.class);
        q.setParameter(1,licencePlate);
        List<truck> trucks = q.getResultList();

        if(!trucks.isEmpty())
            return trucks.get(0);
        else
            return null;
    }

    public Driver findDriverByPassword(String password)
    {
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM drivers WHERE password = ?",Driver.class);
        q.setParameter(1,password);
        List<Driver> drivers = q.getResultList();

        if(!drivers.isEmpty())
            return drivers.get(0);
        else
            return null;
    }

    public order findOrderById(int id)
    {
        EntityManager em = getEntityManager();
        return em.find(order.class,id);
    }

}
