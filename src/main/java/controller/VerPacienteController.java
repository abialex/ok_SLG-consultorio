/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Persona;
import com.jfoenix.controls.JFXTextField;
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

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class VerPacienteController implements Initializable {

    @FXML
    AnchorPane ap;

    @FXML
    private JFXTextField jtfbuscar;

    @FXML
    private TableView<Persona> tablePersona;

    @FXML
    private TableColumn<Persona, String> tableDni, tableNombre, tableTelefono, tableDomicilio, tableOcupacion, tableOpcion;

    @FXML
    private TableColumn<Persona, LocalDate> tableAdulto;

    RegistrarPacienteController oRegistrarPacienteController;
    ObservableList<Persona> listPersona = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateListaComprobante();
        initTableView();
        tablePersona.setItems(listPersona);
        // TODO
    }

    void setController(RegistrarPacienteController aThis) {
        this.oRegistrarPacienteController = aThis;
    }
    
    @FXML
    void updateListaComprobante() {
        List<Persona> olistPerson = App.jpa.createQuery("select p from Persona p order by idpersona DESC").setMaxResults(10).getResultList();
        listPersona.clear();
        for (Persona ocarta : olistPerson) {
            listPersona.add(ocarta);
        }
    }

    @FXML
    void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();//cerrando la ventanada anterior
    }
    
    void initTableView(){
        tableDni.setCellValueFactory(new PropertyValueFactory<Persona, String>("dni"));
        tableNombre.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombres_apellidos"));
        tableTelefono.setCellValueFactory(new PropertyValueFactory<Persona, String>("telefono"));
        tableDomicilio.setCellValueFactory(new PropertyValueFactory<Persona, String>("domicilio"));
        tableOcupacion.setCellValueFactory(new PropertyValueFactory<Persona, String>("ocupacion"));
        tableAdulto.setCellValueFactory(new PropertyValueFactory<Persona, LocalDate>("fechanacimiento"));
        tableOpcion.setCellValueFactory(new PropertyValueFactory<Persona, String>("dni"));  
    }

}
