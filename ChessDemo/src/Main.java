import view.ChessGameFrame;
import view.Menu;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu upperFrame = new Menu(1000, 760);
            upperFrame.setVisible(true);
        });
    }
}
