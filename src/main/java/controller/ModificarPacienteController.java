/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Doctor;
import Entidades.Enfermedad;
import Entidades.ExamenAuxiliar;
import Entidades.Historia_clinica;
import Entidades.Paciente;
import Entidades.Paciente_Enfermedad;
import Entidades.Paciente_Pregunta;
import Entidades.Persona;
import Entidades.PlanTratamiento;
import Entidades.Pregunta;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author UTIC
 */
public class ModificarPacienteController implements Initializable {       
    //Atributos de la ventana
    @FXML AnchorPane ap;
    @FXML Accordion accordion,accordion2;
    @FXML TitledPane tpAnamnesis, tpEnfermedades;
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
    @FXML TextArea jtfmotivoconsulta;
    @FXML TextField jtftutornombre, jtftutordni, jtftutortelefono;
    @FXML TextField jtfemergenciaNombre, jtfemergenciaParentesco, jtfemergenciatelefono;
    @FXML JFXComboBox<Doctor> jcbDoctor;
    // examenes auxiliares
    @FXML
    private JFXTextField jtfExamenAuxiliar;
    
    //plan tratamiento
    @FXML
    private JFXTextField jtfPlandetratamiento;

    @FXML
    private TableView<ExamenAuxiliar> tableExamenAuxiliar;

    @FXML
    private TableColumn<ExamenAuxiliar, String> columnNumExamenAuxiliar;

    @FXML
    private TableColumn<ExamenAuxiliar, String> columnExamenAuxiliar;

    @FXML
    private TableColumn<ExamenAuxiliar, ExamenAuxiliar> columnEstadoExamenAuxiliar;

    @FXML
    private TableView<PlanTratamiento> tablePlandetratamiento;

    @FXML
    private TableColumn<PlanTratamiento, String> columnNum;

    @FXML
    private TableColumn<PlanTratamiento, String> columnPlanTratamiento;

    @FXML
    private TableColumn<PlanTratamiento, PlanTratamiento> columnEstado;
    
    //II.
    @FXML
    private JFXTextField jtfenfermedadActual, jtf_examen_intraoral, jtfantecedentesPersonales;
    
    //Enfermedad actual
    @FXML TextField jtfsintomasEnfermedadActual;
    @FXML TextField jtftiempoEnfermedadActual;
    
    //Antecedentes
    @FXML CheckBox checkalergia;;@FXML TextField jtfAlergapregunta;
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
    //Plan de tratamiento
    @FXML TextArea jtfrecomendaciones;
    //Pronóstico / alta paciente
    @FXML TextArea jtapronostico;
    @FXML TextArea jtaAltapaciente;
            
    @FXML CheckBox checkpregunta1;@FXML TextField jtfpregunta1;
    @FXML CheckBox checkpregunta2;@FXML TextField jtfpregunta2;
    @FXML CheckBox checkpregunta3;@FXML TextField jtfpregunta3;
    @FXML CheckBox checkpregunta4;@FXML TextField jtfpregunta4;
    @FXML CheckBox checkpreguntamujer1;@FXML TextField jtfPreguntamujer;  
    @FXML CheckBox checkpreguntamujer2;@FXML TextField jtfantpreguntamujer2; 
    /*----------Fin Atributos---------------*/

    //Fin Atributos Actualización  
    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
    AlertController oAlertController=new AlertController();
    VerPacienteController oVerPacienteController;
    Stage stagePrincipal;
    private double x = 0;
    private double y = 0;
    List<CheckBox> listcheck = new ArrayList<>();
    List<CheckBox> listcheckPregunta = new ArrayList<>();
    List<CheckBox> listcheckG = new ArrayList<>();
    List<CheckBox> listcheckGPregunta = new ArrayList<>();
    List<Paciente_Enfermedad> listPaciente_Enfermedad;
    List<Paciente_Pregunta> listPaciente_pregunta;
    Persona oPersona;
    Paciente oPaciente;
    Historia_clinica oHistoria_Clinica;
    ObservableList<PlanTratamiento> listPlanTratamiento = FXCollections.observableArrayList();
    ObservableList<ExamenAuxiliar> listExamenAuxiliar = FXCollections.observableArrayList();
    //Botones y métodos de prueba   
    
    //Fin Botones y métodos de prueba
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> OCUPACION=FXCollections.observableArrayList("ESTUDIANTE", "UNIVERSITARIO", "TRABAJADOR");
        ObservableList<String> SEXO=FXCollections.observableArrayList("VARÓN", "MUJER");
        jcbocupacion.setItems(OCUPACION);
        jcbsexo.setItems(SEXO);
        accordion.setExpandedPane(tpAnamnesis); 
        accordion2.setExpandedPane(tpEnfermedades);
        asignar();
        listCheck();
        listCheckPregunta();
        initRestricciones();
        cargarDoctor();
        initTablePlanTratamiento();
        initTableExamenesAuxiliares();
        tablePlandetratamiento.setItems(listPlanTratamiento);
        tableExamenAuxiliar.setItems(listExamenAuxiliar);
        
    }    
    
    void cargarDoctor(){
        List<Doctor> listDoctorG = App.jpa.createQuery("select p from Doctor p where flag = false and activo = true").getResultList();      
        ObservableList<Doctor> listDoctor = FXCollections.observableArrayList();
        for (Doctor odoct : listDoctorG) {
            listDoctor.add(odoct);
        }
        jcbDoctor.setItems(listDoctor);
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
                        Alergia = jtfAlergapregunta.getText().trim();
                    }
                    list_enfermedades_paciente.add(new Paciente_Enfermedad(opaciente, enfermedad, Alergia));
                }
            }
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
                    } else if (checkBox.getUserData().toString().equals("¿Está usted tomando algún medicamento?")) {
                        pgt = jtfpregunta2.getText().trim();
                        isMujer = false;
                    } else if (checkBox.getUserData().toString().equals("¿Le han realizado alguna intervención quirúrjica?")) {
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

    void setStagePrincipall(Stage stage) {
        this.stagePrincipal = stage;
    }

    @FXML
    void test(){
        for (CheckBox checkBox : listcheck) {
            System.out.println(checkBox.getUserData().toString());
        }
        
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
    
    @FXML
    void agregarPlanTratamiento() {
        if (jtfPlandetratamiento.getText().length() != 0) {
            PlanTratamiento oplantratamiento = new PlanTratamiento();
            oplantratamiento.setDescripcion(jtfPlandetratamiento.getText());
            oplantratamiento.setHistoria_clinica(oHistoria_Clinica);
            App.jpa.getTransaction().begin();
            App.jpa.persist(oplantratamiento);
            App.jpa.getTransaction().commit();
            jtfPlandetratamiento.setStyle("");
            jtfPlandetratamiento.setText("");
            updateListPlanTratamiento();
        } else {
            jtfPlandetratamiento.setStyle("-fx-border-color: #ff052b");
            alertWarning.setHeaderText(null);
            alertWarning.setTitle("Plan de tratamiento");
            alertWarning.setContentText("Espacio en blanco");
            alertWarning.showAndWait();
        }
    }
    
    @FXML
    void agregarExamenAuxiliar() {
        if (jtfExamenAuxiliar.getText().length() != 0) {
            ExamenAuxiliar oExamenAuxiliar = new ExamenAuxiliar();
            oExamenAuxiliar.setDescripcion(jtfExamenAuxiliar.getText());
            oExamenAuxiliar.setHistoria_clinica(oHistoria_Clinica);
            App.jpa.getTransaction().begin();
            App.jpa.persist(oExamenAuxiliar);
            App.jpa.getTransaction().commit();
            jtfExamenAuxiliar.setStyle("");
            jtfExamenAuxiliar.setText("");
            updateListExamenAuxiliar();
        } else {
            jtfExamenAuxiliar.setStyle("-fx-border-color: #ff052b");
            alertWarning.setHeaderText(null);
            alertWarning.setTitle("Examen Auxiliar");
            alertWarning.setContentText("Espacio en blanco");
            alertWarning.showAndWait();
        }
    }
    
    void updateListPlanTratamiento() {
        List<PlanTratamiento> olistTratamiento = App.jpa.createQuery("select p from PlanTratamiento p where idhistoria_clinica= " + oHistoria_Clinica.getIdhistoria_clinica() + "  order by idplantratamiento ASC").getResultList();
        listPlanTratamiento.clear();
        for (PlanTratamiento planTratamiento : olistTratamiento) {
            listPlanTratamiento.add(planTratamiento);            
        }
    }
    
    void updateListExamenAuxiliar() {
        List<ExamenAuxiliar> olistExamenAuxiliar = App.jpa.createQuery("select p from ExamenAuxiliar p where idhistoria_clinica= " + oHistoria_Clinica.getIdhistoria_clinica() + "  order by idexamenauxiliar ASC").getResultList();
        listExamenAuxiliar.clear();
        for (ExamenAuxiliar oexam : olistExamenAuxiliar) {
            listExamenAuxiliar.add(oexam);            
        }
    }
    
    void initTablePlanTratamiento() {
        columnNum.setCellValueFactory(new PropertyValueFactory<PlanTratamiento, String>("descripcion"));
        columnPlanTratamiento.setCellValueFactory(new PropertyValueFactory<PlanTratamiento, String>("descripcion"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<PlanTratamiento, PlanTratamiento>("PlanTratamiento"));
        columnNum.setCellFactory(column -> {
            TableCell<PlanTratamiento, String> cell = new TableCell<PlanTratamiento, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();
                        label.setText((getIndex() + 1) + "");
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999; ");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });

        columnEstado.setCellFactory(column -> {
            TableCell<PlanTratamiento, PlanTratamiento> cell = new TableCell<PlanTratamiento, PlanTratamiento>() {
                @Override
                protected void updateItem(PlanTratamiento item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        int tamHightImag = 16;
                        int tamWidthImag = 16;
                        ImageView deleteIcon = new ImageView(new Image(getClass().getResource("/imagenes/delete-1.png").toExternalForm()));
                        deleteIcon.setFitHeight(tamHightImag);
                        deleteIcon.setFitWidth(tamWidthImag);
                        deleteIcon.setUserData(item);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand;"
                        );
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarEliminar(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagEliminarMoved(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagEliminarFuera(event));
                        //deleteIcon.setText("Eliminar");
                        HBox managebtn = new HBox(deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        setGraphic(managebtn);
                        setText(null);

                    }

                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    PlanTratamiento plan = (PlanTratamiento) imag.getUserData();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Info");
                    alert.setContentText("¿Desea eliminar el plan de tratamiento: " + plan.getDescripcion() + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        listPlanTratamiento.remove(plan);
                        App.jpa.getTransaction().begin();
                        App.jpa.remove(plan);
                        App.jpa.getTransaction().commit();
                        updateListPlanTratamiento();
                    }
                }

                private void imagEliminarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-2.png").toExternalForm()));
                }

                private void imagEliminarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-1.png").toExternalForm()));
                }

            };
            return cell;
        });

    }
    
    void initTableExamenesAuxiliares() {
        columnNumExamenAuxiliar.setCellValueFactory(new PropertyValueFactory<ExamenAuxiliar, String>("descripcion"));
        columnExamenAuxiliar.setCellValueFactory(new PropertyValueFactory<ExamenAuxiliar, String>("descripcion"));
        columnEstadoExamenAuxiliar.setCellValueFactory(new PropertyValueFactory<ExamenAuxiliar, ExamenAuxiliar>("PlanTratamiento"));
        columnNumExamenAuxiliar.setCellFactory(column -> {
            TableCell<ExamenAuxiliar, String> cell = new TableCell<ExamenAuxiliar, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();
                        label.setText((getIndex() + 1) + "");
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999; ");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });

        columnEstadoExamenAuxiliar.setCellFactory(column -> {
            TableCell<ExamenAuxiliar, ExamenAuxiliar> cell = new TableCell<ExamenAuxiliar, ExamenAuxiliar>() {
                @Override
                protected void updateItem(ExamenAuxiliar item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        int tamHightImag = 16;
                        int tamWidthImag = 16;
                        ImageView deleteIcon = new ImageView(new Image(getClass().getResource("/imagenes/delete-1.png").toExternalForm()));
                        deleteIcon.setFitHeight(tamHightImag);
                        deleteIcon.setFitWidth(tamWidthImag);
                        deleteIcon.setUserData(item);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand;"
                        );
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarEliminar(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagEliminarMoved(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagEliminarFuera(event));
                        //deleteIcon.setText("Eliminar");
                        HBox managebtn = new HBox(deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        setGraphic(managebtn);
                        setText(null);

                    }

                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    ExamenAuxiliar oexam = (ExamenAuxiliar) imag.getUserData();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Info");
                    alert.setContentText("¿Desea eliminar el examen auxilair: " + oexam.getDescripcion() + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        listPlanTratamiento.remove(oexam);
                        App.jpa.getTransaction().begin();
                        App.jpa.remove(oexam);
                        App.jpa.getTransaction().commit();
                        updateListExamenAuxiliar();
                    }
                }

                private void imagEliminarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-2.png").toExternalForm()));
                }

                private void imagEliminarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-1.png").toExternalForm()));
                }

            };
            return cell;
        });

    }
    
    void listCheck(){
        listcheckG.add(checkalergia);listcheckG.add(checkfiebrereumatica);listcheckG.add(checkanemia);
        listcheckG.add(checkdiabetes);listcheckG.add(checktuberculosis);listcheckG.add(checkhepatitis);
        listcheckG.add(checkinfeccionveneria);listcheckG.add(checkenfermedadcardiaca);listcheckG.add(checkgastritis);
        listcheckG.add(checkepilepsia);listcheckG.add(checkdolordepecho);listcheckG.add(checkneuralgia);
        listcheckG.add(checkenfermedaddelapiel);listcheckG.add(checkenfermedadrenal);listcheckG.add(checkhipertensionarterial);
    }
    
    void listCheckPregunta(){
        listcheckGPregunta.add(checkpregunta1);listcheckGPregunta.add(checkpregunta2);listcheckGPregunta.add(checkpregunta3);
        listcheckGPregunta.add(checkpregunta4);listcheckGPregunta.add(checkpreguntamujer1);listcheckGPregunta.add(checkpreguntamujer2);     
   }
    
    public void setPersona(Persona opersona) {
        this.oPersona= opersona;
        this.oPaciente = (Paciente) App.jpa.createQuery("select p from Paciente p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        this.oHistoria_Clinica = (Historia_clinica) App.jpa.createQuery("select p from Historia_clinica p where idpaciente=" + oPaciente.getIdpaciente()).getSingleResult();
        listPaciente_Enfermedad = App.jpa.createQuery("select p from Paciente_Enfermedad p where idpaciente=" + oPaciente.getIdpaciente()).getResultList();
        listPaciente_pregunta = App.jpa.createQuery("select p from Paciente_Pregunta p where idpaciente=" + oPaciente.getIdpaciente()).getResultList();
                
        jtfNombresyApellidos.setText(opersona.getNombres_apellidos());
        jtfDni.setText(opersona.getDni());
        jtfTelefono.setText(opersona.getTelefono());
        jcbsexo.getSelectionModel().select(opersona.getSexo());
        jtfDia.setText(opersona.getFechaNacimiento().getDayOfMonth()+"");
        jtfMes.setText(opersona.getFechaNacimiento().getMonthValue()+"");
        jtfanio.setText(opersona.getFechaNacimiento().getYear()+"");
        jcbocupacion.getSelectionModel().select(opersona.getOcupacion());
        jtflugarprocedencia.setText(opersona.getLugar_de_procedencia());
        jtfDomicilio.setText(opersona.getDomicilio());
        jcbDoctor.getSelectionModel().select(oHistoria_Clinica.getDoctor().isFlag()? null: oHistoria_Clinica.getDoctor());
        
        jtfmotivoconsulta.setText(oHistoria_Clinica.getMotivoConsulta());
        
        jtfemergenciaNombre.setText(oPaciente.getEmergenciaNombre());
        jtfemergenciaParentesco.setText(oPaciente.getEmergenciaParentesco());
        jtfemergenciatelefono.setText(oPaciente.getEmergenciaTelefono());
        jtfenfermedadActual.setText(oPaciente.getEnfermedadActual());
        jtf_examen_intraoral.setText(oPaciente.getExamenIntraoral());
        jtfantecedentesPersonales.setText(oPaciente.getAntecedentesFamiliares());
        
        jtftutornombre.setText(opersona.getTutorNombre());
        jtftutordni.setText(opersona.getTutorDni());
        jtftutortelefono.setText(opersona.getTutorTelefono());
        
        //enfermedad actual
        jtfsintomasEnfermedadActual.setText(oPaciente.getSintomasEnfermedadActual());
        jtftiempoEnfermedadActual.setText(oPaciente.getTiempoEnfermedadActual());
        
        //exploracin física    
        jtfsignosvitales.setText(oHistoria_Clinica.getSignosVitales());
        jtfsaturacionoxigeno.setText(oHistoria_Clinica.getSaturacionOxigeno());
        jtfPA.setText(oHistoria_Clinica.getPA());
        jtfFC.setText(oHistoria_Clinica.getFC());
        jtftemperatura.setText(oHistoria_Clinica.getTemperatura());
        jtfFR.setText(oHistoria_Clinica.getFR());
        jtfexamenclinicogeneral.setText(oHistoria_Clinica.getExamenClinicoGeneral());
        jtfexamenclinicoodontoestomatolgico.setText(oHistoria_Clinica.getExamenClinicoOdontoestomtologico());
        
        //diagnostico
        jtaDiagCIE10.setText(oHistoria_Clinica.getDiagnosticoCIE10());

        //Plan de tratamiento
        jtfrecomendaciones.setText(oHistoria_Clinica.getRecomendaciones());
        
        //Pronóstico
        jtapronostico.setText(oHistoria_Clinica.getPronostico());
        jtaAltapaciente.setText(oHistoria_Clinica.getAltaPaciente());
        
        //Antecedentes
        jtfotrasEnfermedades.setText(oPaciente.getOtrasEnfermedades());
        jtfantecedentesFamiliares.setText(oPaciente.getAntecedentesFamiliares());
        
        
        //antecendentes
        for (CheckBox checkBox : listcheckG) {
            for (Paciente_Enfermedad paciente_Enfermedad : listPaciente_Enfermedad) {
                if (checkBox.getUserData().toString().equals(paciente_Enfermedad.getEnfermedad().getNombre())) {
                    listcheck.add(checkBox);
                    checkBox.setSelected(true);
                    if (paciente_Enfermedad.getEnfermedad().getNombre().equals("Alergia")) {
                        jtfAlergapregunta.setText(paciente_Enfermedad.getDetalleEnfermedad());
                        jtfAlergapregunta.setDisable(false);
                    }
                }
            }

        }
        
        for (CheckBox checkbox : listcheckGPregunta) {
            for (Paciente_Pregunta pregunta : listPaciente_pregunta) {
                if (pregunta.getPregunta().getTextopregunta().equals(checkbox.getUserData().toString())) {
                    listcheckPregunta.add(checkbox);
                    checkbox.setSelected(true);
                    if (checkbox.getUserData().toString().equals("¿Reacciona  anormalmente a algún medicamento?")) {
                        jtfpregunta1.setText(pregunta.getEspecificaciones());
                        jtfpregunta1.setDisable(false);

                    } else if (checkbox.getUserData().toString().equals("¿Está usted tomando algún medicamento?")) {
                        jtfpregunta2.setText(pregunta.getEspecificaciones());
                        jtfpregunta2.setDisable(false);
                    } else if (checkbox.getUserData().toString().equals("¿Le han realizado alguna intervención quirúrjica?")) {
                        jtfpregunta3.setText(pregunta.getEspecificaciones());
                        jtfpregunta3.setDisable(false);

                    } else if (checkbox.getUserData().toString().equals("¿Es alérgico a la anestesia dental?")) {
                        jtfpregunta4.setText(pregunta.getEspecificaciones());
                        jtfpregunta4.setDisable(false);

                    } else if (checkbox.getUserData().toString().equals("¿Está usted embarazada?")) {
                        jtfPreguntamujer.setText(pregunta.getEspecificaciones());
                        jtfPreguntamujer.setDisable(false);
                    }
                }
            }
        }
        updateListPlanTratamiento();
        updateListExamenAuxiliar();
    }

    void setController(VerPacienteController odc) {
        this.oVerPacienteController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }
    
    @FXML
    void actualizar() {
        oPersona.setNombres_apellidos(jtfNombresyApellidos.getText());
        oPersona.setDni(jtfDni.getText());
        oPersona.setTelefono(jtfTelefono.getText());
        oPersona.setSexo(jcbsexo.getSelectionModel().getSelectedItem());
        oPersona.setFechaNacimiento(LocalDate.of(Integer.parseInt(jtfanio.getText()), Integer.parseInt(jtfMes.getText()), Integer.parseInt(jtfDia.getText())));
        oPersona.setOcupacion(jcbocupacion.getSelectionModel().getSelectedItem());
        oPersona.setLugar_de_procedencia(jtflugarprocedencia.getText());
        oPersona.setDomicilio(jtfDomicilio.getText());

        oHistoria_Clinica.setMotivoConsulta(jtfmotivoconsulta.getText());
        oHistoria_Clinica.setDoctor(jcbDoctor.getSelectionModel().getSelectedItem());
        
        oPaciente.setEmergenciaNombre(jtfemergenciaNombre.getText());
        oPaciente.setEmergenciaParentesco(jtfemergenciaParentesco.getText());
        oPaciente.setEmergenciaTelefono(jtfemergenciatelefono.getText());
        
        oPersona.setTutorNombre(jtftutornombre.getText());
        oPersona.setTutorDni(jtftutordni.getText());
        oPersona.setTutorTelefono(jtftutortelefono.getText());
        
        //Enfermedad actual
        oPaciente.setSintomasEnfermedadActual(jtfsintomasEnfermedadActual.getText());
        oPaciente.setTiempoEnfermedadActual(jtftiempoEnfermedadActual.getText());
        oPaciente.setEnfermedadActual(jtfenfermedadActual.getText());
        oPaciente.setExamenIntraoral(jtf_examen_intraoral.getText());
        oPaciente.setAntecedentesFamiliares(jtfantecedentesPersonales.getText());
        
        //Exploración física
        oHistoria_Clinica.setSignosVitales(jtfsignosvitales.getText());
        oHistoria_Clinica.setSaturacionOxigeno(jtfsaturacionoxigeno.getText());
        oHistoria_Clinica.setPA(jtfPA.getText());
        oHistoria_Clinica.setFC(jtfFC.getText());
        oHistoria_Clinica.setTemperatura(jtftemperatura.getText());
        oHistoria_Clinica.setFR(jtfFR.getText());
        oHistoria_Clinica.setExamenClinicoGeneral(jtfexamenclinicogeneral.getText());
        oHistoria_Clinica.setExamenClinicoOdontoestomtologico(jtfexamenclinicoodontoestomatolgico.getText());
        
        //diagnostico
        oHistoria_Clinica.setDiagnosticoCIE10(jtaDiagCIE10.getText());
        
        //Plan de tratamiento
        oHistoria_Clinica.setRecomendaciones(jtfrecomendaciones.getText());
        
        //Pronóstico
        oHistoria_Clinica.setPronostico(jtapronostico.getText());
        oHistoria_Clinica.setAltaPaciente(jtaAltapaciente.getText());
        
        //Antecedentes
        oPaciente.setOtrasEnfermedades(jtfotrasEnfermedades.getText());
        //oPaciente.setAntecedentesFamiliares(jtfantecedentesFamiliares.getText());
        App.jpa.getTransaction().begin();
        for (Paciente_Enfermedad oPacienteEnfermedad : listPaciente_Enfermedad) {
            boolean borrar = true;
            for (CheckBox checkBox : listcheck) {
                if (oPacienteEnfermedad.getEnfermedad().getNombre().equals(checkBox.getUserData().toString())) {
                    borrar = false;
                }
            }
            if (borrar) {
                App.jpa.remove(oPacienteEnfermedad);
            }
        }
        List<CheckBox> listcheckAdd = new ArrayList<>();
        for (CheckBox checkBox : listcheck) {
            boolean agregar = true;
            for (Paciente_Enfermedad oPaciente_Enfermedad : listPaciente_Enfermedad) {
                if (oPaciente_Enfermedad.getEnfermedad().getNombre().equals(checkBox.getUserData().toString())) {
                    agregar = false;
                    if (oPaciente_Enfermedad.getEnfermedad().getNombre().equals("Alergia")) {
                        oPaciente_Enfermedad.setDetalleEnfermedad(jtfAlergapregunta.getText());
                        App.jpa.persist(oPaciente_Enfermedad);
                    }
                }
            }
            if (agregar) {
                listcheckAdd.add(checkBox);
            }
        }
        List<Paciente_Enfermedad> Lista_enfermedadesPaciente = Paciente_relacionar_enfermedad(listcheckAdd, oPaciente);
        
        /*---preguntas-------*/
        for (Paciente_Pregunta oPacientePregunta : listPaciente_pregunta) {
            boolean borrar = true;
            for (CheckBox checkBox : listcheckPregunta) {
                if (oPacientePregunta.getPregunta().getTextopregunta().equals(checkBox.getUserData().toString())) {
                    borrar = false;
                }
            }
            if (borrar) {
                App.jpa.remove(oPacientePregunta);
            }
        }
        List<CheckBox> listcheckAddPregunta = new ArrayList<>();
        for (CheckBox checkBox : listcheckPregunta) {
            boolean agregar = true;
            for (Paciente_Pregunta oPaciente_Pregunta : listPaciente_pregunta) {
                if (oPaciente_Pregunta.getPregunta().getTextopregunta().equals(checkBox.getUserData().toString())) {
                    agregar = false;
                    if (oPaciente_Pregunta.getPregunta().getTextopregunta().equals("¿Reacciona  anormalmente a algún medicamento?")) {
                        oPaciente_Pregunta.setEspecificaciones(jtfpregunta1.getText());
                        App.jpa.persist(oPaciente_Pregunta);
                    } else if (oPaciente_Pregunta.getPregunta().getTextopregunta().equals("¿Está usted tomando algún medicamento?")) {
                        oPaciente_Pregunta.setEspecificaciones(jtfpregunta2.getText());
                        App.jpa.persist(oPaciente_Pregunta);
                    } else if (oPaciente_Pregunta.getPregunta().getTextopregunta().equals("¿Le han realizado alguna intervención quirúrjica?")) {
                        oPaciente_Pregunta.setEspecificaciones(jtfpregunta3.getText());
                        App.jpa.persist(oPaciente_Pregunta);
                    } else if (oPaciente_Pregunta.getPregunta().getTextopregunta().equals("¿Es alérgico a la anestesia dental?")) {
                        oPaciente_Pregunta.setEspecificaciones(jtfpregunta4.getText());
                        App.jpa.persist(oPaciente_Pregunta);
                    } else if (oPaciente_Pregunta.getPregunta().getTextopregunta().equals("¿Está usted embarazada?")) {
                        oPaciente_Pregunta.setEspecificaciones(jtfPreguntamujer.getText());
                        App.jpa.persist(oPaciente_Pregunta);
                    }
                }
            }
            if (agregar) {
                listcheckAddPregunta.add(checkBox);
            }
        }
        List<Paciente_Pregunta> Lista_preguntaPaciente = Paciente_relacionar_pregunta(listcheckAddPregunta, oPaciente);
        
        
        
        App.jpa.persist(oPersona);
        App.jpa.persist(oPaciente);
        for (Paciente_Enfermedad paciente_Enfermedad : Lista_enfermedadesPaciente) {
            App.jpa.persist(paciente_Enfermedad);
        }
        for (Paciente_Pregunta paciente_Pregunta : Lista_preguntaPaciente) {
            App.jpa.persist(paciente_Pregunta);
            
        }
        App.jpa.getTransaction().commit();
        cerrar();
        oVerPacienteController.updateListPersona();
        oVerPacienteController.selectModificado(oPersona);
    }

    /*--Otras ventanas---*/
    

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
        if (jtfAlergapregunta.isDisable()) {
            jtfAlergapregunta.setDisable(false);
        } else {
            jtfAlergapregunta.setDisable(true);
        }
    }

    @FXML
    void unlockecdPregunta1(ActionEvent event) {
        CheckBox ch = (CheckBox) event.getSource();
        if (ch.isSelected()) {
            listcheckPregunta.add(ch);
            jtfpregunta1.setDisable(false);
        } else {
            listcheckPregunta.remove((CheckBox) event.getSource());
            jtfpregunta1.setDisable(true);
        }
    }

    @FXML
    void unlockecdPregunta2(ActionEvent event) {
        CheckBox ch = (CheckBox) event.getSource();
        if (ch.isSelected()) {
            listcheckPregunta.add(ch);
            jtfpregunta2.setDisable(false);
        } else {
            listcheckPregunta.remove((CheckBox) event.getSource());
            jtfpregunta2.setDisable(true);
        }
    }
    
    @FXML
    void unlockecdPregunta3(ActionEvent event) {
        CheckBox ch = (CheckBox) event.getSource();
        if (ch.isSelected()) {
            listcheckPregunta.add(ch);
            jtfpregunta3.setDisable(false);
        } else {
            listcheckPregunta.remove(ch);
            jtfpregunta3.setDisable(true);
        }
    }

    @FXML
    void unlockecdPregunta4(ActionEvent event) {
        CheckBox ch = (CheckBox) event.getSource();
        if (ch.isSelected()) {
            listcheckPregunta.add(ch);
            jtfpregunta4.setDisable(false);
        } else {
            listcheckPregunta.remove(ch);
            jtfpregunta4.setDisable(true);
        }
    }

    @FXML
    void unlockecdPreguntamujer1(ActionEvent event) {
        CheckBox ch = (CheckBox) event.getSource();
        if (ch.isSelected()) {
            listcheckPregunta.add(ch);
            jtfPreguntamujer.setDisable(false);
        } else {
            listcheckPregunta.remove(ch);
            jtfPreguntamujer.setDisable(true);
        }
    }
    
    @FXML
    void unlockecdPreguntamujer2(ActionEvent event) {
        CheckBox ch = (CheckBox) event.getSource();
        if (ch.isSelected()) {
            listcheckPregunta.add(ch);
        } else {
            listcheckPregunta.remove(ch);
        }
    }

    @FXML
    void cerrar(){
        oVerPacienteController.lockedPantalla();
        ((Stage)ap.getScene().getWindow()).close();
    }
    /*------Fin Metodos de ventana---------------*/

}
