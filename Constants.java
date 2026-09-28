/**
 * Shared constant values used across multiple GUI panels.
 */
public class Constants {

    public static final String[] JOB_STATUSES = {
        "Received", "Diagnosis", "Under Repair", "Awaiting Parts",
        "Ready for Collection", "Completed", "Cancelled"
    };

    public static final String[] DEVICE_TYPES = {
        "Desktop", "Laptop", "Printer", "Monitor", "Other"
    };

    public static final String[] PAYMENT_STATUSES = {
        "Unpaid", "Partially Paid", "Paid"
    };

    public static final String DATA_FILE = "repairshop_data.ser";
}
