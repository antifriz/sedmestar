import gui.GUI;
import model.Model;

/**
 * Created by ivan on 6/16/15.
 */
public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        GUI gui = new GUI(model);
        gui.setBounds(100, 100, 500, 400);
        gui.setVisible(true);

    }
}
