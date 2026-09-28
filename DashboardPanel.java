import java.awt.*;
import java.awt.event.*;

/**
 * Landing panel shown when the application starts. Provides large buttons
 * to jump straight to each module.
 */
public class DashboardPanel extends Panel {

    public DashboardPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        Label title = new Label("Computer Repair Shop Management System", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        Panel buttonGrid = new Panel(new GridLayout(3, 2, 20, 20));
        buttonGrid.setBackground(new Color(240, 244, 250));

        String[] labels = {
            "Customer Management", "Device Management",
            "Repair Jobs", "Billing",
            "Search Records", "Exit Application"
        };
        String[] cards = {
            "CUSTOMER", "DEVICE", "JOB", "BILLING", "SEARCH", "EXIT"
        };

        for (int i = 0; i < labels.length; i++) {
            Button b = new Button(labels[i]);
            b.setFont(new Font("SansSerif", Font.PLAIN, 14));
            final String target = cards[i];
            b.addActionListener(e -> {
                if (target.equals("EXIT")) {
                    mainFrame.confirmExit();
                } else {
                    mainFrame.showCard(target);
                }
            });
            buttonGrid.add(b);
        }

        Panel center = new Panel(new FlowLayout(FlowLayout.CENTER));
        center.add(buttonGrid);
        add(center, BorderLayout.CENTER);
    }
}
