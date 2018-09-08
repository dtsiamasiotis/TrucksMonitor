import javax.persistence.*;

@Entity
@Table(name="orders")
public class order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int quantity;
    private String status;

    public order(){}

    public int getQuantity(){return quantity;}
    public void setQuantity(int quantity){this.quantity=quantity;}
    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}
}
