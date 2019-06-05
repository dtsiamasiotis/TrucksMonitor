import lombok.Getter;
import lombok.Setter;

import java.util.Map;


public class plus_code {
    private Map<String,String> plus_code;

    public void setPlus_code(Map<String,String> plus_code)
    {
        this.plus_code = plus_code;
    }

    public Map<String,String> getPlus_code()
    {
        return this.plus_code;
    }
}
