import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class usersDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void saveUser(User user)
    {
        entityManager.persist(user);
    }
}
