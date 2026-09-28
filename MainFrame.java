import java.awt.*;
import java.awt.event.*;

/**
 * Top-level application window. Holds a MenuBar, a CardLayout content area
 * that switches between the Dashboard and each module's panel, and helper
 * methods the panels use to navigate and show messages.
 */
public class MainFrame extends Frame {

    private final RepairShopManager manager;

    private CardLayout cardLayout = new CardLayout();
    private Panel cards = new Panel(cardLayout);

    private DashboardPanel dashboardPanel;
    private CustomerPanel customerPanel;
    private DevicePanel devicePanel;
    private RepairJobPanel jobPanel;
    private BillingPanel billingPanel;
    private SearchPanel searchPanel;

    public MainFrame() {
        super("Computer Repair Shop Management System");
        manager = new RepairShopManager();

        setLayout(new BorderLayout());
        setMenuBar(buildMenuBar());

        dashboardPanel = new DashboardPanel(this);
        customerPanel = new CustomerPanel(manager, this);
        devicePanel = new DevicePanel(manager, this);
        jobPanel = new RepairJobPanel(manager, this);
        billingPanel = new BillingPanel(manager, this);
        searchPanel = new SearchPanel(manager, this);

        cards.add(dashboardPanel, "DASHBOARD");
        cards.add(customerPanel, "CUSTOMER");
        cards.add(devicePanel, "DEVICE");
        cards.add(jobPanel, "JOB");
        cards.add(billingPanel, "BILLING");
        cards.add(searchPanel, "SEARCH");

        add(cards, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        setSize(950, 620);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save Now");
        saveItem.addActionListener(e -> {
            manager.save();
            showMessage("All records saved.");
        });
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> confirmExit());
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        Menu navMenu = new Menu("Navigate");
        String[] labels = {"Dashboard", "Customers", "Devices", "Repair Jobs", "Billing", "Search"};
        String[] cardNames = {"DASHBOARD", "CUSTOMER", "DEVICE", "JOB", "BILLING", "SEARCH"};
        for (int i = 0; i < labels.length; i++) {
            MenuItem item = new MenuItem(labels[i]);
            final String target = cardNames[i];
            item.addActionListener(e -> showCard(target));
            navMenu.add(item);
        }

        menuBar.add(fileMenu);
        menuBar.add(navMenu);
        return menuBar;
    }

    /** Switches the visible card and refreshes its data (dropdowns/lists) first. */
    public void showCard(String name) {
        switch (name) {
            case "CUSTOMER": customerPanel.refreshList(); break;
            case "DEVICE": devicePanel.refreshList(); break;
            case "JOB": jobPanel.refreshList(); break;
            case "BILLING": billingPanel.refreshList(); break;
            default: break;
        }
        cardLayout.show(cards, name);
    }

    /** Simple modal message dialog used by every panel for feedback/validation errors. */
    public void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Notice", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(new Label(message, Label.CENTER), BorderLayout.CENTER);
        Button ok = new Button("OK");
        ok.addActionListener(e -> dialog.dispose());
        Panel btnPanel = new Panel();
        btnPanel.add(ok);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setSize(320, 130);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /** Saves data and closes the application after user confirmation. */
    public void confirmExit() {
        Dialog dialog = new Dialog(this, "Confirm Exit", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(new Label("Save records and exit the application?", Label.CENTER), BorderLayout.CENTER);
        Panel btnPanel = new Panel();
        Button yes = new Button("Yes");
        Button no = new Button("Cancel");
        yes.addActionListener(e -> {
            manager.save();
            dialog.dispose();
            dispose();
            System.exit(0);
        });
        no.addActionListener(e -> dialog.dispose());
        btnPanel.add(yes);
        btnPanel.add(no);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setSize(340, 130);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
