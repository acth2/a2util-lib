
package fr.acth2.a2util;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatLaf;
import fr.acth2.a2util.arguments.Arguments;
import fr.acth2.a2util.logging.Logger;
import fr.acth2.a2util.memory.MemoryManager;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class A2Utils {

    private static Arguments programArguments;
    private static MemoryManager memoryManager;

    public static void setup(
            String name,
            String[] args,
            boolean loadLaF,
            boolean whiteMode
    ) {
        Logger.initialize(name);

        programArguments = new Arguments();
        memoryManager = new MemoryManager(name);

        initArgs(args);

        if (!loadLaF) {
            return;
        }

        FlatLaf.setGlobalExtraDefaults(
                Collections.singletonMap(
                        "@accentColor",
                        whiteMode ? "#B8B8B8" : "#000000"
                )
        );

        if (whiteMode) {
            FlatLightLaf.setup();
            configureWhiteMode();
        } else {
            FlatDarkLaf.setup();
            configureDarkMode();
        }

        configureCheckbox(whiteMode);
        installTableCheckBoxes();
    }

    private static void configureCheckbox(boolean whiteMode) {
        ColorUIResource background;
        ColorUIResource selectedBackground;
        ColorUIResource border;
        ColorUIResource selectedBorder;
        ColorUIResource checkmark;
        ColorUIResource disabledBackground;
        ColorUIResource disabledCheckmark;

        if (whiteMode) {
            background = color(255, 255, 255);
            selectedBackground = color(255, 255, 255);
            border = color(220, 220, 220);
            selectedBorder = color(205, 205, 205);
            checkmark = color(165, 165, 165);
            disabledBackground = color(250, 250, 250);
            disabledCheckmark = color(205, 205, 205);
        } else {
            background = color(35, 35, 35);
            selectedBackground = color(35, 35, 35);
            border = color(255, 255, 255);
            selectedBorder = color(255, 255, 255);
            checkmark = color(255, 255, 255);
            disabledBackground = color(55, 55, 55);
            disabledCheckmark = color(150, 150, 150);
        }

        putCheckboxColors(
                "CheckBox.icon.",
                background,
                selectedBackground,
                border,
                selectedBorder,
                checkmark,
                disabledBackground,
                disabledCheckmark
        );

        putCheckboxColors(
                "CheckBox.icon[filled].",
                background,
                selectedBackground,
                border,
                selectedBorder,
                checkmark,
                disabledBackground,
                disabledCheckmark
        );

        UIManager.put("CheckBox.icon.style", "filled");
        UIManager.put("CheckBox.opaque", true);

        UIManager.put(
                "CheckBox.background",
                whiteMode ? color(255, 255, 255) : color(35, 35, 35)
        );

        UIManager.put(
                "CheckBox.foreground",
                whiteMode ? color(100, 100, 100) : color(255, 255, 255)
        );
    }

    private static void putCheckboxColors(
            String prefix,
            ColorUIResource background,
            ColorUIResource selectedBackground,
            ColorUIResource border,
            ColorUIResource selectedBorder,
            ColorUIResource checkmark,
            ColorUIResource disabledBackground,
            ColorUIResource disabledCheckmark
    ) {
        UIManager.put(prefix + "background", background);
        UIManager.put(prefix + "selectedBackground", selectedBackground);

        UIManager.put(prefix + "borderColor", border);
        UIManager.put(prefix + "selectedBorderColor", selectedBorder);

        UIManager.put(prefix + "checkmarkColor", checkmark);

        UIManager.put(prefix + "disabledBackground", disabledBackground);
        UIManager.put(prefix + "disabledCheckmarkColor", disabledCheckmark);
        UIManager.put(prefix + "disabledBorderColor", disabledCheckmark);
        UIManager.put(
                prefix + "disabledSelectedBorderColor",
                disabledCheckmark
        );
    }

    private static ColorUIResource color(int r, int g, int b) {
        return new ColorUIResource(r, g, b);
    }

    private static void configureDarkMode() {
        UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();

        for (Object uiKey : new ArrayList<>(lafDefaults.keySet())) {
            if (!(uiKey instanceof String)) {
                continue;
            }

            if (!(lafDefaults.get(uiKey) instanceof Color)) {
                continue;
            }

            String keyStr = ((String) uiKey).toLowerCase();

            if ((keyStr.contains("selection")
                    && keyStr.endsWith("background"))
                    || keyStr.equals("texthighlight")) {

                UIManager.put(
                        uiKey,
                        new ColorUIResource(Color.BLACK)
                );

            } else if ((keyStr.contains("selection")
                    && keyStr.endsWith("foreground"))
                    || keyStr.equals("texthighlighttext")) {

                UIManager.put(
                        uiKey,
                        new ColorUIResource(Color.WHITE)
                );

            } else if (keyStr.endsWith("foreground")
                    || keyStr.endsWith("disabledtext")) {

                UIManager.put(
                        uiKey,
                        new ColorUIResource(Color.BLACK)
                );
            }
        }

        ColorUIResource black = color(0, 0, 0);
        ColorUIResource white = color(255, 255, 255);
        ColorUIResource panelColor =
                new ColorUIResource(new Color(-13618636));

        Color frameDefault = UIManager.getColor("Panel.background");

        if (frameDefault != null) {
            UIManager.put(
                    "A2.frameBackground",
                    new Color(frameDefault.getRGB())
            );
        }

        UIManager.put("Panel.background", panelColor);
        UIManager.put("Panel.opaque", true);

        UIManager.put("Component.focusColor", black);
        UIManager.put("Component.focusedBorderColor", black);
        UIManager.put("Button.focusedBorderColor", black);
        UIManager.put("Button.default.focusedBorderColor", black);
        UIManager.put("Button.default.focusColor", black);

        UIManager.put("TitledBorder.titleColor", white);

        String[] buttonKeys = {
                "background",
                "foreground",
                "hoverBackground",
                "pressedBackground",
                "borderColor",
                "focusedBorderColor",
                "focusColor",
                "hoverBorderColor"
        };

        for (String buttonKey : buttonKeys) {
            Color regular = UIManager.getColor("Button." + buttonKey);

            if (regular != null) {
                UIManager.put(
                        "Button.default." + buttonKey,
                        new ColorUIResource(regular)
                );
            }
        }
    }

    private static void configureWhiteMode() {
        ColorUIResource gray = color(180, 180, 180);
        ColorUIResource textGray = color(100, 100, 100);
        ColorUIResource white = color(255, 255, 255);
        ColorUIResource lightPanel = color(212, 212, 212);

        UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();

        for (Object uiKey : new ArrayList<>(lafDefaults.keySet())) {
            if (!(uiKey instanceof String)) {
                continue;
            }

            if (!(lafDefaults.get(uiKey) instanceof Color)) {
                continue;
            }

            String keyStr = ((String) uiKey).toLowerCase();

            if ((keyStr.contains("selection")
                    && keyStr.endsWith("background"))
                    || keyStr.equals("texthighlight")) {

                UIManager.put(
                        uiKey,
                        new ColorUIResource(196, 192, 192)
                );

            } else if ((keyStr.contains("selection")
                    && keyStr.endsWith("foreground"))
                    || keyStr.equals("texthighlighttext")) {

                UIManager.put(uiKey, textGray);
            }
        }

        Color frameDefault = UIManager.getColor("Panel.background");

        if (frameDefault != null) {
            UIManager.put(
                    "A2.frameBackground",
                    new Color(frameDefault.getRGB())
            );
        }

        UIManager.put("Panel.background", lightPanel);
        UIManager.put("Panel.opaque", true);

        UIManager.put("Component.focusColor", gray);
        UIManager.put("Component.focusedBorderColor", gray);
        UIManager.put("Button.focusedBorderColor", gray);
        UIManager.put("Button.default.focusedBorderColor", gray);
        UIManager.put("Button.default.focusColor", gray);

        UIManager.put("TitledBorder.titleColor", textGray);

        UIManager.put("CheckBox.background", white);
        UIManager.put("CheckBox.foreground", textGray);

        UIManager.put("ProgressBar.background", color(245, 245, 245));
        UIManager.put("ProgressBar.foreground", color(215, 215, 215));
        UIManager.put("ProgressBar.selectionBackground", textGray);
        UIManager.put("ProgressBar.selectionForeground", textGray);

        UIManager.put("ProgressBar.horizontalSize", new Dimension(146, 16));
        UIManager.put("ProgressBar.verticalSize", new Dimension(16, 146));
    }

    private static void installTableCheckBoxes() {
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (!(event.getSource() instanceof JTable)) {
                return;
            }

            JTable table = (JTable) event.getSource();

            if (table.getClientProperty("a2.checkBoxInstalled") != null) {
                return;
            }

            table.putClientProperty(
                    "a2.checkBoxInstalled",
                    Boolean.TRUE
            );

            final JCheckBox rendererBox = new JCheckBox();
            rendererBox.setHorizontalAlignment(SwingConstants.CENTER);
            rendererBox.setOpaque(true);

            table.setDefaultRenderer(
                    Boolean.class,
                    (t, value, isSelected, hasFocus, row, column) -> {
                        rendererBox.setSelected(
                                value instanceof Boolean
                                        && (Boolean) value
                        );

                        rendererBox.setBackground(
                                isSelected
                                        ? t.getSelectionBackground()
                                        : t.getBackground()
                        );

                        rendererBox.setForeground(
                                isSelected
                                        ? t.getSelectionForeground()
                                        : t.getForeground()
                        );

                        return rendererBox;
                    }
            );

            JCheckBox editorBox = new JCheckBox();
            editorBox.setHorizontalAlignment(SwingConstants.CENTER);
            editorBox.setOpaque(true);
            editorBox.setBackground(table.getSelectionBackground());

            table.setDefaultEditor(
                    Boolean.class,
                    new DefaultCellEditor(editorBox)
            );
        }, AWTEvent.HIERARCHY_EVENT_MASK);
    }

    private static void initArgs(String[] args) {
        for (String arg : args) {
            if ("--help".equalsIgnoreCase(arg)) {
                Logger.log("Available arguments:");
                Logger.log("  --debug    Enable debug mode");
                Logger.log("  --help     Show this help menu");
                System.exit(0);
            }

            if ("--debug".equalsIgnoreCase(arg)) {
                Logger.log("--debug argument found");
                programArguments.setDebug(true);
            }
        }
    }

    public static String pickFile() {
        JFileChooser chooser = new JFileChooser() {
            @Override
            protected JDialog createDialog(Component parent)
                    throws HeadlessException {

                JDialog dialog = super.createDialog(parent);
                dialog.setResizable(false);
                dialog.setSize(900, 600);
                dialog.setLocationRelativeTo(parent);

                return dialog;
            }
        };

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);

        int result = chooser.showOpenDialog(null);

        return result == JFileChooser.APPROVE_OPTION
                ? chooser.getSelectedFile().getAbsolutePath()
                : null;
    }

    public static void playNotification() {
        new Thread(() -> {
            try {
                float rate = 44100f;
                int[] freqs = {660, 880};

                ByteArrayOutputStream buf =
                        new ByteArrayOutputStream();

                for (int f : freqs) {
                    int samples = (int) (rate * 0.18);

                    for (int i = 0; i < samples; i++) {
                        double env = Math.min(
                                1.0,
                                Math.min(i, samples - i) / (rate * 0.01)
                        );

                        short v = (short) (
                                Math.sin(2 * Math.PI * f * i / rate)
                                        * 12000
                                        * env
                        );

                        buf.write(v & 0xff);
                        buf.write((v >> 8) & 0xff);
                    }
                }

                byte[] data = buf.toByteArray();

                AudioFormat format =
                        new AudioFormat(rate, 16, 1, true, false);

                SourceDataLine line =
                        AudioSystem.getSourceDataLine(format);

                line.open(format);
                line.start();
                line.write(data, 0, data.length);
                line.drain();
                line.close();

            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }

    public static Arguments getProgramArguments() {
        return programArguments;
    }

    public static MemoryManager getMemoryManager() {
        return memoryManager;
    }
}
