import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
public class order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int quantity;
    private String status;
    private String address;
    @Transient
    private List<truck> candidateTrucks = new ArrayList<truck>();

    public order(){}

    public int getQuantity(){return quantity;}
    public void setQuantity(int quantity){this.quantity=quantity;}
    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}
    public int getId() {return id;}
    public void setAddress(String address){this.address=address;}
    public String getAddress(){return address;}
    public void addToCandidateTrucks(truck newTruck)
    {
        candidateTrucks.add(newTruck);
    }
    public List<truck> getCandidateTrucks(){return candidateTrucks;}
}
