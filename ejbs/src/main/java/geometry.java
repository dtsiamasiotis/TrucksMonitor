import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class geometry {
    private Map<String,String> location;
    private String location_type;
    private Map<String,Map<String,String>> viewport;
}
