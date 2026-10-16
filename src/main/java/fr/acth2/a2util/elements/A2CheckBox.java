package fr.acth2.a2util.elements;

import fr.acth2.a2util.A2Utils;

import javax.swing.*;
import java.awt.*;

public class A2CheckBox extends JCheckBox {
    public A2CheckBox(String text, String memoryKey) {
        super(text);
        setForeground(Color.BLACK);

        setSelected(A2Utils.getMemoryManager().getBool(memoryKey));

        addItemListener(e -> {
            A2Utils.getMemoryManager().setBool(memoryKey, isSelected());
        });
    }
}
