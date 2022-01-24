/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller.emergente;

import Util.FileImagUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class AlertController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    HBox hboxAlert;
    @FXML
    Button jbtnOk;
    @FXML
    ImageView jimg;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imag = new Image(getClass().getResource("/imagenes/geminis1.jpg").toExternalForm());
        jimg.setImage(imag);

        jbtnOk.setStyle("-fx-background-color : #ffffff");
        hboxAlert.setStyle("-fx-background-color : #ffffff");
        // TODO
    }

    public static void Mostrar() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();//creando la base vacía
        AnchorPane ap = loader.load(AlertController.class.getResource("/fxml/Alert.fxml"));
        Scene scene = new Scene(ap);
        stage.initStyle(StageStyle.UNDECORATED);
        //stage.resizableProperty().setValue(false);
        stage.setTitle("Guardando");
        stage.setScene(scene);

        stage.show();
    }

    @FXML
    void cambiar() {
        Image imag = new Image(getClass().getResource("../imagenes/geminis1.jpg").toString());

        jimg.setImage(imag);
    }

    FileChooser filechooserImagPerfil = new FileChooser();

    @FXML

    void test(ActionEvent event) throws FileNotFoundException, IOException {
        FileImagUtil oFileUtil = new FileImagUtil("user.home","holi");
        File fileImag = oFileUtil.buscarImagen();
        if (fileImag != null) {
            jimg.setImage(new Image(fileImag.getAbsolutePath()));
        }

    }

}
