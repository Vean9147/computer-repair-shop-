import java.io.Serializable;

/**
 * Represents a device (computer, laptop, printer, etc.) brought in for repair.
 * Each device belongs to exactly one customer.
 */
public class Device implements Serializable {
    private static final long serialVersionUID = 1L;

    private int deviceId;
    private int customerId;
    private String deviceType;
    private String brand;
    private String model;
    private String serialNumber;
    private String reportedProblem;

    public Device(int deviceId, int customerId, String deviceType, String brand,
                   String model, String serialNumber, String reportedProblem) {
        this.deviceId = deviceId;
        this.customerId = customerId;
        this.deviceType = deviceType;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.reportedProblem = reportedProblem;
    }

    public int getDeviceId() { return deviceId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getReportedProblem() { return reportedProblem; }
    public void setReportedProblem(String reportedProblem) { this.reportedProblem = reportedProblem; }

    @Override
    public String toString() {
        return deviceId + " - " + deviceType + " " + brand + " " + model + " (Cust #" + customerId + ")";
    }
}
