package fr.acth2.a2util.types;

import javax.swing.*;
import java.awt.*;

public class SubWindow extends JFrame {
    public SubWindow() {
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        applyFrameBackground();
    }

    public SubWindow(String name) {
        this.setTitle(name);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        applyFrameBackground();
    }


    private void applyFrameBackground() {
        Color frameBg = UIManager.getColor("A2.frameBackground");
        getContentPane().setBackground(frameBg);
        getRootPane().setBackground(frameBg);
        setBackground(frameBg);
    }

    @Override
    public void setContentPane(Container contentPane) {
        super.setContentPane(contentPane);
        contentPane.setBackground(UIManager.getColor("A2.frameBackground"));
    }

    public void showWindow() {
        this.setVisible(true);
    }
}
