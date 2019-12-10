import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class servletInit extends Application {
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(truckActions.class);
        classes.add(orderActions.class);
        classes.add(CorsFilter.class);
        classes.add(userActions.class);
        return classes;
    }

    public Set<Object> getSingletons() {
        // nothing to do, no singletons
        return null;
    }

}

