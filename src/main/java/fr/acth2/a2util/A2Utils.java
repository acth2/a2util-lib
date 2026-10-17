package fr.acth2.a2util;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import fr.acth2.a2util.arguments.Arguments;
import fr.acth2.a2util.logging.Logger;
import fr.acth2.a2util.memory.MemoryManager;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class A2Utils {
    private static Arguments programArguments;
    private static MemoryManager memoryManager;

    public static void setup(String name, String[] args, boolean loadLaF) {
        Logger.initialize(name);

        programArguments = new Arguments();
        memoryManager = new MemoryManager(name);
        initArgs(args);

        if (loadLaF) {
            FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#000000"));
            FlatDarkLaf.setup();

            UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
            for (Object uiKey : new ArrayList<Object>(lafDefaults.keySet())) {
                if (!(uiKey instanceof String)) continue;
                if (!(lafDefaults.get(uiKey) instanceof Color)) continue;
                String keyStr = ((String) uiKey).toLowerCase();

                if (keyStr.contains("selection") && keyStr.endsWith("background") || keyStr.equals("texthighlight")) {
                    UIManager.put(uiKey, new ColorUIResource(Color.BLACK));
                } else if (keyStr.contains("selection") && keyStr.endsWith("foreground") || keyStr.equals("texthighlighttext")) {
                    UIManager.put(uiKey, new ColorUIResource(Color.WHITE));
                } else if (keyStr.endsWith("foreground") || keyStr.endsWith("disabledtext")) {
                    UIManager.put(uiKey, new ColorUIResource(Color.BLACK));
                }
            }

            ColorUIResource black = new ColorUIResource(Color.BLACK);
            ColorUIResource white = new ColorUIResource(Color.WHITE);
            ColorUIResource panelColor = new ColorUIResource(new Color(-13618636));

            Color frameDefault = UIManager.getColor("Panel.background");
            UIManager.put("A2.frameBackground", new Color(frameDefault.getRGB()));
            UIManager.put("Panel.background", panelColor);


            UIManager.put("Component.focusColor", black);
            UIManager.put("Component.focusedBorderColor", black);
            UIManager.put("Button.focusedBorderColor", black);
            UIManager.put("Button.default.focusedBorderColor", black);
            UIManager.put("Button.default.focusColor", black);

            UIManager.put("CheckBox.icon.checkmarkColor", black);
            UIManager.put("CheckBox.icon.disabledCheckmarkColor", black);
            UIManager.put("CheckBox.icon.background", white);
            UIManager.put("CheckBox.icon.selectedBackground", white);
            UIManager.put("CheckBox.icon.focusedBackground", white);
            UIManager.put("CheckBox.icon.borderColor", black);
            UIManager.put("CheckBox.icon.selectedBorderColor", black);
            UIManager.put("CheckBox.icon.focusedBorderColor", black);
            UIManager.put("CheckBox.icon.focusedSelectedBorderColor", black);
            UIManager.put("CheckBox.icon.hoverBorderColor", black);
            UIManager.put("CheckBox.icon.pressedBorderColor", black);

            String[] buttonKeys = {"background", "foreground", "hoverBackground", "pressedBackground",
                    "borderColor", "focusedBorderColor", "focusColor", "hoverBorderColor"};
            for (String buttonKey : buttonKeys) {
                Color regular = UIManager.getColor("Button." + buttonKey);
                if (regular != null) {
                    UIManager.put("Button.default." + buttonKey, new ColorUIResource(regular));
                }
            }

            installTableCheckBoxes();
        }
    }

    private static void installTableCheckBoxes() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (!(event.getSource() instanceof JTable)) return;
                JTable table = (JTable) event.getSource();
                if (table.getClientProperty("a2.checkBoxInstalled") != null) return;
                table.putClientProperty("a2.checkBoxInstalled", Boolean.TRUE);

                final JCheckBox rendererBox = new JCheckBox();
                rendererBox.setHorizontalAlignment(SwingConstants.CENTER);
                rendererBox.setOpaque(true);

                table.setDefaultRenderer(Boolean.class, new TableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                                   boolean hasFocus, int row, int column) {
                        rendererBox.setSelected(value instanceof Boolean && (Boolean) value);
                        rendererBox.setBackground(isSelected ? t.getSelectionBackground() : t.getBackground());
                        return rendererBox;
                    }
                });

                JCheckBox editorBox = new JCheckBox();
                editorBox.setHorizontalAlignment(SwingConstants.CENTER);
                editorBox.setOpaque(true);
                editorBox.setBackground(table.getSelectionBackground());
                table.setDefaultEditor(Boolean.class, new DefaultCellEditor(editorBox));
            }
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
            protected JDialog createDialog(Component parent) throws HeadlessException {
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
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            return file.getAbsolutePath();
        }
        return null;
    }

    private void playNotification() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    float rate = 44100f;
                    int[] freqs = {660, 880};
                    ByteArrayOutputStream buf = new ByteArrayOutputStream();
                    for (int f : freqs) {
                        int samples = (int) (rate * 0.18);
                        for (int i = 0; i < samples; i++) {
                            double env = Math.min(1.0, Math.min(i, samples - i) / (rate * 0.01));
                            short v = (short) (Math.sin(2 * Math.PI * f * i / rate) * 12000 * env);
                            buf.write(v & 0xff);
                            buf.write((v >> 8) & 0xff);
                        }
                    }
                    byte[] data = buf.toByteArray();
                    AudioFormat format = new AudioFormat(rate, 16, 1, true, false);
                    SourceDataLine line = AudioSystem.getSourceDataLine(format);
                    line.open(format);
                    line.start();
                    line.write(data, 0, data.length);
                    line.drain();
                    line.close();
                } catch (Exception e) {
                    Toolkit.getDefaultToolkit().beep();
                }
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
