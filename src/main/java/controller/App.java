package controller;

import Util.JPAUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.stage.StageStyle;
import javax.persistence.EntityManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static EntityManager jpa= JPAUtil.getEntityManagerFactory().createEntityManager();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("RegistrarPaciente"));
        scene.getStylesheets().add(getClass().getResource("/css/bootstrap3.css").toExternalForm());
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
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

}