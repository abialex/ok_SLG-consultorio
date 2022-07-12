/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Tratamiento;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.collections.FXCollections;
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

    Tratamiento oTratamiento;
    CajaVerController oCajaVerController;
    AlertController oAlertController = new AlertController();
    float resto = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initRestricciones();

    }

    public void setTratamiento(Tratamiento get, float resto) {
        this.oTratamiento = get;
        this.resto = resto+oTratamiento.getMonto();
        System.out.println(resto+"s");
        jtfTratamiento.setText(get.getTratamiento());
        jtfMonto.setText(get.getMonto() + "");
    }

    void setController(CajaVerController odc) {
        this.oCajaVerController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    @FXML
    void modificar() {
        if (isCompleto()) {
            if (resto - Integer.parseInt(jtfMonto.getText()) >= 0) {
                oTratamiento.setTratamiento(jtfTratamiento.getText());
                oTratamiento.setMonto(Integer.parseInt(jtfMonto.getText()));
                App.jpa.getTransaction().begin();
                App.jpa.persist(oTratamiento);
                App.jpa.getTransaction().commit();
                cerrar();
                oCajaVerController.updateListaTratamiento();
                oCajaVerController.initTable();
                oAlertController.Mostrar("successful", "Modificado");
            } else {
                oAlertController.Mostrar("error", "Se pasó del precio");
            }
        } else {
            oAlertController.Mostrar("error", "Llene los espacios en blanco");
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
        System.out.println("terminando");
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
