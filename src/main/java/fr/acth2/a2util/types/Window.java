package fr.acth2.a2util.types;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public Window(String name) {
        this.setTitle(name);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        applyFrameBackground();
    }

    public Window() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1280, 720);
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
