import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UserService {
    @PersistenceContext
    private EntityManager entityManager;

    public User findUserByNameAndPassword(String username, String passwordInMD5)
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM users WHERE username=? AND password=?",User.class);
        q.setParameter(1,username);
        q.setParameter(2,passwordInMD5);
        User foundUser = null;
        try{
            foundUser = (User)q.getSingleResult();
        } catch (javax.persistence.NoResultException nre){}

        return foundUser;
    }
}
