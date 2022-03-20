/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Enfermedad;
import Entidades.Historia_clinica;
import Entidades.Paciente;
import Entidades.Paciente_Enfermedad;
import Entidades.Paciente_Pregunta;
import Entidades.Persona;
import Entidades.Pregunta;
import Util.FileImagUtil;
import Pdf.Historiaclinicapdf;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertController;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class RegistrarPacienteController implements Initializable {   
    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfNombresyApellidos;

    @FXML
    private JFXTextField jtflugarprocedencia;

    @FXML
    private JFXTextField jtfDni;

    @FXML
    private JFXTextField jtfTelefono;

    @FXML
    private JFXComboBox<?> jcbsexo;

    @FXML
    private JFXTextField jtfDia;

    @FXML
    private JFXTextField jtfMes;

    @FXML
    private JFXTextField jtfanio;

    @FXML
    private JFXComboBox<?> jcbocupacion;

    @FXML
    private JFXTextField jtfDomicilio;

    @FXML
    private JFXTextField jtfemergenciaParentesco1;

    @FXML
    private JFXTextField jtfemergenciatelefono1;

    @FXML
    private JFXTextField jtfemergenciaNombre1;

    @FXML
    private JFXTextField jtftutornombre1;

    @FXML
    private JFXTextField jtftutordni1;

    @FXML
    private JFXTextField jtftutortelefono1;

    @FXML
    private JFXTextField jtfsintomasEnfermedadActual;

    @FXML
    private JFXTextField jtftiempoEnfermedadActual;

    @FXML
    private JFXTextField jtfotrasEnfermedades;

    @FXML
    private JFXCheckBox checkfiebrereumatica;

    @FXML
    private JFXCheckBox checkanemia;

    @FXML
    private JFXCheckBox checkdiabetes;

    @FXML
    private JFXCheckBox checkalergia;

    @FXML
    private JFXCheckBox checktuberculosis;

    @FXML
    private JFXCheckBox checkhepatitis;

    @FXML
    private JFXCheckBox checkinfeccionveneria;

    @FXML
    private JFXCheckBox checkenfermedadcardiaca;

    @FXML
    private JFXCheckBox checkgastritis;

    @FXML
    private JFXCheckBox checkepilepsia;

    @FXML
    private JFXCheckBox checkdolordepecho;

    @FXML
    private JFXCheckBox checkneuralgia;

    @FXML
    private JFXCheckBox checkenfermedaddelapiel;

    @FXML
    private JFXCheckBox checkenfermedadrenal;

    @FXML
    private JFXCheckBox checkhipertensionarterial;

    @FXML
    private JFXTextField jtfantAque;

    @FXML
    private JFXCheckBox checkpreguntamujer1;

    @FXML
    private JFXCheckBox checkpreguntamujer2;

    @FXML
    private JFXTextField jtfantpreguntamujer1;

    @FXML
    private JFXCheckBox checkpregunta3;

    @FXML
    private JFXCheckBox checkpregunta1;

    @FXML
    private JFXCheckBox checkpregunta2;

    @FXML
    private JFXCheckBox checkpregunta4;

    @FXML
    private JFXTextField jtfantPregunta1;

    @FXML
    private JFXTextField jtfantPregunta3;

    @FXML
    private JFXTextField jtfantPregunta2;

    @FXML
    private JFXTextField jtfantPregunta4;

    @FXML
    private JFXTextField jtfantecedentesFamiliares;

    @FXML
    private JFXTextField jtfsignosvitales;

    @FXML
    private JFXTextField jtfsaturacionoxigeno;

    @FXML
    private JFXTextField jtfPA;

    @FXML
    private JFXTextField jtfFC;

    @FXML
    private JFXTextField jtftemperatura;

    @FXML
    private JFXTextField jtfFR;

    @FXML
    private JFXTextField jtfexamenclinicogeneral;

    @FXML
    private JFXTextField jtfexamenclinicoodontoestomatolgico;

    @FXML
    private JFXTextArea jtaDiagCIE10;

    @FXML
    private JFXTextArea jtaDiagPresentivo;

    @FXML
    private JFXTextArea jtaDiagDefinitivo;

    @FXML
    private JFXTextArea jtfrecomendaciones;

    @FXML
    private JFXTextArea jtapronostico;

    @FXML
    private JFXTextArea jtaAltapaciente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void setController(VerPacienteController aThis) {
    }

    @FXML
    void cuadrarCheckbox() {

    }

    @FXML
    void unlockecdPregunta1() {

    }

    @FXML
    void unlockecdPregunta2() {

    }

    @FXML
    void unlockecdPregunta3() {

    }

    @FXML
    void unlockecdPregunta4() {

    }

    @FXML
    void unlockecdPreguntamujer1() {

    }

    @FXML
    void test() {

        System.out.println(((Stage) ap.getScene().getWindow()).getHeight());
        System.out.println(((Stage) ap.getScene().getWindow()).getWidth());

    }

    @FXML
    void test1() {
        ((Stage) ap.getScene().getWindow()).setWidth(Integer.parseInt(jtfMes.getText()));
        ((Stage) ap.getScene().getWindow()).setHeight(Integer.parseInt(jtfDia.getText()));
    }

    @FXML
    void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();
    }
}
