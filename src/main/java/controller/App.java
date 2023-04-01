package controller;

import Repository.remote.AutheticationRepository;

import Util.PreferencesLocal;
import Util.UtilClass;
import controllerLogin.LoginController;
import controllerLogin.SplashController;
import global.HeaderHttp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    LoginController ocControlller;
    UtilClass oUtilClass = new UtilClass();
    AutheticationRepository autheticationRepository = new AutheticationRepository();

    @Override
    public void start(Stage stage)  {
        oUtilClass.mostrarVentana(SplashController.class, "Splash");

        //ocControlller.validarWithCookie();
        //autheticationRepository.getUser();
    }
    private void validateSession(){

    }
    public static void main(String[] args) {
        launch();
    }


}