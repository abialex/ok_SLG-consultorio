/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerCita;

import Entidades.Cita;
import Entidades.Doctor;
import Entidades.HoraAtencion;
import Entidades.Paciente;
import Entidades.Persona;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.App;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class CitaAgregarController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfDoctor;

    @FXML
    private JFXTextField jtfFecha;

    @FXML
    private JFXTextField jtfHora, jtfminuto;

    @FXML
    private JFXComboBox<Paciente> jcbPaciente;

    @FXML
    private JFXTextField jtfrazon;

    CitaVerController citaControol;
    HoraAtencion horaAtencion;
    Doctor oDoctor;
    LocalDate oFechaCita;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPaciente();
    }

    void cargarPaciente() {
        List<Paciente> olistPaciente = App.jpa.createQuery("select p from Paciente p").getResultList();
        ObservableList<Paciente> listpac = FXCollections.observableArrayList();
        for (Paciente opacient : olistPaciente) {
            listpac.add(opacient);
        }
        jcbPaciente.setItems(listpac);
    }

    void setController(CitaVerController odc) {
        this.citaControol = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    void setPersona(HoraAtencion oHora, Doctor doc, LocalDate oFecha) {
        this.horaAtencion = oHora;
        this.oDoctor = doc;
        this.oFechaCita = oFecha;
        jtfDoctor.setText(doc.getPersona().getNombres_apellidos());
        jtfFecha.setText(oFecha.toString());
        jtfHora.setText(oHora.getHora());
    }
    
    @FXML 
    void guardarCita(){
        Cita ocita=new Cita(oDoctor, jcbPaciente.getSelectionModel().getSelectedItem(), horaAtencion, oFechaCita, jtfrazon.getText(),jtfminuto.getText());
        App.jpa.getTransaction().begin();
        App.jpa.persist(ocita);
        App.jpa.getTransaction().commit();
        citaControol.initTable();
        cerrar();
    }

    @FXML
    void cerrar() {
        citaControol.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }

}
