import java.io.Serializable;

/**
 * Represents the final bill generated for a repair job.
 */
public class Billing implements Serializable {
    private static final long serialVersionUID = 1L;

    private int billId;
    private int jobId;
    private double serviceCharge;
    private double additionalCharge;
    private double totalAmount;
    private String paymentStatus;

    public Billing(int billId, int jobId, double serviceCharge, double additionalCharge,
                    double totalAmount, String paymentStatus) {
        this.billId = billId;
        this.jobId = jobId;
        this.serviceCharge = serviceCharge;
        this.additionalCharge = additionalCharge;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    public int getBillId() { return billId; }

    public int getJobId() { return jobId; }

    public double getServiceCharge() { return serviceCharge; }
    public void setServiceCharge(double serviceCharge) { this.serviceCharge = serviceCharge; }

    public double getAdditionalCharge() { return additionalCharge; }
    public void setAdditionalCharge(double additionalCharge) { this.additionalCharge = additionalCharge; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    @Override
    public String toString() {
        return billId + " - Job #" + jobId + " - Total Rs." + totalAmount + " - " + paymentStatus;
    }
}
