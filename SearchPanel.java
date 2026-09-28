import java.awt.*;
import java.awt.event.*;

/**
 * Panel that lets the user search customers, devices, or repair jobs
 * using a chosen search type and a keyword.
 */
public class SearchPanel extends Panel {

    private final RepairShopManager manager;
    private final MainFrame mainFrame;

    private Choice searchTypeChoice = new Choice();
    private TextField queryField = new TextField(20);
    private TextArea resultsArea = new TextArea(15, 50);

    private static final String[] SEARCH_TYPES = {
        "Customer by Name", "Customer by ID", "Device by ID",
        "Device by Serial Number", "Repair Job by ID", "Repair Jobs by Status"
    };

    public SearchPanel(RepairShopManager manager, MainFrame mainFrame) {
        this.manager = manager;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));

        for (String t : SEARCH_TYPES) searchTypeChoice.add(t);
        resultsArea.setEditable(false);

        Panel controls = new Panel(new FlowLayout(FlowLayout.LEFT));
        controls.add(new Label("Search Type:"));
        controls.add(searchTypeChoice);
        controls.add(new Label("Query:"));
        controls.add(queryField);
        Button searchBtn = new Button("Search");
        searchBtn.addActionListener(e -> performSearch());
        controls.add(searchBtn);

        Panel north = new Panel(new BorderLayout());
        north.add(buildTopBar(), BorderLayout.NORTH);
        north.add(controls, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);

        add(resultsArea, BorderLayout.CENTER);
    }

    private Panel buildTopBar() {
        Panel bar = new Panel(new BorderLayout());
        Label heading = new Label("Search Records", Label.LEFT);
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        bar.add(heading, BorderLayout.WEST);
        Button back = new Button("Back to Dashboard");
        back.addActionListener(e -> mainFrame.showCard("DASHBOARD"));
        bar.add(back, BorderLayout.EAST);
        return bar;
    }

    private void performSearch() {
        String type = searchTypeChoice.getSelectedItem();
        String query = queryField.getText().trim();
        StringBuilder sb = new StringBuilder();

        switch (type) {
            case "Customer by Name":
                for (Customer c : manager.searchCustomersByName(query)) {
                    sb.append("Customer #").append(c.getCustomerId()).append(" - ").append(c.getName())
                      .append(" | Contact: ").append(c.getContactNumber())
                      .append(" | Email: ").append(c.getEmail())
                      .append(" | Address: ").append(c.getAddress()).append("\n");
                }
                break;
            case "Customer by ID":
                try {
                    Customer c = manager.findCustomerById(Integer.parseInt(query));
                    if (c != null) {
                        sb.append("Customer #").append(c.getCustomerId()).append(" - ").append(c.getName())
                          .append(" | Contact: ").append(c.getContactNumber())
                          .append(" | Email: ").append(c.getEmail())
                          .append(" | Address: ").append(c.getAddress()).append("\n");
                    } else {
                        sb.append("No customer found with ID ").append(query).append("\n");
                    }
                } catch (NumberFormatException ex) {
                    sb.append("Enter a numeric Customer ID.\n");
                }
                break;
            case "Device by ID":
                try {
                    Device d = manager.findDeviceById(Integer.parseInt(query));
                    appendDeviceResult(sb, d, query);
                } catch (NumberFormatException ex) {
                    sb.append("Enter a numeric Device ID.\n");
                }
                break;
            case "Device by Serial Number":
                Device d = manager.findDeviceBySerial(query);
                appendDeviceResult(sb, d, query);
                break;
            case "Repair Job by ID":
                try {
                    RepairJob j = manager.findJobById(Integer.parseInt(query));
                    appendJobResult(sb, j, query);
                } catch (NumberFormatException ex) {
                    sb.append("Enter a numeric Job ID.\n");
                }
                break;
            case "Repair Jobs by Status":
                for (RepairJob job : manager.getJobsByStatus(query)) {
                    appendJobResult(sb, job, query);
                }
                break;
        }

        if (sb.length() == 0) {
            sb.append("No matching records found.");
        }
        resultsArea.setText(sb.toString());
    }

    private void appendDeviceResult(StringBuilder sb, Device d, String query) {
        if (d != null) {
            sb.append("Device #").append(d.getDeviceId()).append(" - ").append(d.getDeviceType())
              .append(" ").append(d.getBrand()).append(" ").append(d.getModel())
              .append(" | Serial: ").append(d.getSerialNumber())
              .append(" | Customer #").append(d.getCustomerId())
              .append(" | Problem: ").append(d.getReportedProblem()).append("\n");
        } else {
            sb.append("No device found for '").append(query).append("'\n");
        }
    }

    private void appendJobResult(StringBuilder sb, RepairJob j, String query) {
        if (j != null) {
            sb.append("Job #").append(j.getJobId()).append(" - Device #").append(j.getDeviceId())
              .append(" | Status: ").append(j.getStatus())
              .append(" | Technician: ").append(j.getTechnicianName())
              .append(" | Diagnosis: ").append(j.getDiagnosis())
              .append(" | Charge: Rs.").append(j.getServiceCharge()).append("\n");
        } else {
            sb.append("No repair job found for '").append(query).append("'\n");
        }
    }
}
