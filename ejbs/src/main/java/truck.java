import javax.ejb.Stateless;
import javax.persistence.*;


@Entity
@Table(name="trucks")
public class truck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String licence_plate;
    private String status;
    private int current_order_id;

    public truck(){}

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
}