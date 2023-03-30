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
    UtilClass oUtilClass = new UtilClass();

    @Override
    public void start(Stage stage)  {
        ocControlller = (LoginController) oUtilClass.mostrarVentana(LoginController.class,"Login",stage);
        ocControlller.validarWithCookie();
    }
    public static void main(String[] args) {
        launch();
    }


}