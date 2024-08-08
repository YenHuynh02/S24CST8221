import javax.swing.*;

public class MainMenu extends JMenuBar {
    private BattleshipView view;

    public MainMenu(BattleshipView view) {
        this.view = view;

        JMenu gameMenu = new JMenu("Game");
        add(gameMenu);

        JMenuItem newGame = new JMenuItem("New Game");
        gameMenu.add(newGame);

        JMenuItem exitGame = new JMenuItem("Exit");
        gameMenu.add(exitGame);

        JMenuItem language = new JMenuItem("Language");
        gameMenu.add(language);

        language.addActionListener(e -> showLanguageSelectionDialog());

        exitGame.addActionListener(e -> System.exit(0));
    }

    public void addMenuToFrame(JFrame frame) {
        frame.setJMenuBar(this);
    }

    private void showLanguageSelectionDialog() {
        String[] options = {"English", "Vietnamese"};
        int choice = JOptionPane.showOptionDialog(null, "Select Language", "Language Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            view.updateLanguageToVietnamese();
        } else {
            view.updateLanguageToEnglish();
        }
    }
}