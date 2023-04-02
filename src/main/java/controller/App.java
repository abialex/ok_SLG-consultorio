package controller;

import Util.UtilClass;
import controllerLogin.SplashController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    UtilClass oUtilClass = new UtilClass();
    @Override
    public void start(Stage stage)  {
        oUtilClass.mostrarVentana(SplashController.class, "Splash");
    }
    public static void main(String[] args) {
        launch();
    }


}