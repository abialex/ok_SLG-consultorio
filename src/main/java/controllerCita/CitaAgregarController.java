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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private JFXTextField jtfrazon, jtfBuscar;

    @FXML
    private TableView<Persona> tablePersona;

    @FXML
    private TableColumn<Persona, String> columnNombres;

    CitaVerController citaControol;
    HoraAtencion horaAtencion;
    Doctor oDoctor;
    LocalDate oFechaCita;
    ObservableList<Persona> listPersona = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateListPersona();
        initTableView();
        tablePersona.setItems(listPersona);
    }

    @FXML
    void updateListPersona() {
        List<Persona> olistPerson = App.jpa.createQuery("select p from Persona p where (dni like " + "'" + jtfBuscar.getText() + "%'"
                + " or " + "nombres_apellidos like " + "'%" + jtfBuscar.getText() + "%') and flag = false and ocupacion <> 'DOCTOR' ").setMaxResults(5).getResultList();
        listPersona.clear();
        for (Persona ocarta : olistPerson) {
            listPersona.add(ocarta);
        }
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
    void guardarCita() {
        if (isComplete()) {
            Cita ocita = new Cita(oDoctor, tablePersona.getSelectionModel().getSelectedItem().getPaciente(), horaAtencion, oFechaCita, jtfrazon.getText(), jtfminuto.getText());
            App.jpa.getTransaction().begin();
            App.jpa.persist(ocita);
            App.jpa.getTransaction().commit();
            citaControol.initTable();
            cerrar();
        }
    }

    void initTableView() {
        columnNombres.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombres_apellidos"));
    }

    boolean isComplete() {
        boolean aux = true;
        if (jtfminuto.getText().trim().length() == 0) {
            jtfminuto.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfminuto.setStyle("");
        }

        if (jtfrazon.getText().trim().length() == 0) {
            jtfrazon.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfrazon.setStyle("");
        }

        if (tablePersona.getSelectionModel().getSelectedItem() == null) {
            tablePersona.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            tablePersona.setStyle("-fx-border-color: #337ab7");
        }
        return aux;
    }

    @FXML
    void cerrar() {
        citaControol.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }

}
