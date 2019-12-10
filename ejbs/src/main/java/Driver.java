import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="drivers")
@Getter
@Setter
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
}
