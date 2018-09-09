import java.util.List;

public class googleResponse {
    private String[] destination_addresses;
    private String[] origin_addresses;
    private row[] rows;
    private String status;

    public String[] getDestination_addresses(){
        return this.destination_addresses;
    }

    public void setDestination_addresses(String[] destination_addresses){
        this.destination_addresses=destination_addresses;
    }

    public String[] getOrigin_addresses(){
        return this.origin_addresses;
    }

    public void setOrigin_addresses(String[] origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public String getStatus(){
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public row[] getRows() {
        return rows;
    }

    public void setRows(row[] rows) {
        this.rows = rows;
    }
}
