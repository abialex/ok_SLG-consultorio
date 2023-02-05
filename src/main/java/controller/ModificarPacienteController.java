/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Doctor;
import Entidades.Enfermedad;
import Entidades.ExamenAuxiliar;
import Entidades.Historia_clinica;
import Entidades.Persona_Enfermedad;
import Entidades.Persona;
import Entidades.PlanTratamiento;
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
import javafx.geometry.Insets;
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
    @FXML
    AnchorPane ap;
    @FXML
    Accordion accordion;
    @FXML
    TitledPane tpAnamnesis, tpEnfermedades;
    @FXML
    TitledPane tpAnamnesisAct;
    @FXML
    JFXDatePicker dtpicker;
    @FXML
    Button jbtnGuardar;
    @FXML
    JFXTextField jtfbuscar;
    @FXML
    Button jbtnbuscar;
    @FXML
    TextField jtfbuscarAct;
    @FXML
    Label jlblnombresyapellidos;
    @FXML
    Label jlbldni;
    @FXML
    Button jbtnimprimir;

    @FXML
    HBox hbox_enfermedad;
    /*--------------Atributos---------------*/
    //Anamnesis
    @FXML
    TextField jtfNombresyApellidos, jtf_ap_paterno, jtf_ap_materno;
    @FXML
    TextField jtfDomicilio;
    @FXML
    TextField jtfDni;
    @FXML
    TextField jtfDia;
    @FXML
    TextField jtfMes;
    @FXML
    TextField jtfanio;
    @FXML
    TextField jtfTelefono;
    @FXML
    TextField jtflugarprocedencia;
    @FXML
    ComboBox<String> jcbocupacion;
    @FXML
    ComboBox<String> jcbsexo;
    @FXML
    TextField jtf_motivo_consulta, jtf_examen_radiografico, jtf_diagnostico;
    @FXML
    JFXComboBox<Doctor> jcbDoctor;
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
    //Antecedentes
    //EXPLORACIÓN FÍSICA
    //Diagnóstico
    //Plan de tratamiento
    //Pronóstico / alta paciente
    /*----------Fin Atributos---------------*/
    //Fin Atributos Actualización  
    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
    AlertController oAlertController = new AlertController();
    VerPacienteController oVerPacienteController;
    Stage stagePrincipal;
    private double x = 0;
    private double y = 0;
    List<CheckBox> listcheck = new ArrayList<>();
    List<CheckBox> listcheckPregunta = new ArrayList<>();
    List<CheckBox> listcheckG = new ArrayList<>();
    List<CheckBox> listcheckGPregunta = new ArrayList<>();
    List<Persona_Enfermedad> list_enfermedad_persona;
    Persona oPersona;
    Historia_clinica oHistoria_Clinica;
    ObservableList<PlanTratamiento> listPlanTratamiento = FXCollections.observableArrayList();
    ObservableList<ExamenAuxiliar> listExamenAuxiliar = FXCollections.observableArrayList();
    List<Enfermedad> lista_enfermedades_del_persona_volatil = new ArrayList<>();
    List<Persona_Enfermedad> lista_enfermedades_persona = new ArrayList<>();
    //Botones y métodos de prueba   

    //Fin Botones y métodos de prueba
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> OCUPACION = FXCollections.observableArrayList("ESTUDIANTE", "UNIVERSITARIO", "TRABAJADOR");
        ObservableList<String> SEXO = FXCollections.observableArrayList("VARÓN", "MUJER");
        jcbocupacion.setItems(OCUPACION);
        jcbsexo.setItems(SEXO);
        accordion.setExpandedPane(tpAnamnesis);
        initRestricciones();
        cargarDoctor();
        initTablePlanTratamiento();
        initTableExamenesAuxiliares();
        tablePlandetratamiento.setItems(listPlanTratamiento);
        tableExamenAuxiliar.setItems(listExamenAuxiliar);

    }

    void cargarEnfermedad() {
        List<Enfermedad> listEnfermedad = App.jpa.createQuery("select p from Enfermedad p ").getResultList();
        EventHandler evento = (event) -> {
            //Codigo de la función evento
            JFXCheckBox oCheck = ((JFXCheckBox) event.getSource());
            if (oCheck.isSelected()) {
                lista_enfermedades_del_persona_volatil.add((Enfermedad) oCheck.getUserData());
            } else {
                lista_enfermedades_del_persona_volatil.remove((Enfermedad) oCheck.getUserData());
            }
        };
        for (Enfermedad enfermedad_general : listEnfermedad) {            
            JFXCheckBox JB = new JFXCheckBox();
            Label lbl = new Label();
            lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            lbl.setText(enfermedad_general.getNombre());
            JB.setOnAction(evento);
            Insets oInsetslbl = new Insets(0, 15, 0, 15);
            hbox_enfermedad.setMargin(lbl, oInsetslbl);
            JB.setStyle("-fx-background-color: white;\n" + "-fx-border-color:grey;\n" + "-fx-border-radius:3px; ");
            for (Enfermedad enfermedad_paciente : lista_enfermedades_del_persona_volatil) {
                if(enfermedad_general.getIdenfermedad()==enfermedad_paciente.getIdenfermedad()){                   
                    JB.setSelected(true);
                }
            }
            hbox_enfermedad.getChildren().add(JB);
            hbox_enfermedad.getChildren().add(lbl);
            hbox_enfermedad.getChildren();
            JB.setUserData(enfermedad_general);
        }
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
    void cuadrarCheckbox(ActionEvent o) {
        CheckBox ch = (CheckBox) o.getSource();

        if (ch.isSelected()) {
            listcheck.add(ch);

        } else {
            listcheck.remove(ch);
        }
        if (ch.getUserData().toString().equals("Alergia")) {
        }

    }

    void setStagePrincipall(Stage stage) {
        this.stagePrincipal = stage;
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

    public void setPersona(Persona opersona) {
        this.oPersona = opersona;
        //this.oPaciente = (Paciente) App.jpa.createQuery("select p from Paciente p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        this.oHistoria_Clinica = (Historia_clinica) App.jpa.createQuery("select p from Historia_clinica p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        list_enfermedad_persona = App.jpa.createQuery("select p from Persona_Enfermedad p where idpersona=" + opersona.getIdpersona()).getResultList();
        lista_enfermedades_persona = App.jpa.createQuery("select p from Persona_Enfermedad p where idpersona=" + opersona.getIdpersona()).getResultList();
        for (Persona_Enfermedad persona_Enfermedad : list_enfermedad_persona) {            
            lista_enfermedades_del_persona_volatil.add(persona_Enfermedad.getEnfermedad());
        }
         cargarEnfermedad();
        jtfNombresyApellidos.setText(opersona.getNombres());
        jtf_ap_paterno.setText(opersona.getAp_paterno());
        jtf_ap_materno.setText(opersona.getAp_materno());
        jtfDni.setText(opersona.getDni());
        jtfTelefono.setText(opersona.getTelefono());
        jcbsexo.getSelectionModel().select(opersona.getSexo());
        jtfDia.setText(opersona.getFecha_cumple().getDayOfMonth() + "");
        jtfMes.setText(opersona.getFecha_cumple().getMonthValue() + "");
        jtfanio.setText(opersona.getFecha_cumple().getYear() + "");
        jcbocupacion.getSelectionModel().select(opersona.getOcupacion());
        jtflugarprocedencia.setText(opersona.getLugar_de_procedencia());
        jtfDomicilio.setText(opersona.getDomicilio());
        jcbDoctor.getSelectionModel().select(oHistoria_Clinica.getDoctor().isFlag() ? null : oHistoria_Clinica.getDoctor());

        jtf_motivo_consulta.setText(oHistoria_Clinica.getMotivoConsulta());

        jtfenfermedadActual.setText(oHistoria_Clinica.getEnfermedadActual());
        jtf_examen_intraoral.setText(oHistoria_Clinica.getExamenIntraoral());
        jtf_examen_radiografico.setText(oHistoria_Clinica.getExamen_radiografico());
        jtfantecedentesPersonales.setText(oHistoria_Clinica.getAntecedentesFamiliares());
        jtf_diagnostico.setText(oHistoria_Clinica.getDiagnostico());

        //enfermedad actual 
        //exploracin física    
        //diagnostico
        //Plan de tratamiento
        //Pronóstico
        //Antecedentes
        //antecendentes
        updateListPlanTratamiento();
        updateListExamenAuxiliar();
    }

    void setController(VerPacienteController odc) {
        this.oVerPacienteController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    @FXML
    void actualizar() {
        oPersona.setNombres(jtfNombresyApellidos.getText().toUpperCase());
        oPersona.setAp_paterno(jtf_ap_paterno.getText().toUpperCase());
        oPersona.setAp_materno(jtf_ap_materno.getText().toUpperCase());
        oPersona.setDni(jtfDni.getText());
        oPersona.setTelefono(jtfTelefono.getText());
        oPersona.setSexo(jcbsexo.getSelectionModel().getSelectedItem());
        oPersona.setFecha_cumple(LocalDate.of(Integer.parseInt(jtfanio.getText()), Integer.parseInt(jtfMes.getText()), Integer.parseInt(jtfDia.getText())));
        oPersona.setOcupacion(jcbocupacion.getSelectionModel().getSelectedItem());
        oPersona.setLugar_de_procedencia(jtflugarprocedencia.getText());
        oPersona.setDomicilio(jtfDomicilio.getText());

        oHistoria_Clinica.setMotivoConsulta(jtf_motivo_consulta.getText());
        oHistoria_Clinica.setDoctor(jcbDoctor.getSelectionModel().getSelectedItem());
        oHistoria_Clinica.setExamen_radiografico(jtf_examen_radiografico.getText());
        oHistoria_Clinica.setDiagnostico(jtf_diagnostico.getText());

        //Enfermedad actual
        oHistoria_Clinica.setEnfermedadActual(jtfenfermedadActual.getText());
        oHistoria_Clinica.setExamenIntraoral(jtf_examen_intraoral.getText());
        oHistoria_Clinica.setAntecedentesFamiliares(jtfantecedentesPersonales.getText());
        //Exploración física
        //diagnostico
        //Plan de tratamiento
        //Pronóstico
        //Antecedentes
        //oPaciente.setAntecedentesFamiliares(jtfantecedentesFamiliares.getText());
        App.jpa.getTransaction().begin();
        for (Persona_Enfermedad oPacienteEnfermedad : list_enfermedad_persona) {
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
        
        
        for (Persona_Enfermedad enfermedad_persona : lista_enfermedades_persona) {
            boolean isDelete=true;
            for (Enfermedad enfermedad_volatil : lista_enfermedades_del_persona_volatil) {
                if(enfermedad_persona.getEnfermedad().getIdenfermedad()==enfermedad_volatil.getIdenfermedad()){
                    isDelete=false;
                }
            }
            if(isDelete){
                App.jpa.remove(enfermedad_persona);
            }      
        }
        
        for (Enfermedad enfermedad_persona : lista_enfermedades_del_persona_volatil) {
            boolean isAgregable=true;
            for (Persona_Enfermedad enfermedad_volatil : lista_enfermedades_persona) {
                if(enfermedad_volatil.getEnfermedad().getIdenfermedad()==enfermedad_persona.getIdenfermedad()){
                    isAgregable=false;
                }
            }
            if(isAgregable){
                App.jpa.persist(new Persona_Enfermedad(oPersona, enfermedad_persona, ""));
            }      
        }

        App.jpa.persist(oPersona);
       // App.jpa.persist(oPaciente);
        App.jpa.getTransaction().commit();
        cerrar();
        oVerPacienteController.updateListPersona();
        //oVerPacienteController.selectModificado(oPersona);
    }

    /*--Otras ventanas---*/
 /*--Otras ventanas fin---*/
    void initRestricciones() {
        jtfNombresyApellidos.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloLetras(event));
        jtfTelefono.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEntero(event));
        jtfDni.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros8(event));
        jtfDia.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros2(event));
        jtfMes.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros2(event));
        jtfanio.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros4(event));
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
    void cerrar() {
        oVerPacienteController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }
    /*------Fin Metodos de ventana---------------*/

}
