import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class geocodeResult {
    private address_component[] address_components;
    private String formatted_address;
    private geometry geometry;
    private String place_id;
    private Map<String,String> plus_code;
    private String[] types;

}
