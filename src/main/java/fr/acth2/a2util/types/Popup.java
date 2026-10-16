package fr.acth2.a2util.types;

import javax.swing.*;
import java.awt.*;

public class Popup extends JFrame {
    public Popup() {
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setSize(512, 382);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        applyFrameBackground();
    }

    public Popup(String name) {
        this.setTitle(name);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setSize(512, 382);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        applyFrameBackground();
    }

    @Override
    public void setContentPane(Container contentPane) {
        super.setContentPane(contentPane);
        contentPane.setBackground(UIManager.getColor("A2.frameBackground"));
    }

    private void applyFrameBackground() {
        Color frameBg = UIManager.getColor("A2.frameBackground");
        getContentPane().setBackground(frameBg);
        getRootPane().setBackground(frameBg);
        setBackground(frameBg);
    }

    public void showWindow() {
        this.setVisible(true);
    }
}
