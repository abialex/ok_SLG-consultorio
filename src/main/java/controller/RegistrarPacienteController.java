/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Doctor;
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
import javafx.stage.WindowEvent;

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
    Accordion accordion, accordion2;
    @FXML
    TitledPane tpAnamnesis, tpEnfermedades;

    //I. Amnemesis
    @FXML
    private JFXTextField jtfNombresyApellidos, jtfDni, jtfTelefono;
    @FXML
    private JFXComboBox<String> jcbsexo;
    @FXML
    private JFXTextField jtfDia, jtfMes, jtfanio;
    @FXML
    private JFXComboBox<String> jcbocupacion;
    @FXML
    private JFXTextField jtflugarprocedencia, jtfDomicilio;
    @FXML
    private JFXTextArea jtaConsulta;
    @FXML
    private JFXTextField jtfInformeradiografico;
    @FXML
    private JFXTextField jtfemergenciaNombre, jtfemergenciaParentesco, jtfemergenciatelefono;
    @FXML
    private JFXTextField jtftutornombre, jtftutordni, jtftutortelefono;
    @FXML
    private JFXComboBox<Doctor> jcbDoctor;

    //II. Enfermedad actual
    @FXML
    private JFXTextField jtfsintomasEnfermedadActual, jtftiempoEnfermedadActual;

    //III. Antecedentes
    @FXML
    private JFXCheckBox checkalergia, checkfiebrereumatica, checkanemia, checkdiabetes, checktuberculosis, checkhepatitis,
            checkinfeccionveneria, checkenfermedadcardiaca, checkgastritis, checkepilepsia, checkdolordepecho, checkneuralgia,
            checkenfermedaddelapiel, checkenfermedadrenal, checkhipertensionarterial;
    @FXML
    private JFXTextField jtfotrasEnfermedades, jtfAlergapregunta, jtfantecedentesFamiliares;
    @FXML
    private JFXCheckBox checkpreguntamujer1, checkpreguntamujer2;
    @FXML
    private JFXCheckBox checkpregunta1, checkpregunta2, checkpregunta3, checkpregunta4;
    @FXML
    private JFXTextField jtfpregunta1, jtfpregunta2, jtfpregunta3, jtfpregunta4, jtfPreguntamujer;

    //IV. Exploración fisica
    @FXML
    private JFXTextField jtfsignosvitales, jtfsaturacionoxigeno;
    @FXML
    private JFXTextField jtfPA, jtfFC, jtftemperatura, jtfFR;
    @FXML
    private JFXTextField jtfexamenclinicogeneral, jtfexamenclinicoodontoestomatolgico;

    //V. Diagnostico
    @FXML
    private JFXTextArea jtaDiagCIE10;

    //VI. Plan de tratamiento
    @FXML
    private JFXTextArea jtfrecomendaciones;

    //VII. Pronostico
    @FXML
    private JFXTextArea jtapronostico, jtaAltapaciente;

    List<CheckBox> listcheck = new ArrayList<>();
    AlertController oAlert = new AlertController();
    VerPacienteController oVerPacienteController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> OCUPACION = FXCollections.observableArrayList("ESTUDIANTE", "UNIVERSITARIO", "TRABAJADOR");
        ObservableList<String> SEXO = FXCollections.observableArrayList("VARÓN", "MUJER");
        jcbocupacion.setItems(OCUPACION);
        jcbsexo.setItems(SEXO);
        accordion.setExpandedPane(tpAnamnesis);
        accordion2.setExpandedPane(tpEnfermedades);
        asignar();
        initRestricciones();
        cargarDoctor();
    }

    void cargarDoctor() {
        List<Doctor> listDoctorG = App.jpa.createQuery("select p from Doctor p where flag = false and activo = true").getResultList();
        ObservableList<Doctor> listDoctor = FXCollections.observableArrayList();
        for (Doctor odoct : listDoctorG) {
            listDoctor.add(odoct);
        }
        jcbDoctor.setItems(listDoctor);
    }

    @FXML
    void GuardarPaciente(ActionEvent evt) throws IOException {
        if (isCompleto()) {
            LocalDate fechaNacimiento = LocalDate.of(
                    Integer.parseInt(jtfanio.getText().trim()),
                    Integer.parseInt(jtfMes.getText().trim()),
                    Integer.parseInt(jtfDia.getText().trim()));

            Persona opersona = new Persona(
                    jtfNombresyApellidos.getText().trim(),
                    jcbsexo.getSelectionModel().getSelectedItem(),
                    jtfDomicilio.getText().trim(),
                    jtfDni.getText().trim(),
                    fechaNacimiento,
                    jtflugarprocedencia.getText().trim(),
                    jcbocupacion.getSelectionModel().getSelectedItem(),
                    jtfTelefono.getText().trim(),
                    0
            );
            opersona.setTutorDni(jtftutordni.getText().trim());
            opersona.setTutorNombre(jtftutornombre.getText().trim());
            opersona.setTutorTelefono(jtftutortelefono.getText().trim());

            Paciente opaciente = new Paciente(
                    opersona,
                    jtfsintomasEnfermedadActual.getText().trim(),
                    jtftiempoEnfermedadActual.getText().trim(),
                    jtfotrasEnfermedades.getText().trim(),
                    jtfantecedentesFamiliares.getText().trim());
            opaciente.setEmergenciaNombre(jtfemergenciaNombre.getText().trim());
            opaciente.setEmergenciaParentesco(jtfemergenciaParentesco.getText().trim());
            opaciente.setEmergenciaTelefono(jtfemergenciatelefono.getText().trim());

            List<Paciente_Enfermedad> Lista_enfermedadesPaciente = Paciente_relacionar_enfermedad(listcheck, opaciente);
            List<Paciente_Pregunta> Lista_preguntasPaciente = Paciente_relacionar_pregunta(listcheck, opaciente);

            Historia_clinica ohistoria = new Historia_clinica(
                    opaciente,
                    jcbDoctor.getSelectionModel().getSelectedItem(),
                    jtfsignosvitales.getText().trim(),
                    jtfsaturacionoxigeno.getText().trim(),
                    jtfPA.getText().trim(),
                    jtfFC.getText().trim(),
                    jtftemperatura.getText().trim(),
                    jtfFR.getText(),
                    jtfexamenclinicogeneral.getText().trim(),
                    jtfexamenclinicoodontoestomatolgico.getText().trim(),
                    jtaDiagCIE10.getText().trim(),
                    jtfrecomendaciones.getText().trim(),
                    jtapronostico.getText().trim(),
                    jtaAltapaciente.getText().trim(),
                    jtaConsulta.getText().trim(),
                    LocalDate.now(),
                    LocalDate.now(),
                    jtfInformeradiografico.getText().trim());
            //GuardarPaciente
            App.jpa.getTransaction().begin();
            App.jpa.persist(opersona);

            App.jpa.persist(opaciente);

            for (Paciente_Enfermedad paciente_Enfermedad : Lista_enfermedadesPaciente) {
                App.jpa.persist(paciente_Enfermedad);
            }
            for (Paciente_Pregunta paciente_Pregunta : Lista_preguntasPaciente) {
                App.jpa.persist(paciente_Pregunta);
            }
            App.jpa.persist(ohistoria);
            App.jpa.getTransaction().commit();
            oVerPacienteController.updateListPersona();
            oVerPacienteController.selectAgregado();
            File carpetaImages = new File("Archivos paciente/" + opersona.getNombres_apellidos());
            if (!carpetaImages.exists()) {
                carpetaImages.mkdirs();
            }
            cerrar();
        }
    }

    List<Paciente_Enfermedad> Paciente_relacionar_enfermedad(List<CheckBox> listC, Paciente opaciente) {
        List<Enfermedad> list_enfermedad = App.jpa.createQuery("select p from Enfermedad p ").getResultList();
        List<Paciente_Enfermedad> list_enfermedades_paciente = new ArrayList<Paciente_Enfermedad>();
        for (CheckBox checkBox : listC) {
            for (Enfermedad enfermedad : list_enfermedad) {
                if (checkBox.getUserData().toString().equals(enfermedad.getNombre())) {
                    String Alergia = "";
                    if (enfermedad.getNombre().equals("Alergia")) {
                        Alergia = jtfAlergapregunta.getText().trim();
                    }
                    list_enfermedades_paciente.add(new Paciente_Enfermedad(opaciente, enfermedad, Alergia));
                }
            }
        }
        for (Paciente_Enfermedad paciente_Enfermedad : list_enfermedades_paciente) {
            System.out.println(paciente_Enfermedad.getEnfermedad().getNombre());
        }
        return list_enfermedades_paciente;
    }

    List<Paciente_Pregunta> Paciente_relacionar_pregunta(List<CheckBox> listcb, Paciente opaciente) {
        List<Pregunta> list_pregunta = App.jpa.createQuery("select p from Pregunta p ").getResultList();
        List<Paciente_Pregunta> list_pregunta_paciente = new ArrayList<Paciente_Pregunta>();

        for (CheckBox checkBox : listcb) {
            for (Pregunta pregunta : list_pregunta) {
                boolean isMujer = opaciente.getPersona().getSexo().equals("MUJER");
                if (checkBox.getUserData().toString().equals(pregunta.getTextopregunta())) {
                    String pgt = "";
                    if (checkBox.getUserData().toString().equals("¿Reacciona  anormalmente a algún medicamento?")) {
                        pgt = jtfpregunta1.getText().trim();
                        isMujer = false;
                    } else if (checkBox.getUserData().toString().equals("¿Le han realizado alguna intervención quirúrjica?")) {
                        pgt = jtfpregunta2.getText().trim();
                        isMujer = false;
                    } else if (checkBox.getUserData().toString().equals("¿Está usted tomando algún medicamento?")) {
                        pgt = jtfpregunta3.getText().trim();
                        isMujer = false;
                    } else if (checkBox.getUserData().toString().equals("¿Es alérgico a la anestesia dental?")) {
                        pgt = jtfpregunta4.getText().trim();
                        isMujer = false;
                    } else if (checkBox.getUserData().toString().equals("¿Está usted embarazada?")) {
                        pgt = jtfPreguntamujer.getText().trim();
                    }
                    list_pregunta_paciente.add(new Paciente_Pregunta(opaciente, pregunta, pgt, isMujer));
                }
            }
        }
        return list_pregunta_paciente;
    }

    @FXML
    void cuadrarCheckbox(ActionEvent o) {
        CheckBox ch = (CheckBox) o.getSource();
        if (ch.isSelected()) {
            listcheck.add(ch);
        } else {
            listcheck.remove(ch);
        }
        if (ch.getUserData().toString().equals("Alergia")) {
            unlockecdAlergia();
        }
    }

    void setController(VerPacienteController aThis) {
        this.oVerPacienteController = aThis;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    void asignar() {
        checkalergia.setUserData("Alergia");
        checkfiebrereumatica.setUserData("Fiebre reumática");
        checkanemia.setUserData("Anemia");
        checkdiabetes.setUserData("Diabetes");
        checktuberculosis.setUserData("Tuberculosis");
        checkhepatitis.setUserData("Hepatitis");
        checkinfeccionveneria.setUserData("Infección veneria");
        checkenfermedadcardiaca.setUserData("Enfermedad cardiaca");
        checkgastritis.setUserData("Gastritis");
        checkepilepsia.setUserData("Epilepsia");
        checkdolordepecho.setUserData("Dolor de pecho");
        checkneuralgia.setUserData("Neuralgia");
        checkenfermedaddelapiel.setUserData("Enfermedad de la piel");
        checkenfermedadrenal.setUserData("Enfermedad renal");
        checkhipertensionarterial.setUserData("Hipertensión Arterial");
        checkpregunta1.setUserData("¿Reacciona  anormalmente a algún medicamento?");
        checkpregunta2.setUserData("¿Está usted tomando algún medicamento?");
        checkpregunta3.setUserData("¿Le han realizado alguna intervención quirúrjica?");
        checkpregunta4.setUserData("¿Es alérgico a la anestesia dental?");
        checkpreguntamujer1.setUserData("¿Está usted embarazada?");
        checkpreguntamujer2.setUserData("¿Está dando de lactar?");
    }

    void initRestricciones() {
        jtfNombresyApellidos.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloLetras(event));
        jtfTelefono.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEntero(event));
        jtfDni.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros8(event));
        jtfDia.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros2(event));
        jtfMes.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros2(event));
        jtfanio.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros4(event));

        jtfemergenciaNombre.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloLetras(event));
        jtfemergenciaParentesco.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloLetras(event));
        jtfemergenciatelefono.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEntero(event));

        jtftutornombre.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloLetras(event));
        jtftutordni.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros8(event));
        jtftutortelefono.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEntero(event));
    }

    void SoloNumerosEntero(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
    }

    void SoloNumerosEnteros2(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 2) {
            event.consume();
        }
    }

    void SoloNumerosEnteros4(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 4) {
            event.consume();
        }
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

    void SoloLetras(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (Character.isDigit(key)) {
            event.consume();
        }
    }

    /*----------Metodos de ventana---------------*/
    void unlockecdAlergia() {
        if (jtfAlergapregunta.isDisable()) {
            jtfAlergapregunta.setDisable(false);
        } else {
            jtfAlergapregunta.setDisable(true);
        }
    }

    @FXML
    void unlockecdPregunta1(ActionEvent event) {
        if (jtfpregunta1.isDisable()) {
            listcheck.add((CheckBox) event.getSource());
            jtfpregunta1.setDisable(false);
        } else {
            listcheck.remove((CheckBox) event.getSource());
            jtfpregunta1.setDisable(true);
        }
    }

    @FXML
    void unlockecdPregunta2(ActionEvent event) {
        if (jtfpregunta2.isDisable()) {
            listcheck.add((CheckBox) event.getSource());
            jtfpregunta2.setDisable(false);
        } else {
            listcheck.remove((CheckBox) event.getSource());
            jtfpregunta2.setDisable(true);
        }
    }

    @FXML
    void unlockecdPregunta3(ActionEvent event) {
        listcheck.add((CheckBox) event.getSource());
        if (jtfpregunta3.isDisable()) {
            listcheck.add((CheckBox) event.getSource());
            jtfpregunta3.setDisable(false);
        } else {
            listcheck.remove((CheckBox) event.getSource());
            jtfpregunta3.setDisable(true);
        }
    }

    @FXML
    void unlockecdPregunta4(ActionEvent event) {
        listcheck.add((CheckBox) event.getSource());
        if (jtfpregunta4.isDisable()) {
            listcheck.add((CheckBox) event.getSource());
            jtfpregunta4.setDisable(false);
        } else {
            listcheck.remove((CheckBox) event.getSource());
            jtfpregunta4.setDisable(true);
        }
    }

    @FXML
    void unlockecdPreguntamujer1(ActionEvent event) {
        if (jtfPreguntamujer.isDisable()) {
            listcheck.add((CheckBox) event.getSource());
            jtfPreguntamujer.setDisable(false);
        } else {
            listcheck.remove((CheckBox) event.getSource());
            jtfPreguntamujer.setDisable(true);
        }
    }

    /*------Fin Metodos de ventana---------------*/
    boolean isCompleto() throws IOException {
        boolean aux = true;
        if (jtfNombresyApellidos.getText().trim().length() == 0) {
            jtfNombresyApellidos.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfNombresyApellidos.setStyle("");
        }

        if (jtfDni.getText().trim().length() == 0) {
            jtfDni.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfDni.setStyle("");
        }

        if (jtfTelefono.getText().trim().length() == 0) {
            jtfTelefono.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfTelefono.setStyle("");
        }

        if (jcbsexo.getSelectionModel().getSelectedItem() == null) {
            jcbsexo.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jcbsexo.setStyle("");
        }

        if (jcbocupacion.getSelectionModel().getSelectedItem() == null) {
            jcbocupacion.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jcbocupacion.setStyle("");
        }

        if (jtflugarprocedencia.getText().trim().length() == 0) {
            jtflugarprocedencia.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtflugarprocedencia.setStyle("");
        }

        if (jtfDomicilio.getText().trim().length() == 0) {
            jtfDomicilio.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfDomicilio.setStyle("");
        }

        if (jtaConsulta.getText().trim().length() == 0) {
            jtaConsulta.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtaConsulta.setStyle("");
        }

        if (jcbDoctor.getSelectionModel().getSelectedItem() == null) {
            jcbDoctor.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jcbDoctor.setStyle("");
        }

        boolean auxfecha = true;
        if (jtfDia.getText().trim().length() == 0) {
            jtfDia.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtfDia.setStyle("");
        }

        if (jtfMes.getText().trim().length() == 0) {
            jtfMes.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtfMes.setStyle("");
        }

        if (jtfanio.getText().trim().length() == 0) {
            jtfanio.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtfanio.setStyle("");
        }

        if (jtfInformeradiografico.getText().trim().length() == 0) {
            jtfInformeradiografico.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtfInformeradiografico.setStyle("");
        }

        boolean auxfechaCorrect = isfechavalid(auxfecha);
        if (!aux || !auxfecha) {
            oAlert.Mostrar("error", "Llene los cuadros en rojo");
        }
        return aux && auxfecha && auxfechaCorrect;
    }

    boolean isfechavalid(boolean aux) throws IOException {
        try {
            if (aux) {
                LocalDate.of(Integer.parseInt(jtfanio.getText().trim()), Integer.parseInt(jtfMes.getText().trim()), Integer.parseInt(jtfDia.getText().trim()));
            }
        } catch (Exception e) {
            aux = false;
            oAlert.Mostrar("warning", "ingrese una fecha válida");
        }
        return aux;
    }

    @FXML
    void cerrar() {
        oVerPacienteController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }
}
