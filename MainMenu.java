import javax.swing.*;

public class MainMenu extends JMenuBar {
    public MainMenu() {
        JMenu gameMenu = new JMenu("Game");
        add(gameMenu);

        JMenuItem newGame = new JMenuItem("New Game");
        gameMenu.add(newGame);

        JMenuItem exitGame = new JMenuItem("Exit");
        gameMenu.add(exitGame);

        exitGame.addActionListener(e -> System.exit(0));
    }

    public void addMenuToFrame(JFrame frame) {
        frame.setJMenuBar(this);
    }
}
