import fr.acth2.a2util.A2Utils;
import fr.acth2.a2util.types.Window;

import javax.swing.*;
import java.awt.*;

public class PanelTests {
    public static void main(String[] args) {
        A2Utils.setup("panel-test", args, true);
        SwingUtilities.invokeLater(PanelTests::buildUi);
    }

    private static void buildUi() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        Window frame = new Window("Panel Test");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screen);
        frame.setLocation(0, 0);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(screen.width / 3, screen.height));
        panel.setBorder(BorderFactory.createTitledBorder("1/3 panel"));
        panel.add(new JLabel("Test panel", SwingConstants.CENTER), BorderLayout.CENTER);

        frame.add(panel, BorderLayout.WEST);
        frame.setVisible(true);
    }
}