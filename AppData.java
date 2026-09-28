import java.io.Serializable;
import java.util.ArrayList;

/**
 * A single serializable object that bundles all application records together
 * so the whole data set can be saved to, and loaded from, one file.
 */
public class AppData implements Serializable {
    private static final long serialVersionUID = 1L;

    public ArrayList<Customer> customers = new ArrayList<>();
    public ArrayList<Device> devices = new ArrayList<>();
    public ArrayList<RepairJob> jobs = new ArrayList<>();
    public ArrayList<Billing> bills = new ArrayList<>();
}
