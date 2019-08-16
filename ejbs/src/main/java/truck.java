import javax.ejb.Stateless;
import javax.persistence.*;
import javax.websocket.Session;


@Entity
@Table(name="trucks")
public class truck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String licence_plate;
    private String status;
    private int current_order_id;
    private String password;
    @Transient
    private Session session;
    @Transient
    private String lat;
    @Transient
    private String lng;

    public truck(){}

    public int getId() { return id; }

    public void setLicenceplate(String licence_plate) {
        this.licence_plate = licence_plate;
    }
    public String getLicenceplate(){return licence_plate;}

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus(){return status;}

    public void setCurrentorderid(int current_order_id) {
        this.current_order_id = current_order_id;
    }
    public int getCurrenorderid(){return current_order_id;}

    public void setSession(Session session){this.session=session;}
    public Session getSession(){return session;}

    public void setLat(String lat){this.lat = lat;}
    public String getLat(){return this.lat;}

    public void setLng(String lng){this.lng = lng;}
    public String getLng(){return this.lng;}

    public void setPassword(String password){this.password = password;}
    public String getPassword(){return password;}

}
