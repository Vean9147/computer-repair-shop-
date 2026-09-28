import java.awt.*;
import java.awt.event.*;

/**
 * Panel for generating and updating bills for repair jobs.
 */
public class BillingPanel extends Panel {

    private final RepairShopManager manager;
    private final MainFrame mainFrame;

    private Choice jobChoice = new Choice();
    private Label serviceChargeValue = new Label("0.0");
    private TextField additionalChargeField = new TextField("0.0", 10);
    private Label totalValue = new Label("0.0");
    private Choice paymentStatusChoice = new Choice();

    private java.awt.List billList = new java.awt.List(10, false);

    public BillingPanel(RepairShopManager manager, MainFrame mainFrame) {
        this.manager = manager;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));

        for (String s : Constants.PAYMENT_STATUSES) paymentStatusChoice.add(s);

        add(buildTopBar(), BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(5, 2, 8, 8));
        form.add(new Label("Repair Job:"));
        form.add(jobChoice);
        form.add(new Label("Service Charge (Rs.):"));
        form.add(serviceChargeValue);
        form.add(new Label("Additional Charge (Rs.):"));
        form.add(additionalChargeField);
        form.add(new Label("Total Amount (Rs.):"));
        form.add(totalValue);
        form.add(new Label("Payment Status:"));
        form.add(paymentStatusChoice);

        Panel formWrapper = new Panel(new BorderLayout());
        formWrapper.add(form, BorderLayout.NORTH);
        formWrapper.add(buildButtonBar(), BorderLayout.SOUTH);
        add(formWrapper, BorderLayout.WEST);

        Panel listWrapper = new Panel(new BorderLayout());
        listWrapper.add(new Label("Generated Bills"), BorderLayout.NORTH);
        listWrapper.add(billList, BorderLayout.CENTER);
        add(listWrapper, BorderLayout.CENTER);

        jobChoice.addItemListener(e -> loadJobDetails());
        additionalChargeField.addTextListener(e -> recomputeTotal());

        refreshList();
    }

    private Panel buildTopBar() {
        Panel bar = new Panel(new BorderLayout());
        Label heading = new Label("Billing", Label.LEFT);
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        bar.add(heading, BorderLayout.WEST);
        Button back = new Button("Back to Dashboard");
        back.addActionListener(e -> mainFrame.showCard("DASHBOARD"));
        bar.add(back, BorderLayout.EAST);
        return bar;
    }

    private Panel buildButtonBar() {
        Panel bar = new Panel(new FlowLayout(FlowLayout.LEFT));
        Button generateBtn = new Button("Generate / Update Bill");
        generateBtn.addActionListener(e -> generateBill());
        bar.add(generateBtn);
        return bar;
    }

    private Integer getSelectedJobId() {
        if (jobChoice.getItemCount() == 0) return null;
        try {
            return Integer.parseInt(jobChoice.getSelectedItem().split(" - ")[0].trim());
        } catch (Exception ex) {
            return null;
        }
    }

    private void loadJobDetails() {
        Integer jobId = getSelectedJobId();
        if (jobId == null) return;
        RepairJob job = manager.findJobById(jobId);
        if (job == null) return;
        serviceChargeValue.setText(String.valueOf(job.getServiceCharge()));

        Billing existing = manager.findBillingByJob(jobId);
        if (existing != null) {
            additionalChargeField.setText(String.valueOf(existing.getAdditionalCharge()));
            selectChoiceItem(paymentStatusChoice, existing.getPaymentStatus());
        } else {
            additionalChargeField.setText("0.0");
            paymentStatusChoice.select(0);
        }
        recomputeTotal();
    }

    private void recomputeTotal() {
        try {
            double service = Double.parseDouble(serviceChargeValue.getText().trim());
            double additional = additionalChargeField.getText().trim().isEmpty()
                    ? 0.0 : Double.parseDouble(additionalChargeField.getText().trim());
            totalValue.setText(String.valueOf(service + additional));
        } catch (NumberFormatException ex) {
            totalValue.setText("--");
        }
    }

    private void generateBill() {
        Integer jobId = getSelectedJobId();
        if (jobId == null) {
            mainFrame.showMessage("Create a repair job first, then bill it here.");
            return;
        }
        RepairJob job = manager.findJobById(jobId);
        double additional;
        try {
            additional = additionalChargeField.getText().trim().isEmpty()
                    ? 0.0 : Double.parseDouble(additionalChargeField.getText().trim());
        } catch (NumberFormatException ex) {
            mainFrame.showMessage("Enter a valid numeric additional charge.");
            return;
        }
        double total = job.getServiceCharge() + additional;
        String paymentStatus = paymentStatusChoice.getSelectedItem();

        Billing existing = manager.findBillingByJob(jobId);
        if (existing != null) {
            manager.updateBilling(existing.getBillId(), additional, paymentStatus);
            mainFrame.showMessage("Bill #" + existing.getBillId() + " updated. Total: Rs." + total);
        } else {
            int billId = manager.nextBillId();
            manager.addBilling(new Billing(billId, jobId, job.getServiceCharge(), additional, total, paymentStatus));
            mainFrame.showMessage("Bill #" + billId + " generated. Total: Rs." + total);
        }
        refreshList();
        recomputeTotal();
    }

    private void selectChoiceItem(Choice choice, String value) {
        for (int i = 0; i < choice.getItemCount(); i++) {
            if (choice.getItem(i).equals(value)) {
                choice.select(i);
                return;
            }
        }
    }

    /** Rebuilds the job dropdown and the bill list from current data. Call before showing this panel. */
    public void refreshList() {
        String previouslySelected = jobChoice.getItemCount() > 0 ? jobChoice.getSelectedItem() : null;
        jobChoice.removeAll();
        for (RepairJob j : manager.getAllJobs()) {
            jobChoice.add(j.toString());
        }
        if (previouslySelected != null) selectChoiceItem(jobChoice, previouslySelected);
        if (jobChoice.getItemCount() > 0 && previouslySelected == null) loadJobDetails();

        billList.removeAll();
        for (Billing b : manager.getAllBills()) {
            billList.add(b.toString());
        }
    }
}
