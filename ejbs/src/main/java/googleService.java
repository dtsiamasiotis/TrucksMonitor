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
        ResteasyWebTarget target = client.target("http://localhost:4000/geocode");//"https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+originsCoordinates+"&destinations=Kountouriotou 253,Pireas&departure_time=now&key=AIzaSyDW7z9-4rKR6W5kXTQ1AjiKB_2JSNsl3ko");

        Response response = target.request().get();
        //Read output in string format
        String responseStr = response.readEntity(String.class);


        System.out.println(responseStr);
        response.close();
        return responseStr;
    }

    public void testUnmarshall()  throws Exception
    {
        String googleJson = sendGeocodeRequest("38.006726,23.863777");
        //String googleJson = "{\"destination_addresses\": [\"Kountouriotou 253, Pireas 185 36, Greece\"],\"origin_addresses\": [\"Patriarchou Grigoriou E 5, Ag. Paraskevi 153 41, Greece\"],\"rows\": [{\"elements\": [{\"distance\": {\"text\": \"20.8 mi\",\"value\": 33486},\"duration\": {\"text\": \"33 mins\",\"value\": 1974},\"status\": \"OK\"}],}],\"status\": \"OK\"}";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        googleResponseGeocode res = gson.fromJson(googleJson,googleResponseGeocode.class);
        return;

    }
}
