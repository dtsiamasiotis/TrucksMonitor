import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.*;


@ServerEndpoint("/actions")
public class webSocketServer {

    @Inject
    private backendManager backendManager;

    @OnOpen
    public void handleOpen(Session session)
    {

    }
    @OnMessage
    public void handleMessage(Session session, String message)
    {
        if(message.contains("connectTruck"))
            backendManager.connectFromTruck(session,message);
        if(message.contains("test address"))
            System.out.println(message);
    }
}
