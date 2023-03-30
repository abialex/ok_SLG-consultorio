/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerLogin;


import Entidades.Persona;

import Entidades.User;
import Util.HttpMethods;
import Util.UtilClass;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import controller.VerPacienteController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class LoginController implements Initializable {
    //loguear
    //visualizar contraseña

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfNickname;

    @FXML
    private JFXPasswordField jtf_password;

    @FXML
    private Label lblMensaje, lbl_password_show;
    UtilClass oUtilClass = new UtilClass();
    HttpMethods http = new HttpMethods();
    Gson json = new Gson();
    VerPacienteController oControllerVista;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbl_password_show.setVisible(false);

    }

    public void validarWithCookie() {
        HttpResponse<String> response = http.loguear(jtfNickname.getText(), jtf_password.getText());

        if (response != null) {
            if (response.statusCode() == 226) {
                User ousuario = json.fromJson(response.body(), User.class);
                lblMensaje.setText("Ya está logueado");
                ingresar(ousuario);
            }
        }
        else {
            lblMensaje.setText("no hay conexión al servidor");
        }
    }

    @FXML
    void validar() {
        if (isCompleto()) {
            HttpResponse<String> response = http.loguear(jtfNickname.getText(), jtf_password.getText());
            if (response != null) {

                switch (response.statusCode()) {
                    case 200 :
                        User osuario = json.fromJson(response.body(), User.class);
                        /* validar que exista el header
                         validar que haya mas de 43 caracteres */
                        if (response.headers().allValues("set-cookie").size() > 1) {
                            http.setCSRFToken(response.headers().allValues("set-cookie").get(0).substring(10, 42));
                            http.setCokkie(response.headers().allValues("set-cookie").get(0).substring(0, 43)
                                    + " " + response.headers().allValues("set-cookie").get(1).substring(0, 42));
                        }
                        //actualizando csfr y cookie
                        http = new HttpMethods();
                        lblMensaje.setText("Bienvenido " + osuario.getPersona().getNombres() + ".");
                        ingresar(osuario);
                        break;

                    case 226:
                        lblMensaje.setText("Bienvenido");

                        break;
                    case 406:
                        lblMensaje.setText("Credenciales incorrectos");
                        break;
                    default:
                        lblMensaje.setText("Sucedio otro error N° " + response.statusCode());
                        break;

                }
            } else {
                lblMensaje.setText("no hay conexión al servidor");
            }
        } else {
            lblMensaje.setText("llene los campos");
        }
    }
    void ingresar(User osuario) {
        Stage stage = new Stage();
        oControllerVista = (VerPacienteController) oUtilClass.mostrarVentana(VerPacienteController.class, "VerPaciente", stage);
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
        oUtilClass.ejecutarMetodo(oControllerVista, "stop");
    }

    @FXML
    public void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();

    }

    boolean visible = false;

    @FXML
    void passwordFieldTyped(KeyEvent event) {
        lbl_password_show.setText(jtf_password.getText());

    }
    @FXML
    void changue_visible_password(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        if(visible) {
            imag.setImage(new Image(getClass().getResource("/imagenes/visible-1.png").toExternalForm()));
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
