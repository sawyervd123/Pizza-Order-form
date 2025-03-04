import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrust, regularCrust, deepDishCrust;
    private JComboBox<String> sizeBox;
    private JCheckBox[] toppings;
    private JTextArea orderSummary;
    private JButton orderButton, clearButton, quitButton;

    private final String[] sizes = {"Small", "Medium", "Large", "Super"};
    private final double[] sizePrices = {8.00, 12.00, 16.00, 20.00};
    private final String[] toppingNames = {"Pepperoni", "Mushrooms", "Sausage", "Bacon", "Onions", "Extra Cheese"};
    private final double toppingPrice = 1.00;
    private final double taxRate = 0.07;

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create panels
        JPanel crustPanel = createCrustPanel();
        JPanel sizePanel = createSizePanel();
        JPanel toppingsPanel = createToppingsPanel();
        JPanel outputPanel = createOutputPanel();
        JPanel buttonPanel = createButtonPanel();

        // Layout panels
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(toppingsPanel, BorderLayout.CENTER);
        centerPanel.add(outputPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createCrustPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Crust Type"));

        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDishCrust = new JRadioButton("Deep-dish");

        ButtonGroup group = new ButtonGroup();
        group.add(thinCrust);
        group.add(regularCrust);
        group.add(deepDishCrust);

        panel.add(thinCrust);
        panel.add(regularCrust);
        panel.add(deepDishCrust);

        return panel;
    }

    private JPanel createSizePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Size"));
        sizeBox = new JComboBox<>(sizes);
        panel.add(sizeBox);
        return panel;
    }

    private JPanel createToppingsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Toppings ($1 each)"));
        panel.setLayout(new GridLayout(3, 2));
        toppings = new JCheckBox[toppingNames.length];
        for (int i = 0; i < toppingNames.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            panel.add(toppings[i]);
        }
        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Order Summary"));
        orderSummary = new JTextArea(10, 40);
        orderSummary.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderSummary);
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");

        panel.add(orderButton);
        panel.add(clearButton);
        panel.add(quitButton);

        orderButton.addActionListener(new OrderListener());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> confirmExit());

        return panel;
    }

    private void clearForm() {
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepDishCrust.setSelected(false);
        sizeBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        orderSummary.setText("");
    }

    private void confirmExit() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private class OrderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String crust = "";
            if (thinCrust.isSelected()) crust = "Thin Crust";
            if (regularCrust.isSelected()) crust = "Regular Crust";
            if (deepDishCrust.isSelected()) crust = "Deep Dish";

            if (crust.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a crust type!");
                return;
            }

            int sizeIndex = sizeBox.getSelectedIndex();
            double basePrice = sizePrices[sizeIndex];

            StringBuilder toppingList = new StringBuilder();
            int toppingCount = 0;
            for (JCheckBox topping : toppings) {
                if (topping.isSelected()) {
                    toppingList.append(String.format("%-25s $1.00\n", topping.getText()));
                    toppingCount++;
                }
            }

            if (toppingCount == 0) {
                JOptionPane.showMessageDialog(null, "Please select at least one topping!");
                return;
            }

            double subtotal = basePrice + (toppingCount * toppingPrice);
            double tax = subtotal * taxRate;
            double total = subtotal + tax;

            orderSummary.setText(String.format(
                    "=========================================\n" +
                            "%-25s $%.2f\n" +
                            "%-25s\n" +
                            "%s\n" +
                            "Sub-total:                 $%.2f\n" +
                            "Tax:                        $%.2f\n" +
                            "---------------------------------------------------------------------\n" +
                            "Total:                     $%.2f\n" +
                            "=========================================\n",
                    crust + " & " + sizes[sizeIndex], basePrice,
                    "Ingredient", toppingList.toString(), subtotal, tax, total));
        }
    }
}
