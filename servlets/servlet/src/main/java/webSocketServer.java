import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;



@ServerEndpoint("/actions")
public class webSocketServer {

    @Inject
    private backendManager bcManager;

    @OnOpen
    public void handleOpen(Session session)
    {

    }
    @OnMessage
    public void handleMessage(Session session, String message)
    {
        if(message.contains("connectTruck"))
            bcManager.connectFromTruck(session,message);
        else if(message.contains("coordinates"))
            bcManager.handleResponseForOrder(session,message);
        else
            try {
                bcManager.testUnmarshall();
            }catch(Exception e){}

    }
}
