import java.awt.*;
import java.awt.event.*;

/**
 * Panel for creating repair jobs, updating diagnosis/status/service info,
 * and browsing existing jobs.
 */
public class RepairJobPanel extends Panel {

    private final RepairShopManager manager;
    private final MainFrame mainFrame;

    private Choice deviceChoice = new Choice();
    private TextField dateField = new TextField(12);
    private TextArea diagnosisArea = new TextArea(2, 20);
    private Choice statusChoice = new Choice();
    private TextField technicianField = new TextField(18);
    private TextArea serviceDescArea = new TextArea(2, 20);
    private TextField partsField = new TextField(18);
    private TextField chargeField = new TextField(10);

    private java.awt.List jobList = new java.awt.List(10, false);
    private int selectedId = -1;

    public RepairJobPanel(RepairShopManager manager, MainFrame mainFrame) {
        this.manager = manager;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));

        for (String s : Constants.JOB_STATUSES) statusChoice.add(s);

        add(buildTopBar(), BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(8, 2, 6, 6));
        form.add(new Label("Device:"));
        form.add(deviceChoice);
        form.add(new Label("Date Received:"));
        form.add(dateField);
        form.add(new Label("Diagnosis:"));
        form.add(diagnosisArea);
        form.add(new Label("Status:"));
        form.add(statusChoice);
        form.add(new Label("Technician:"));
        form.add(technicianField);
        form.add(new Label("Service Description:"));
        form.add(serviceDescArea);
        form.add(new Label("Parts Used:"));
        form.add(partsField);
        form.add(new Label("Service Charge (Rs.):"));
        form.add(chargeField);

        Panel formWrapper = new Panel(new BorderLayout());
        formWrapper.add(form, BorderLayout.NORTH);
        formWrapper.add(buildButtonBar(), BorderLayout.SOUTH);
        add(formWrapper, BorderLayout.WEST);

        Panel listWrapper = new Panel(new BorderLayout());
        listWrapper.add(new Label("Existing Repair Jobs (select to edit)"), BorderLayout.NORTH);
        listWrapper.add(jobList, BorderLayout.CENTER);
        add(listWrapper, BorderLayout.CENTER);

        jobList.addItemListener(e -> loadSelectedJob());

        refreshList();
    }

    private Panel buildTopBar() {
        Panel bar = new Panel(new BorderLayout());
        Label heading = new Label("Repair Job Management", Label.LEFT);
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        bar.add(heading, BorderLayout.WEST);
        Button back = new Button("Back to Dashboard");
        back.addActionListener(e -> mainFrame.showCard("DASHBOARD"));
        bar.add(back, BorderLayout.EAST);
        return bar;
    }

    private Panel buildButtonBar() {
        Panel bar = new Panel(new FlowLayout(FlowLayout.LEFT));

        Button addBtn = new Button("Create Job");
        addBtn.addActionListener(e -> addJob());

        Button updateBtn = new Button("Update");
        updateBtn.addActionListener(e -> updateJob());

        Button deleteBtn = new Button("Delete");
        deleteBtn.addActionListener(e -> deleteJob());

        Button clearBtn = new Button("Clear");
        clearBtn.addActionListener(e -> clearForm());

        bar.add(addBtn);
        bar.add(updateBtn);
        bar.add(deleteBtn);
        bar.add(clearBtn);
        return bar;
    }

    private Integer getSelectedDeviceId() {
        if (deviceChoice.getItemCount() == 0) return null;
        try {
            return Integer.parseInt(deviceChoice.getSelectedItem().split(" - ")[0].trim());
        } catch (Exception ex) {
            return null;
        }
    }

    private Double parseCharge() {
        try {
            return Double.parseDouble(chargeField.getText().trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void addJob() {
        Integer deviceId = getSelectedDeviceId();
        if (deviceId == null) {
            mainFrame.showMessage("Register a device first, then create a repair job for it.");
            return;
        }
        Double charge = parseCharge();
        if (charge == null) {
            mainFrame.showMessage("Enter a valid numeric service charge.");
            return;
        }
        if (dateField.getText().trim().isEmpty()) {
            mainFrame.showMessage("Enter the date received (e.g. 26-09-2026).");
            return;
        }
        int id = manager.nextJobId();
        manager.addJob(new RepairJob(id, deviceId, dateField.getText().trim(), diagnosisArea.getText().trim(),
                statusChoice.getSelectedItem(), technicianField.getText().trim(),
                serviceDescArea.getText().trim(), partsField.getText().trim(), charge));
        mainFrame.showMessage("Repair job created with ID " + id);
        clearForm();
        refreshList();
    }

    private void updateJob() {
        if (selectedId == -1) {
            mainFrame.showMessage("Select a job from the list first.");
            return;
        }
        Double charge = parseCharge();
        if (charge == null) {
            mainFrame.showMessage("Enter a valid numeric service charge.");
            return;
        }
        manager.updateJob(selectedId, diagnosisArea.getText().trim(), statusChoice.getSelectedItem(),
                technicianField.getText().trim(), serviceDescArea.getText().trim(),
                partsField.getText().trim(), charge);
        mainFrame.showMessage("Repair job #" + selectedId + " updated.");
        clearForm();
        refreshList();
    }

    private void deleteJob() {
        if (selectedId == -1) {
            mainFrame.showMessage("Select a job from the list first.");
            return;
        }
        manager.deleteJob(selectedId);
        mainFrame.showMessage("Repair job #" + selectedId + " deleted.");
        clearForm();
        refreshList();
    }

    private void loadSelectedJob() {
        int idx = jobList.getSelectedIndex();
        if (idx < 0) return;
        RepairJob j = manager.getAllJobs().get(idx);
        selectedId = j.getJobId();
        selectChoiceItemStartingWith(deviceChoice, j.getDeviceId() + " -");
        dateField.setText(j.getDateReceived());
        diagnosisArea.setText(j.getDiagnosis());
        selectChoiceItem(statusChoice, j.getStatus());
        technicianField.setText(j.getTechnicianName());
        serviceDescArea.setText(j.getServiceDescription());
        partsField.setText(j.getPartsUsed());
        chargeField.setText(String.valueOf(j.getServiceCharge()));
    }

    private void selectChoiceItemStartingWith(Choice choice, String prefix) {
        for (int i = 0; i < choice.getItemCount(); i++) {
            if (choice.getItem(i).startsWith(prefix)) {
                choice.select(i);
                return;
            }
        }
    }

    private void selectChoiceItem(Choice choice, String value) {
        for (int i = 0; i < choice.getItemCount(); i++) {
            if (choice.getItem(i).equals(value)) {
                choice.select(i);
                return;
            }
        }
    }

    private void clearForm() {
        selectedId = -1;
        dateField.setText("");
        diagnosisArea.setText("");
        technicianField.setText("");
        serviceDescArea.setText("");
        partsField.setText("");
        chargeField.setText("");
        if (jobList.getSelectedIndex() >= 0) jobList.deselect(jobList.getSelectedIndex());
    }

    /** Rebuilds the device dropdown and the job list from current data. Call before showing this panel. */
    public void refreshList() {
        String previouslySelected = deviceChoice.getItemCount() > 0 ? deviceChoice.getSelectedItem() : null;
        deviceChoice.removeAll();
        for (Device d : manager.getAllDevices()) {
            deviceChoice.add(d.toString());
        }
        if (previouslySelected != null) selectChoiceItem(deviceChoice, previouslySelected);

        jobList.removeAll();
        for (RepairJob j : manager.getAllJobs()) {
            jobList.add(j.toString());
        }
    }
}
