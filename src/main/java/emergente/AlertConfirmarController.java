/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package emergente;

import Entidades.Persona;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author UTIC
 */
public class AlertConfirmarController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    AnchorPane ap;
    @FXML
    Label lblmensaje;

    Object oObjectController;
    Persona oPersona;
    int index;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setMensaje(String mensaje) {
        lblmensaje.setText(mensaje);
    }

    public void setController(Object objectController) {
        this.oObjectController = objectController;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    @FXML
    void confirmar() {
        try {
            Class[] parametro = null;
            Object[] parametro2 = null;
            Method a = oObjectController.getClass().getDeclaredMethod("eliminar", parametro);
            a.invoke(oObjectController, parametro2);
            cerrar();
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(AlertConfirmarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void cerrar() {
        try {
            Class[] parametro = null;
            Object[] parametro2 = null;
            Method a = oObjectController.getClass().getDeclaredMethod("lockedPantalla", parametro);
            a.invoke(oObjectController, parametro2);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(AlertConfirmarController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ((Stage) ap.getScene().getWindow()).close();
    }

}
