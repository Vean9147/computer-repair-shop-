import java.io.IOException;
import java.util.ArrayList;

/**
 * Central manager class that holds the application's data in memory and
 * provides add/update/delete/search operations for every entity, plus
 * simple wrappers around FileHandler for persistence.
 */
public class RepairShopManager {

    private AppData data;

    public RepairShopManager() {
        try {
            data = FileHandler.load(Constants.DATA_FILE);
        } catch (Exception e) {
            // Corrupt or unreadable file - start fresh rather than crash the app.
            data = new AppData();
        }
    }

    public void save() {
        try {
            FileHandler.save(data, Constants.DATA_FILE);
        } catch (IOException e) {
            System.err.println("Could not save data: " + e.getMessage());
        }
    }

    // ---------- ID GENERATORS ----------

    public int nextCustomerId() {
        int max = 0;
        for (Customer c : data.customers) max = Math.max(max, c.getCustomerId());
        return max + 1;
    }

    public int nextDeviceId() {
        int max = 0;
        for (Device d : data.devices) max = Math.max(max, d.getDeviceId());
        return max + 1;
    }

    public int nextJobId() {
        int max = 0;
        for (RepairJob j : data.jobs) max = Math.max(max, j.getJobId());
        return max + 1;
    }

    public int nextBillId() {
        int max = 0;
        for (Billing b : data.bills) max = Math.max(max, b.getBillId());
        return max + 1;
    }

    // ---------- CUSTOMER OPERATIONS ----------

    public void addCustomer(Customer c) { data.customers.add(c); save(); }

    public boolean updateCustomer(int id, String name, String contact, String email, String address) {
        Customer c = findCustomerById(id);
        if (c == null) return false;
        c.setName(name);
        c.setContactNumber(contact);
        c.setEmail(email);
        c.setAddress(address);
        save();
        return true;
    }

    public boolean deleteCustomer(int id) {
        boolean removed = data.customers.removeIf(c -> c.getCustomerId() == id);
        if (removed) save();
        return removed;
    }

    public Customer findCustomerById(int id) {
        for (Customer c : data.customers) if (c.getCustomerId() == id) return c;
        return null;
    }

    public ArrayList<Customer> searchCustomersByName(String keyword) {
        ArrayList<Customer> result = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (Customer c : data.customers) {
            if (c.getName().toLowerCase().contains(lower)) result.add(c);
        }
        return result;
    }

    public ArrayList<Customer> getAllCustomers() { return data.customers; }

    // ---------- DEVICE OPERATIONS ----------

    public void addDevice(Device d) { data.devices.add(d); save(); }

    public boolean updateDevice(int id, int customerId, String type, String brand, String model,
                                 String serial, String problem) {
        Device d = findDeviceById(id);
        if (d == null) return false;
        d.setCustomerId(customerId);
        d.setDeviceType(type);
        d.setBrand(brand);
        d.setModel(model);
        d.setSerialNumber(serial);
        d.setReportedProblem(problem);
        save();
        return true;
    }

    public boolean deleteDevice(int id) {
        boolean removed = data.devices.removeIf(d -> d.getDeviceId() == id);
        if (removed) save();
        return removed;
    }

    public Device findDeviceById(int id) {
        for (Device d : data.devices) if (d.getDeviceId() == id) return d;
        return null;
    }

    public Device findDeviceBySerial(String serial) {
        for (Device d : data.devices) {
            if (d.getSerialNumber() != null && d.getSerialNumber().equalsIgnoreCase(serial)) return d;
        }
        return null;
    }

    public ArrayList<Device> getDevicesByCustomer(int customerId) {
        ArrayList<Device> result = new ArrayList<>();
        for (Device d : data.devices) if (d.getCustomerId() == customerId) result.add(d);
        return result;
    }

    public ArrayList<Device> getAllDevices() { return data.devices; }

    // ---------- REPAIR JOB OPERATIONS ----------

    public void addJob(RepairJob j) { data.jobs.add(j); save(); }

    public boolean updateJob(int id, String diagnosis, String status, String technician,
                              String serviceDescription, String partsUsed, double serviceCharge) {
        RepairJob j = findJobById(id);
        if (j == null) return false;
        j.setDiagnosis(diagnosis);
        j.setStatus(status);
        j.setTechnicianName(technician);
        j.setServiceDescription(serviceDescription);
        j.setPartsUsed(partsUsed);
        j.setServiceCharge(serviceCharge);
        save();
        return true;
    }

    public boolean deleteJob(int id) {
        boolean removed = data.jobs.removeIf(j -> j.getJobId() == id);
        if (removed) save();
        return removed;
    }

    public RepairJob findJobById(int id) {
        for (RepairJob j : data.jobs) if (j.getJobId() == id) return j;
        return null;
    }

    public ArrayList<RepairJob> getJobsByStatus(String status) {
        ArrayList<RepairJob> result = new ArrayList<>();
        for (RepairJob j : data.jobs) if (j.getStatus().equalsIgnoreCase(status)) result.add(j);
        return result;
    }

    public ArrayList<RepairJob> getAllJobs() { return data.jobs; }

    // ---------- BILLING OPERATIONS ----------

    public void addBilling(Billing b) { data.bills.add(b); save(); }

    public boolean updateBilling(int billId, double additionalCharge, String paymentStatus) {
        Billing b = findBillingById(billId);
        if (b == null) return false;
        b.setAdditionalCharge(additionalCharge);
        b.setTotalAmount(b.getServiceCharge() + additionalCharge);
        b.setPaymentStatus(paymentStatus);
        save();
        return true;
    }

    public Billing findBillingById(int id) {
        for (Billing b : data.bills) if (b.getBillId() == id) return b;
        return null;
    }

    public Billing findBillingByJob(int jobId) {
        for (Billing b : data.bills) if (b.getJobId() == jobId) return b;
        return null;
    }

    public ArrayList<Billing> getAllBills() { return data.bills; }
}
