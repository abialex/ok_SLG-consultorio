/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerCita;

import Entidades.Cita;
import Entidades.Paciente;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class CitaModificarController implements Initializable {

    @FXML
    private AnchorPane ap;
     @FXML
    private JFXTextField jtfDoctor;

    @FXML
    private JFXTextField jtfFecha;

    @FXML
    private JFXTextField jtfHora;

    @FXML
    private JFXTextField jtfminuto;

    @FXML
    private JFXTextField jtfPaciente;

    @FXML
    private JFXTextField jtfrazon;
    
    CitaVerController oCitaVerController;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void setController(CitaVerController odc) {
        this.oCitaVerController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    void setCita(Cita oCita) {
        jtfDoctor.setText(oCita.getDoctor().getPersona().getNombres_apellidos());
        jtfFecha.setText(oCita.getFechacita()+"");
        jtfHora.setText(oCita.getHoraatencion().getHora());
        jtfminuto.setText(oCita.getMinuto());
        jtfPaciente.setText(oCita.getPaciente().getPersona().getNombres_apellidos());
        jtfrazon.setText(oCita.getRazon());
        
    }
    
    @FXML
    void cerrar() {
        oCitaVerController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }
    
}
