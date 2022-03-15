/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package emergente;

import Entidades.Persona;
import controller.RegistrarPacienteController;
import controller.VerPacienteController;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    
    VerPacienteController oVerPacienteController;
    Persona oPersona;
    int index;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void setMensaje(String mensaje) {
        lblmensaje.setText(mensaje);
    }
    
    public void setController(VerPacienteController odc) {
        this.oVerPacienteController = odc;
    }
    public void setCartaIndex(Persona oCarta, int index) {
        this.index = index;
        this.oPersona = oCarta;
    }

    @FXML
    void eliminar() {
        oVerPacienteController.eliminar(oPersona, index);
        cerrar();
    }

    @FXML
    void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();
    }

}
