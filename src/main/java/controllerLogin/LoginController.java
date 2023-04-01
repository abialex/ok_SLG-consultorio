/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerLogin;


import Entidades.User;
import Repository.remote.AutheticationRepository;
import RepositoryInterface.remote.IAutheticationRepository;
import Util.UtilClass;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.App;
import controller.Paciente.VerPacienteController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class LoginController implements Initializable {
    @FXML
    private AnchorPane ap;
    UtilClass oUtilClass = new UtilClass();
    @FXML
    private JFXTextField jtfNickname;
    @FXML
    private JFXPasswordField jtf_password;
    @FXML
    private Label lblMensaje, lbl_password_show;
    VerPacienteController oVerPacienteController;
    IAutheticationRepository autheticationRepositoryI = new AutheticationRepository();
    boolean visible = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbl_password_show.setVisible(false);
    }

    @FXML
    void submit() {
        if (!isCompleto()) {
            lblMensaje.setText("llene los campos");
            return;
        }
        User user= autheticationRepositoryI.login(jtfNickname.getText(),jtf_password.getText());
        if(user==null){
            lblMensaje.setText("Datos incorrectos");
            return;
        }
        oVerPacienteController = (VerPacienteController) oUtilClass.mostrarVentana(VerPacienteController.class, "VerPaciente");
        cerrar();
        }
    private boolean isCompleto() {
        boolean aux = true;
        if (jtfNickname.getText().isEmpty()) {
            jtfNickname.setStyle("-fx-border-color:red");
            aux = false;
        } else {
            jtfNickname.setStyle("");
        }
        if (jtf_password.getText().isEmpty()) {
            jtf_password.setStyle("-fx-border-color:red");
            aux = false;
        } else {
            jtf_password.setStyle("");
        }
        return aux;
    }

    public void stop() {
        oUtilClass.ejecutarMetodo(oVerPacienteController, "stop");
    }

    @FXML
    private void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();

    }

    @FXML
    private void passwordFieldTyped() {
        lbl_password_show.setText(jtf_password.getText());

    }
    @FXML
    private void changue_visible_password(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        if(visible) {
            imag.setImage(new Image(App.class.getResource("/imagenes/visible-1.png").toExternalForm()));
            lbl_password_show.setVisible(false);
            visible = false;
        }
        else {
            imag.setImage(new Image(getClass().getResource("/imagenes/visible-2.png").toExternalForm()));
            lbl_password_show.setVisible(true);
            visible = true;
        }
    }

}
