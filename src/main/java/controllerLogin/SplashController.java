package controllerLogin;

import Repository.local.LocalRepository;
import Repository.remote.AutheticationRepository;
import RepositoryInterface.local.ILocalRepository;
import Util.PreferencesLocal;
import Util.UtilClass;
import controller.Paciente.VerPacienteController;
import global.Global;
import global.HeaderHttp;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class SplashController implements Initializable {
    @FXML
    AnchorPane ap;
    UtilClass oUtilClass = new UtilClass();
    AutheticationRepository autheticationRepository = new AutheticationRepository();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validateSession();
    }



    private void validateSession(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            if (!PreferencesLocal.prefs.get(HeaderHttp.CSRFToken,"empty").equals("empty")) {
                oUtilClass.mostrarVentana(VerPacienteController.class, "VerPaciente");
                autheticationRepository.getUser();
            }
            else{
                oUtilClass.mostrarVentana(LoginController.class, "Login");
            }
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
