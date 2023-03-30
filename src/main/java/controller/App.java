package controller;

import Util.JPAUtil;
import java.io.File;
import Util.UtilClass;
import controllerLogin.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import javax.persistence.EntityManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private double x = 0;
    private double y = 0;
    LoginController ocControlller;
    public static Stage stagePrincpal;
    UtilClass oUtilClass = new UtilClass();

    @Override
    public void start(Stage stage) throws IOException {
        stagePrincpal = stage;
        ocControlller = (LoginController) oUtilClass.mostrarVentana(LoginController.class,"Login",stage);
        ocControlller.validarWithCookie();
        ocControlller.setStagePrincipall(stage);

    }
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    static void CrearArchivos(){
        File carpetaImages = new File("Archivos paciente");
        if (!carpetaImages.exists()) {
            carpetaImages.mkdirs();
        }
        File carpetaPdf = new File("Pdf");
        if (!carpetaPdf.exists()) {
            carpetaPdf.mkdirs();
        }
    }

}