/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller.Caja;

import Entidades.Doctor;
import Entidades.Tratamiento;
import Util.UtilClass;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class CajaModificarController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfTratamiento;

    @FXML
    private JFXTextField jtfMonto;
    
    @FXML
    private JFXComboBox<Doctor> jcb_doctor;
    Tratamiento oTratamiento;
    CajaVerController oCajaVerController;
    int resto = 0;
    UtilClass oUtilClass = new UtilClass();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initRestricciones();

    }

    public void setTratamiento(Tratamiento otratamiento, int resto, ObservableList<Doctor> list_doctor) {
        this.oTratamiento = otratamiento;
        this.resto = resto+oTratamiento.getMonto();
        jtfTratamiento.setText(otratamiento.getNombre());
        jtfMonto.setText(otratamiento.getMonto() + "");
        jcb_doctor.setItems(list_doctor);
        jcb_select_doctor(list_doctor,otratamiento.getDoctor());
    }

    void jcb_select_doctor(ObservableList<Doctor> list_doctor_sd, Doctor oDoctor){
        for(Doctor doctor : list_doctor_sd){
            if(doctor.getIddoctor()==(oDoctor.getIddoctor())){
                jcb_doctor.getSelectionModel().select(doctor);
            }
        }

    }

    void setController(CajaVerController odc) {
        this.oCajaVerController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    @FXML
    void modificar() {
        if (isCompleto()) {
            if (resto - Integer.parseInt(jtfMonto.getText()) >= 0) {
                oTratamiento.setNombre(jtfTratamiento.getText());
                oTratamiento.setMonto(Integer.parseInt(jtfMonto.getText()));
                oTratamiento.setDoctor(jcb_doctor.getSelectionModel().getSelectedItem());
                cerrar();
                oCajaVerController.initTable();
                oCajaVerController.updateMontoAviso();
                oUtilClass.mostrar_alerta_success("Exitoso", "Tratamiento Modificado");
            } else {
                oUtilClass.mostrar_alerta_error("Error", "Se excediò del presupuesto");
            }
        } else {
            oUtilClass.mostrar_alerta_warning("info","Llene todos los campos");
        }
    }

    void initRestricciones() {
        jtfMonto.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros8(event));
    }

    void SoloNumerosEnteros8(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 8) {
            event.consume();
        }
    }

    @FXML
    void cerrar() {
        oCajaVerController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();//cerrando la ventanada anterior
    }

    @FXML
    void hola() {
        System.out.println("terminò de cargar la pantalla");
    }

    boolean isCompleto() {
        boolean aux = true;

        if (jtfTratamiento.getText().trim().length() == 0) {
            jtfTratamiento.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfTratamiento.setStyle("");
        }

        if (jtfMonto.getText().trim().length() == 0) {
            jtfMonto.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfMonto.setStyle("");
        }

        return aux;
    }
}
