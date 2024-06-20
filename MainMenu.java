import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JMenuBar {
    
    public MainMenu(JFrame frame) {
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');
        JMenuItem restartItem = new JMenuItem("Restart");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem saveItem = new JMenuItem("Save");

        gameMenu.add(restartItem);
        gameMenu.add(exitItem);
        gameMenu.add(saveItem);

        this.add(gameMenu);

        // Add action listeners
        restartItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });
    }

    private void restartGame() {
        // Code to restart the game
        System.out.println("Game restarted.");
    }

    private void saveGame() {
        // Code to save the game
        System.out.println("Game saved.");
    }
}