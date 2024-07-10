public class main {
    public static void main(String[] args) {
        BattleshipModel model = new BattleshipModel();
        BattleshipView view = new BattleshipView();
        BattleshipController controller = new BattleshipController(model, view);
    }
}
