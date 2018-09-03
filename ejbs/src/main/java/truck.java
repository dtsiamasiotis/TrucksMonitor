import javax.ejb.Stateless;

@Stateless
public class truck {
    private String licencePlate;
    private int kmTravelled;

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }
}
