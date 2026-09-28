import java.io.Serializable;

/**
 * Represents a single repair job created for a device.
 */
public class RepairJob implements Serializable {
    private static final long serialVersionUID = 1L;

    private int jobId;
    private int deviceId;
    private String dateReceived;
    private String diagnosis;
    private String status;
    private String technicianName;
    private String serviceDescription;
    private String partsUsed;
    private double serviceCharge;

    public RepairJob(int jobId, int deviceId, String dateReceived, String diagnosis, String status,
                      String technicianName, String serviceDescription, String partsUsed, double serviceCharge) {
        this.jobId = jobId;
        this.deviceId = deviceId;
        this.dateReceived = dateReceived;
        this.diagnosis = diagnosis;
        this.status = status;
        this.technicianName = technicianName;
        this.serviceDescription = serviceDescription;
        this.partsUsed = partsUsed;
        this.serviceCharge = serviceCharge;
    }

    public int getJobId() { return jobId; }

    public int getDeviceId() { return deviceId; }
    public void setDeviceId(int deviceId) { this.deviceId = deviceId; }

    public String getDateReceived() { return dateReceived; }
    public void setDateReceived(String dateReceived) { this.dateReceived = dateReceived; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }

    public String getServiceDescription() { return serviceDescription; }
    public void setServiceDescription(String serviceDescription) { this.serviceDescription = serviceDescription; }

    public String getPartsUsed() { return partsUsed; }
    public void setPartsUsed(String partsUsed) { this.partsUsed = partsUsed; }

    public double getServiceCharge() { return serviceCharge; }
    public void setServiceCharge(double serviceCharge) { this.serviceCharge = serviceCharge; }

    @Override
    public String toString() {
        return jobId + " - Device #" + deviceId + " - " + status + " - Rs." + serviceCharge;
    }
}
