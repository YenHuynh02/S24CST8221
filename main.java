import javax.swing.JFrame;

public class main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        BattleshipView view = new BattleshipView();
        BattleshipModel model = new BattleshipModel();
        new BattleshipController(view, model);

        // Create and set the main menu
        MainMenu mainMenu = new MainMenu();
        mainMenu.addMenuToFrame(frame);

        frame.add(view.getMainPanel());  // Add the main panel from BattleshipView
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
