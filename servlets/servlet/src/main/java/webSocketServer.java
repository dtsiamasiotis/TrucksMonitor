import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;



@ServerEndpoint(value="/actions",decoders = messageDecoder.class,encoders = messageEncoder.class)
public class webSocketServer {

    @Inject
    private backendManager bcManager;

    @OnOpen
    public void handleOpen(Session session)
    {
        System.out.println(session.getId());
        bcManager.connectFromTruck(session);
    }

    @OnMessage
    public void handleMessage(Session session, String message)
    {

        bcManager.handleResponseForOrder(session,message);

    }
}
