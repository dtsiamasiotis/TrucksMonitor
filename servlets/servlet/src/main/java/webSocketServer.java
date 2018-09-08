import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.*;


@ServerEndpoint("/actions")
public class webSocketServer {

    @OnOpen
    public void handleOpen(Session session)
    {

    }
    @OnMessage
    public void handleMessage(String message)
    {
        System.out.println(message);
    }
}
