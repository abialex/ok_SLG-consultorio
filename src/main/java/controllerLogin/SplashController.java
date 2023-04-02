package controllerLogin;

import Entidades.User;
import Repository.local.LocalRepository;
import Repository.remote.AutheticationRepository;
import RepositoryInterface.local.ILocalRepository;
import RepositoryInterface.remote.IAutheticationRepository;
import Util.UtilClass;
import controller.Paciente.VerPacienteController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    @FXML
    AnchorPane ap;
    UtilClass oUtilClass = new UtilClass();
    IAutheticationRepository IautheticationRepository = new AutheticationRepository();
    ILocalRepository IlocalRepository = new LocalRepository();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validateSession();
    }

    private void validateSession(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            //COOKIE existe
            if (!IlocalRepository.getCSRFToken().isEmpty()) {
                //SI EL COOKIE ES VÁLIDO
                User user_current = IautheticationRepository.getUser();
                if (user_current != null) {
                    oUtilClass.mostrarVentana(VerPacienteController.class, "VerPaciente");
                    cerrar();
                    return;
                }
            }
            oUtilClass.mostrarVentana(LoginController.class, "Login");
            cerrar();
        }));
        timeline.play();
    }


    @FXML
    public void cerrar() {
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.close();
    }
}
