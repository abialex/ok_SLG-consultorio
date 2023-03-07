/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Detalle_Presupuesto;
import Util.UtilClass;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class PresupuestoModificarController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfDetallePresupuesto;

    @FXML
    private JFXTextField jtfMonto;

    @FXML
    private JFXTextField jtfCantidad;

    PresupuestoVerController oPresupuestoVerController;
    Detalle_Presupuesto oDetalle_presupuesto;
    AlertController oAlertController = new AlertController();
    UtilClass oUtilClass = new UtilClass();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initRestricciones();
    }

    void setDetallePresupuesto(Detalle_Presupuesto opresupuesto) {
        this.oDetalle_presupuesto = opresupuesto;
        jtfDetallePresupuesto.setText(opresupuesto.getDescripcion());
        jtfMonto.setText(((int) opresupuesto.getMonto()) + "");
        jtfCantidad.setText(opresupuesto.getCantidad() + "");
    }

    void setController(PresupuestoVerController odc) {
        this.oPresupuestoVerController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    void initRestricciones() {
        //jtfDetallePresupuesto.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloLetras(event));
        jtfMonto.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros8(event));
        jtfCantidad.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros8(event));

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
    void modificar() {
        if (isCompleto()) {
            int cant_inicial=oDetalle_presupuesto.getMonto()*oDetalle_presupuesto.getCantidad();
            int cant_modificada=Integer.parseInt(jtfMonto.getText())*Integer.parseInt(jtfCantidad.getText());
            oPresupuestoVerController.oPresupuesto.setMonto_total(oPresupuestoVerController.oPresupuesto.getPresupuesto().getMonto_total()-cant_inicial+cant_modificada);
            oDetalle_presupuesto.setDescripcion(jtfDetallePresupuesto.getText());
            oDetalle_presupuesto.setMonto(Integer.parseInt(jtfMonto.getText()));
            oDetalle_presupuesto.setCantidad(Integer.parseInt(jtfCantidad.getText()));
            oPresupuestoVerController.initTable();
            oPresupuestoVerController.updateMontoAviso();
            //oPresupuestoVerController.modificar_detalle_presupuesto(oDetalle_presupuesto);
            oUtilClass.mostrar_alerta_success("exito", "sub presupuesto Modificado");
            cerrar();

        } else {
            oUtilClass.mostrar_alerta_error("error", "Llene los campos en blanco");
        }
    }

    boolean isCompleto() {
        boolean aux = true;

        if (jtfDetallePresupuesto.getText().trim().length() == 0) {
            jtfDetallePresupuesto.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfDetallePresupuesto.setStyle("");
        }

        if (jtfCantidad.getText().trim().length() == 0) {
            jtfCantidad.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfCantidad.setStyle("");
        }

        if (jtfMonto.getText().trim().length() == 0) {
            jtfMonto.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfMonto.setStyle("");
        }

        return aux;
    }

    @FXML
    void cerrar() {
        oPresupuestoVerController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();//cerrando la ventanada anterior
    }

}
