import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Local
@Path("/userActions")
public class userActions {

    @EJB
    private UserService userService;

    @POST
    @Path("/validateLogin")
    public Response validateLogin(User user)
    {
        User userFound = userService.findUserByNameAndPassword(user.getUsername(),user.getPassword());
        if(userFound==null)
            return Response.ok("{\"result\":\"wrong\"}").build();
        else
            return Response.ok("{\"result\":\"correct\"}").build();
    }
}
