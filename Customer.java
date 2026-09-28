import java.io.Serializable;

/**
 * Represents a customer of the repair shop.
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private int customerId;
    private String name;
    private String contactNumber;
    private String email;
    private String address;

    public Customer(int customerId, String name, String contactNumber, String email, String address) {
        this.customerId = customerId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    public int getCustomerId() { return customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    /** Used to render this customer inside a java.awt.List. */
    @Override
    public String toString() {
        return customerId + " - " + name + " - " + contactNumber;
    }
}
