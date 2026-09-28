import java.awt.*;
import java.awt.event.*;

/**
 * Panel for adding, updating, deleting, and browsing customer records.
 */
public class CustomerPanel extends Panel {

    private final RepairShopManager manager;
    private final MainFrame mainFrame;

    private TextField nameField = new TextField(20);
    private TextField contactField = new TextField(20);
    private TextField emailField = new TextField(20);
    private TextField addressField = new TextField(20);

    private java.awt.List customerList = new java.awt.List(10, false);
    private int selectedId = -1;

    public CustomerPanel(RepairShopManager manager, MainFrame mainFrame) {
        this.manager = manager;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));

        add(buildTopBar(), BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(4, 2, 8, 8));
        form.add(new Label("Name:"));
        form.add(nameField);
        form.add(new Label("Contact Number:"));
        form.add(contactField);
        form.add(new Label("Email:"));
        form.add(emailField);
        form.add(new Label("Address:"));
        form.add(addressField);

        Panel formWrapper = new Panel(new BorderLayout());
        formWrapper.add(form, BorderLayout.NORTH);
        formWrapper.add(buildButtonBar(), BorderLayout.SOUTH);

        add(formWrapper, BorderLayout.WEST);

        Panel listWrapper = new Panel(new BorderLayout());
        listWrapper.add(new Label("Existing Customers (select to edit)"), BorderLayout.NORTH);
        listWrapper.add(customerList, BorderLayout.CENTER);
        add(listWrapper, BorderLayout.CENTER);

        customerList.addItemListener(e -> loadSelectedCustomer());

        refreshList();
    }

    private Panel buildTopBar() {
        Panel bar = new Panel(new BorderLayout());
        Label heading = new Label("Customer Management", Label.LEFT);
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
        addBtn.addActionListener(e -> addCustomer());

        Button updateBtn = new Button("Update");
        updateBtn.addActionListener(e -> updateCustomer());

        Button deleteBtn = new Button("Delete");
        deleteBtn.addActionListener(e -> deleteCustomer());

        Button clearBtn = new Button("Clear");
        clearBtn.addActionListener(e -> clearForm());

        bar.add(addBtn);
        bar.add(updateBtn);
        bar.add(deleteBtn);
        bar.add(clearBtn);
        return bar;
    }

    private void addCustomer() {
        if (!validateForm()) return;
        int id = manager.nextCustomerId();
        manager.addCustomer(new Customer(id, nameField.getText().trim(), contactField.getText().trim(),
                emailField.getText().trim(), addressField.getText().trim()));
        mainFrame.showMessage("Customer added with ID " + id);
        clearForm();
        refreshList();
    }

    private void updateCustomer() {
        if (selectedId == -1) {
            mainFrame.showMessage("Select a customer from the list first.");
            return;
        }
        if (!validateForm()) return;
        manager.updateCustomer(selectedId, nameField.getText().trim(), contactField.getText().trim(),
                emailField.getText().trim(), addressField.getText().trim());
        mainFrame.showMessage("Customer #" + selectedId + " updated.");
        clearForm();
        refreshList();
    }

    private void deleteCustomer() {
        if (selectedId == -1) {
            mainFrame.showMessage("Select a customer from the list first.");
            return;
        }
        manager.deleteCustomer(selectedId);
        mainFrame.showMessage("Customer #" + selectedId + " deleted.");
        clearForm();
        refreshList();
    }

    private void loadSelectedCustomer() {
        int idx = customerList.getSelectedIndex();
        if (idx < 0) return;
        Customer c = manager.getAllCustomers().get(idx);
        selectedId = c.getCustomerId();
        nameField.setText(c.getName());
        contactField.setText(c.getContactNumber());
        emailField.setText(c.getEmail());
        addressField.setText(c.getAddress());
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty() || contactField.getText().trim().isEmpty()) {
            mainFrame.showMessage("Name and Contact Number are required.");
            return false;
        }
        return true;
    }

    private void clearForm() {
        selectedId = -1;
        nameField.setText("");
        contactField.setText("");
        emailField.setText("");
        addressField.setText("");
        customerList.deselect(customerList.getSelectedIndex());
    }

    public void refreshList() {
        customerList.removeAll();
        for (Customer c : manager.getAllCustomers()) {
            customerList.add(c.toString());
        }
    }
}
