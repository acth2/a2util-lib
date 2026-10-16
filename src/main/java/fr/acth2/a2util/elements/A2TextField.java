package fr.acth2.a2util.elements;

import fr.acth2.a2util.A2Utils;

import javax.swing.*;
import java.awt.*;

public class A2TextField extends JTextField {
    private final String placeholder;

    public A2TextField(String placeholder) {
        this.placeholder = placeholder;

        setForeground(Color.BLACK);

        getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                repaint();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                repaint();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                repaint();
            }
        });
    }

    public A2TextField(String placeholder, String memoryKey) {
        this.placeholder = placeholder;

        setForeground(Color.BLACK);

        this.setText(A2Utils.getMemoryManager().getStr(memoryKey));

        Timer typingTimer = new Timer(2000, e -> {
            String text = getText();
            A2Utils.getMemoryManager().setStr(memoryKey, text);
        });

        typingTimer.setRepeats(false);

        getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                repaint();
                typingTimer.restart();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                repaint();
                typingTimer.restart();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                repaint();
                typingTimer.restart();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getText().isEmpty() && !isFocusOwner()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.GRAY);
            g2.setFont(getFont());
            g2.drawString(placeholder, getInsets().left,
                    g.getFontMetrics().getAscent()
                            + getInsets().top);
            g2.dispose();
        }
    }
}