import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        BattleshipView view = new BattleshipView();
        BattleshipModel model = new BattleshipModel();
        Client client = new Client(view);
        new BattleshipController(view, model, client.getNetwork());

        // Create and set the main menu
        MainMenu mainMenu = new MainMenu(view);
        mainMenu.addMenuToFrame(frame);

        frame.add(view.getCardPanel());  // Add the card panel from BattleshipView
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Show language selection dialog
        showLanguageSelectionDialog(view);
    }

    private static void showLanguageSelectionDialog(BattleshipView view) {
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
