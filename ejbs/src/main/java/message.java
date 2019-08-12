import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class message {
    private String operation;
    private String coordinates;
    private messageOrder order;
}
