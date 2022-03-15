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
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class RegistrarPacienteController implements Initializable {
    
    
    //Atributos de la ventana
    @FXML Accordion accordion;
    @FXML TitledPane tpAnamnesis;
    @FXML TitledPane tpAnamnesisAct;
    @FXML JFXDatePicker dtpicker;
    @FXML Button jbtnGuardar;
    @FXML JFXTextField jtfbuscar;
    @FXML Button jbtnbuscar;
    @FXML TextField jtfbuscarAct;
    @FXML Label jlblnombresyapellidos;
    @FXML Label jlbldni;
    @FXML Button jbtnimprimir;
    /*--------------Atributos---------------*/
    //Anamnesis
    @FXML TextField jtfNombresyApellidos;
    @FXML TextField jtfDomicilio;
    @FXML TextField jtfDni;
    @FXML TextField jtfDia;
    @FXML TextField jtfMes;
    @FXML TextField jtfanio;
    @FXML TextField jtfTelefono;
    @FXML TextField jtflugarprocedencia;
    @FXML ComboBox<String> jcbocupacion;
    @FXML ComboBox<String> jcbsexo;
    @FXML TextField jtfmotivoconsulta;
    @FXML TextField jtftutornombre, jtftutordni, jtftutortelefono;
    @FXML TextField jtfemergenciaNombre, jtfemergenciaParentesco, jtfemergenciatelefono;
    
    //Enfermedad actual
    @FXML TextField jtfsintomasEnfermedadActual;
    @FXML TextField jtftiempoEnfermedadActual;
    
    //Antecedentes
    @FXML CheckBox checkalergia;;@FXML TextField jtfantAque;
    @FXML CheckBox checkfiebrereumatica;
    @FXML CheckBox checkanemia;
    @FXML CheckBox checkdiabetes;
    @FXML CheckBox checktuberculosis;
    @FXML CheckBox checkhepatitis;
    @FXML CheckBox checkinfeccionveneria;
    @FXML CheckBox checkenfermedadcardiaca;
    @FXML CheckBox checkgastritis;
    @FXML CheckBox checkepilepsia;
    @FXML CheckBox checkdolordepecho;
    @FXML CheckBox checkneuralgia;
    @FXML CheckBox checkenfermedaddelapiel;
    @FXML CheckBox checkenfermedadrenal;
    @FXML CheckBox checkhipertensionarterial;
        
    @FXML TextField jtfantecedentesFamiliares;
    @FXML TextField jtfotrasEnfermedades;
    //EXPLORACIÓN FÍSICA
    @FXML TextField jtfsignosvitales;
    @FXML TextField jtfsaturacionoxigeno;
    @FXML TextField jtfPA;
    @FXML TextField jtfFC;
    @FXML TextField jtftemperatura;
    @FXML TextField jtfFR;
    @FXML TextField jtfexamenclinicogeneral;
    @FXML TextField jtfexamenclinicoodontoestomatolgico;
    //Diagnóstico
    @FXML TextArea jtaDiagCIE10;
    @FXML TextArea jtaDiagPresentivo;
    @FXML TextArea jtaDiagDefinitivo;
    //Plan de tratamiento
    @FXML TextArea jtfrecomendaciones;
    //Pronóstico / alta paciente
    @FXML TextArea jtapronostico;
    @FXML TextArea jtaAltapaciente;
            
    @FXML CheckBox checkpregunta1;@FXML TextField jtfantPregunta1;
    @FXML CheckBox checkpregunta2;@FXML TextField jtfantPregunta2;
    @FXML CheckBox checkpregunta3;@FXML TextField jtfantPregunta3;
    @FXML CheckBox checkpregunta4;@FXML TextField jtfantPregunta4;
    @FXML CheckBox checkpreguntamujer1;@FXML TextField jtfantpreguntamujer1;  
    @FXML CheckBox checkpreguntamujer2;@FXML TextField jtfantpreguntamujer2; 
    /*----------Fin Atributos---------------*/

    //Fin Atributos Actualización  
    AlertController oAlertController=new AlertController();
    Persona oPersona;
    Stage stagePrincipal;
    private double x = 0;
    private double y = 0;
    List<CheckBox> listcheck = new ArrayList<>();
    //Botones y métodos de prueba   
    
    //Fin Botones y métodos de prueba
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> OCUPACION=FXCollections.observableArrayList("ESTUDIANTE", "UNIVERSITARIO", "TRABAJADOR");
        ObservableList<String> SEXO=FXCollections.observableArrayList("VARÓN", "MUJER");
        jcbocupacion.setItems(OCUPACION);
        jcbsexo.setItems(SEXO);
        accordion.setExpandedPane(tpAnamnesis); 
        asignar();
        initRestricciones();
    }    
    
    @FXML
    void GuardarPaciente(ActionEvent evt) throws IOException {
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
                jtfTelefono.getText().trim()
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
                jtfsignosvitales.getText().trim(),
                jtfsaturacionoxigeno.getText().trim(),
                jtfPA.getText().trim(),
                jtfFC.getText().trim(),
                jtftemperatura.getText().trim(),
                jtfFR.getText(),
                jtfexamenclinicogeneral.getText().trim(),
                jtfexamenclinicoodontoestomatolgico.getText().trim(),
                jtaDiagCIE10.getText().trim(),
                jtaDiagPresentivo.getText().trim(),
                jtaDiagDefinitivo.getText().trim(),
                jtfrecomendaciones.getText().trim(),
                jtapronostico.getText().trim(),
                jtaAltapaciente.getText().trim(),
                jtfmotivoconsulta.getText().trim(),
                LocalDate.now(),
                LocalDate.now());
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

    List<Paciente_Enfermedad> Paciente_relacionar_enfermedad(List<CheckBox> listC, Paciente opaciente) {
        List<Enfermedad> list_enfermedad = App.jpa.createQuery("select p from Enfermedad p ").getResultList();
        List<Paciente_Enfermedad> list_enfermedades_paciente = new ArrayList<Paciente_Enfermedad>();
        for (CheckBox checkBox : listC) {
            for (Enfermedad enfermedad : list_enfermedad) {
                if (checkBox.getUserData().toString().equals(enfermedad.getNombre())) {
                    String Alergia = "";
                    if (enfermedad.getNombre().equals("Alergia")) {
                        Alergia = jtfantAque.getText().trim();
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
                        pgt = jtfantPregunta1.getText().trim();
                        isMujer = false;
                    } else if (checkBox.getUserData().toString().equals("¿Le han realizado alguna intervención quirúrjica?")) {
                        pgt = jtfantPregunta2.getText().trim();
                        isMujer = false;
                    } else if (checkBox.getUserData().toString().equals("¿Está usted tomando algún medicamento?")) {
                        pgt = jtfantPregunta3.getText().trim();
                        isMujer = false;
                    } else if (checkBox.getUserData().toString().equals("¿Es alérgico a la anestesia dental?")) {
                        pgt = jtfantPregunta4.getText().trim();
                        isMujer = false;
                    } else if (checkBox.getUserData().toString().equals("¿Está usted embarazada?")) {
                        pgt = jtfantpreguntamujer1.getText().trim();
                    }
                    list_pregunta_paciente.add(new Paciente_Pregunta(opaciente, pregunta, pgt, isMujer));
                }
            }
        }
        return list_pregunta_paciente;
    }

    void setStagePrincipall(Stage stage) {
        this.stagePrincipal = stage;
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

    /*--Otras ventanas---*/
    @FXML
    void mostrarVerPaciente() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VerPacienteController.class.getResource("VerPaciente.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);//instancia el controlador (!)
        scene.getStylesheets().add(VerPacienteController.class.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(stagePrincipal);
        stage.setScene(scene);
        VerPacienteController oVerController = (VerPacienteController) loader.getController(); //esto depende de (1)
        oVerController.setController(this);
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x = event.getX();
                y = event.getY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            }
        });
        stage.show();
    }

    /*--Otras ventanas fin---*/
    void initRestricciones(){
        jtfNombresyApellidos.addEventHandler(KeyEvent.KEY_TYPED, event-> SoloLetras(event));
        jtfTelefono.addEventHandler(KeyEvent.KEY_TYPED, event-> SoloNumerosEntero(event));
        jtfDni.addEventHandler(KeyEvent.KEY_TYPED, event-> SoloNumerosEnteros8(event));
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
        if (jtfantAque.isDisable()) {
            jtfantAque.setDisable(false);
        } else {
            jtfantAque.setDisable(true);
        }
    }

    @FXML
    void unlockecdPregunta1(ActionEvent event) {
        if (jtfantPregunta1.isDisable()) {
            listcheck.add((CheckBox) event.getSource());
            jtfantPregunta1.setDisable(false);
        } else {
            listcheck.remove((CheckBox) event.getSource());
            jtfantPregunta1.setDisable(true);
        }
    }

    @FXML
    void unlockecdPregunta2(ActionEvent event) {
        if (jtfantPregunta2.isDisable()) {
            listcheck.add((CheckBox) event.getSource());
            jtfantPregunta2.setDisable(false);
        } else {
            listcheck.remove((CheckBox) event.getSource());
            jtfantPregunta2.setDisable(true);
        }
    }

    @FXML
    void unlockecdPregunta3(ActionEvent event) {
        listcheck.add((CheckBox) event.getSource());
        if (jtfantPregunta3.isDisable()) {
            listcheck.add((CheckBox) event.getSource());
            jtfantPregunta3.setDisable(false);
        } else {
            listcheck.remove((CheckBox) event.getSource());
            jtfantPregunta3.setDisable(true);
        }
    }

    @FXML
    void unlockecdPregunta4(ActionEvent event) {
        listcheck.add((CheckBox) event.getSource());
        if (jtfantPregunta4.isDisable()) {
            listcheck.add((CheckBox) event.getSource());
            jtfantPregunta4.setDisable(false);
        } else {
            listcheck.remove((CheckBox) event.getSource());
            jtfantPregunta4.setDisable(true);
        }
    }

    @FXML
    void unlockecdPreguntamujer1(ActionEvent event) {

        if (jtfantpreguntamujer1.isDisable()) {
            listcheck.add((CheckBox) event.getSource());
            jtfantpreguntamujer1.setDisable(false);
        } else {
            listcheck.remove((CheckBox) event.getSource());
            jtfantpreguntamujer1.setDisable(true);
        }
    }
    /*------Fin Metodos de ventana---------------*/
}
