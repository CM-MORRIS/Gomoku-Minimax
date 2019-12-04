import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.fxml.FXML;

public class MainMenu {

    @FXML
    public Button startGameBtn;

    @FXML
    public CheckBox checkHuman;
    public CheckBox checkRandomAI;
    public CheckBox checkIntelligentAI;

    /**
     * @return Main menu as parent to be loaded to scene.
     */
     Parent getMainMenu() {
        try {
            Parent mainMenu;
            mainMenu = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
            return mainMenu;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Controller - starts a game and loads the board GUI when start game button clicked.
     */
    public void startGameClick() {
        int opponent = 1;
        if (checkRandomAI.isSelected()) opponent = 2;
        if (checkIntelligentAI.isSelected()) opponent = 3;
        BoardGUI.loadNewGame(opponent);
    }
}


