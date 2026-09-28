import java.awt.*;
import java.awt.event.*;

/**
 * Panel for adding, updating, deleting, and browsing device records.
 */
public class DevicePanel extends Panel {

    private final RepairShopManager manager;
    private final MainFrame mainFrame;

    private Choice customerChoice = new Choice();
    private Choice typeChoice = new Choice();
    private TextField brandField = new TextField(18);
    private TextField modelField = new TextField(18);
    private TextField serialField = new TextField(18);
    private TextArea problemArea = new TextArea(3, 20);

    private java.awt.List deviceList = new java.awt.List(10, false);
    private int selectedId = -1;

    public DevicePanel(RepairShopManager manager, MainFrame mainFrame) {
        this.manager = manager;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));

        for (String t : Constants.DEVICE_TYPES) typeChoice.add(t);

        add(buildTopBar(), BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(6, 2, 8, 8));
        form.add(new Label("Customer:"));
        form.add(customerChoice);
        form.add(new Label("Device Type:"));
        form.add(typeChoice);
        form.add(new Label("Brand:"));
        form.add(brandField);
        form.add(new Label("Model:"));
        form.add(modelField);
        form.add(new Label("Serial Number:"));
        form.add(serialField);
        form.add(new Label("Reported Problem:"));
        form.add(problemArea);

        Panel formWrapper = new Panel(new BorderLayout());
        formWrapper.add(form, BorderLayout.NORTH);
        formWrapper.add(buildButtonBar(), BorderLayout.SOUTH);
        add(formWrapper, BorderLayout.WEST);

        Panel listWrapper = new Panel(new BorderLayout());
        listWrapper.add(new Label("Existing Devices (select to edit)"), BorderLayout.NORTH);
        listWrapper.add(deviceList, BorderLayout.CENTER);
        add(listWrapper, BorderLayout.CENTER);

        deviceList.addItemListener(e -> loadSelectedDevice());

        refreshList();
    }

    private Panel buildTopBar() {
        Panel bar = new Panel(new BorderLayout());
        Label heading = new Label("Device Management", Label.LEFT);
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        bar.add(heading, BorderLayout.WEST);
        Button back = new Button("Back to Dashboard");
        back.addActionListener(e -> mainFrame.showCard("DASHBOARD"));
        bar.add(back, BorderLayout.EAST);
        return bar;
    }

    private Panel buildButtonBar() {
        Panel bar = new Panel(new FlowLayout(FlowLayout.LEFT));

        Button addBtn = new Button("Add");
        addBtn.addActionListener(e -> addDevice());

        Button updateBtn = new Button("Update");
        updateBtn.addActionListener(e -> updateDevice());

        Button deleteBtn = new Button("Delete");
        deleteBtn.addActionListener(e -> deleteDevice());

        Button clearBtn = new Button("Clear");
        clearBtn.addActionListener(e -> clearForm());

        bar.add(addBtn);
        bar.add(updateBtn);
        bar.add(deleteBtn);
        bar.add(clearBtn);
        return bar;
    }

    private Integer getSelectedCustomerId() {
        if (customerChoice.getItemCount() == 0) return null;
        String item = customerChoice.getSelectedItem();
        // Choice items are formatted as "id - name - contact"
        try {
            return Integer.parseInt(item.split(" - ")[0].trim());
        } catch (Exception ex) {
            return null;
        }
    }

    private void addDevice() {
        Integer custId = getSelectedCustomerId();
        if (custId == null) {
            mainFrame.showMessage("Add a customer first, then register their device.");
            return;
        }
        if (!validateForm()) return;
        int id = manager.nextDeviceId();
        manager.addDevice(new Device(id, custId, typeChoice.getSelectedItem(), brandField.getText().trim(),
                modelField.getText().trim(), serialField.getText().trim(), problemArea.getText().trim()));
        mainFrame.showMessage("Device added with ID " + id);
        clearForm();
        refreshList();
    }

    private void updateDevice() {
        if (selectedId == -1) {
            mainFrame.showMessage("Select a device from the list first.");
            return;
        }
        Integer custId = getSelectedCustomerId();
        if (custId == null || !validateForm()) return;
        manager.updateDevice(selectedId, custId, typeChoice.getSelectedItem(), brandField.getText().trim(),
                modelField.getText().trim(), serialField.getText().trim(), problemArea.getText().trim());
        mainFrame.showMessage("Device #" + selectedId + " updated.");
        clearForm();
        refreshList();
    }

    private void deleteDevice() {
        if (selectedId == -1) {
            mainFrame.showMessage("Select a device from the list first.");
            return;
        }
        manager.deleteDevice(selectedId);
        mainFrame.showMessage("Device #" + selectedId + " deleted.");
        clearForm();
        refreshList();
    }

    private void loadSelectedDevice() {
        int idx = deviceList.getSelectedIndex();
        if (idx < 0) return;
        Device d = manager.getAllDevices().get(idx);
        selectedId = d.getDeviceId();
        selectChoiceItemStartingWith(customerChoice, d.getCustomerId() + " -");
        selectChoiceItem(typeChoice, d.getDeviceType());
        brandField.setText(d.getBrand());
        modelField.setText(d.getModel());
        serialField.setText(d.getSerialNumber());
        problemArea.setText(d.getReportedProblem());
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

    private boolean validateForm() {
        if (brandField.getText().trim().isEmpty() || modelField.getText().trim().isEmpty()) {
            mainFrame.showMessage("Brand and Model are required.");
            return false;
        }
        return true;
    }

    private void clearForm() {
        selectedId = -1;
        brandField.setText("");
        modelField.setText("");
        serialField.setText("");
        problemArea.setText("");
        if (deviceList.getSelectedIndex() >= 0) deviceList.deselect(deviceList.getSelectedIndex());
    }

    /** Rebuilds the customer dropdown and the device list from current data. Call before showing this panel. */
    public void refreshList() {
        String previouslySelected = customerChoice.getItemCount() > 0 ? customerChoice.getSelectedItem() : null;
        customerChoice.removeAll();
        for (Customer c : manager.getAllCustomers()) {
            customerChoice.add(c.toString());
        }
        if (previouslySelected != null) selectChoiceItem(customerChoice, previouslySelected);

        deviceList.removeAll();
        for (Device d : manager.getAllDevices()) {
            deviceList.add(d.toString());
        }
    }
}
