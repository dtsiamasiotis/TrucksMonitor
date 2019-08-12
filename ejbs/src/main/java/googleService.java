import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

@Stateless
public class googleService {

    public String sendGeocodeRequest(String address)
    {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:4000/geocode");//"https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+originsCoordinates+"&destinations="+destinationAddress+"&departure_time=now&key=");
        Response response = target.request().get();
        String responseStr = response.readEntity(String.class);
        response.close();

        return responseStr;
    }

    public String sendDistanceRequest(String originCoordinates,String destinationAddress)
    {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+originCoordinates+"&destinations="+destinationAddress+"&departure_time=now&key=");
        //ResteasyWebTarget target = client.target("http://localhost:4000/geocode");
        Response response = target.request().get();
        String responseStr = response.readEntity(String.class);
        response.close();

        return responseStr;
    }

    public googleResponseGeocode unMarshallGoogleRespGeocode(String googleResponseJson)  throws Exception
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        googleResponseGeocode res = gson.fromJson(googleResponseJson,googleResponseGeocode.class);
        return res;

    }

    public googleResponseDistance unMarshallGoogleRespDistance(String googleResponseJson)  throws Exception
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        googleResponseDistance res = gson.fromJson(googleResponseJson,googleResponseDistance.class);
        return res;

    }
}
