import java.util.Map;

public class elements {
    private Map<String,String> distance;
    private Map<String,String> duration;
    private String status;

    public Map<String,String> getDistance(){
        return this.distance;
    }

    public void setDistance(Map<String,String> distance){
        this.distance=distance;
    }

    public Map<String,String> getDuration(){
        return this.duration;
    }

    public void setDuration(Map<String,String> duration){
        this.duration=duration;
    }

    public String getStatus(){
        return this.status;
    }

    public void setStatus(String status){
        this.status=status;
    }
}
