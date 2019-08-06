import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class messageEncoder implements Encoder.Text<message>{

    private static Gson gson = new Gson();

    @Override
    public String encode(message message) throws EncodeException{
        return gson.toJson(message);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}