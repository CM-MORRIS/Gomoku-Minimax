import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.fxml.FXML;
import java.net.URL;

public class GameOverMenu implements Initializable {

    @FXML
    public Button mainMenuBtn;

    @FXML
    public Text winnerText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        winnerText.setText("");
    }


    /**
     * Chooses and makes a move for the AI
     *
     * @param winner takes in winner as string to print on game over screen.
     *
     */
    Parent loadGameOver(String winner) {
        try {
            Parent gameOverScreen;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameOver.fxml"));
            gameOverScreen = loader.load();

            GameOverMenu controller = loader.getController();

            controller.winnerText.setText(winner);

            return gameOverScreen;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Controller
     * Loads mainMenu when main menu button is clicked.
     *
     */
    public void onMainMenuBtnClick() {
        BoardGUI.loadMainMenu();
    }
}
