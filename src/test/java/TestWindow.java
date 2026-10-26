import fr.acth2.a2util.A2Utils;
import fr.acth2.a2util.elements.A2CheckBox;
import fr.acth2.a2util.elements.A2TextField;
import fr.acth2.a2util.types.Window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TestWindow {
    public static void main(String[] args) {
        A2Utils.setup("test-window", args, true, true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                buildUi();
            }
        });
    }

    private static void buildUi() {
        Window frame = new Window("a2utiltiltils UI Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(8, 8));
        frame.setResizable(false);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("Open"));
        fileMenu.add(new JMenuItem("Save"));
        fileMenu.addSeparator();
        JCheckBoxMenuItem checkItem = new JCheckBoxMenuItem("Check item");
        fileMenu.add(checkItem);
        JRadioButtonMenuItem radioItem = new JRadioButtonMenuItem("Radio item");
        fileMenu.add(radioItem);
        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new JMenuItem("About"));
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JLabel label = new JLabel("Test label");
        JButton button = new JButton("Test button");
        JButton defaultButton = new JButton("Default button");
        JToggleButton toggle = new JToggleButton("Toggle");
        A2CheckBox checkBox = new A2CheckBox("Test checkbox", "0");
        A2CheckBox checkedBox = new A2CheckBox("Checked checkbox", "1");
        A2CheckBox disabledBox = new A2CheckBox("Disabled checkbox", "2");

        System.out.println(checkBox.getIcon());
        System.out.println(UIManager.getLookAndFeel().getName());

        disabledBox.setEnabled(false);
        button.setToolTipText("Tooltip text");
        frame.getRootPane().setDefaultButton(defaultButton);
        top.add(label);
        top.add(button);
        top.add(defaultButton);
        top.add(toggle);
        top.add(checkBox);
        top.add(checkedBox);
        top.add(disabledBox);
        frame.add(top, BorderLayout.NORTH);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createTitledBorder("Inputs"));

        A2TextField textField = new A2TextField("TEST TEXTFIELD", "3");
        JPasswordField passwordField = new JPasswordField("password", 15);
        JFormattedTextField formatted = new JFormattedTextField(42);
        JComboBox<String> combo = new JComboBox<String>(new String[]{"Option A", "Option B", "Option C"});
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        JSlider slider = new JSlider(0, 100, 40);
        JProgressBar progress = new JProgressBar(0, 100);
        progress.setValue(40);
        progress.setStringPainted(true);
        slider.addChangeListener(e -> progress.setValue(slider.getValue()));

        JRadioButton r1 = new JRadioButton("Radio 1", true);
        JRadioButton r2 = new JRadioButton("Radio 2");
        JRadioButton r3 = new JRadioButton("Radio 3");
        ButtonGroup group = new ButtonGroup();
        group.add(r1);
        group.add(r2);
        group.add(r3);
        JPanel radios = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radios.add(r1);
        radios.add(r2);
        radios.add(r3);

        left.add(textField);
        left.add(Box.createVerticalStrut(6));
        left.add(passwordField);
        left.add(Box.createVerticalStrut(6));
        left.add(formatted);
        left.add(Box.createVerticalStrut(6));
        left.add(combo);
        left.add(Box.createVerticalStrut(6));
        left.add(spinner);
        left.add(Box.createVerticalStrut(6));
        left.add(slider);
        left.add(Box.createVerticalStrut(6));
        left.add(progress);
        left.add(Box.createVerticalStrut(6));
        left.add(radios);

        JPanel nested = new JPanel(new GridLayout(0, 2, 6, 6));
        nested.setBorder(BorderFactory.createTitledBorder("Nested JPanel"));
        nested.add(new JLabel("Nested label"));
        nested.add(new A2TextField("Nested field"));
        nested.add(new JButton("Nested button"));
        nested.add(new A2CheckBox("Nested checkbox", "4"));
        nested.add(new JRadioButton("Nested radio"));
        nested.add(new JToggleButton("Nested toggle"));
        JPanel inner = new JPanel(new FlowLayout());
        inner.setBorder(BorderFactory.createTitledBorder("Inner JPanel"));
        inner.add(new JLabel("Deep label"));
        inner.add(new JButton("Deep button"));
        JPanel innerBox = new JPanel(new BorderLayout());
        innerBox.add(inner, BorderLayout.CENTER);
        nested.add(innerBox);
        nested.add(new JComboBox<String>(new String[]{"Nested A", "Nested B"}));
        left.add(Box.createVerticalStrut(10));
        left.add(nested);

        JTextArea textArea = new JTextArea("Select this text to test the selection color.\nSecond line.\nThird line.", 6, 30);
        JList<String> list = new JList<String>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"});
        list.setSelectedIndex(1);

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Active"}, 0) {
            @Override
            public Class<?> getColumnClass(int c) {
                return c == 2 ? Boolean.class : super.getColumnClass(c);
            }
        };
        model.addRow(new Object[]{1, "Alpha", true});
        model.addRow(new Object[]{2, "Beta", false});
        model.addRow(new Object[]{3, "Gamma", true});
        JTable table = new JTable(model);

        JTree tree = new JTree();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Text", new JScrollPane(textArea));
        tabs.addTab("List", new JScrollPane(list));
        tabs.addTab("Table", new JScrollPane(table));
        tabs.addTab("Tree", new JScrollPane(tree));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(left), tabs);
        split.setDividerLocation(380);
        frame.add(split, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel status = new JLabel("Ready");
        JButton dialogButton = new JButton("Show dialog");
        dialogButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Test dialog"));
        JButton printButton = new JButton("Print values");
        printButton.addActionListener(e -> status.setText(
                "text=" + textField.getText()
                        + " combo=" + combo.getSelectedItem()
                        + " spinner=" + spinner.getValue()
                        + " check=" + checkBox.isSelected()));
        bottom.add(status);
        bottom.add(printButton);
        bottom.add(dialogButton);
        frame.add(bottom, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}