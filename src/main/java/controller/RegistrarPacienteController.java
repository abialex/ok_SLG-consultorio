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
import Entidades.PersonaReniec;
import Entidades.PlanTratamiento;
import Entidades.Pregunta;
import Entidades.Tratamiento;
import Util.FileImagUtil;
import Pdf.Historiaclinicapdf;
import Util.HttpMethods;
import com.google.gson.Gson;
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
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.SingleSelectionModel;
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
 * @author yalle
 */
public class RegistrarPacienteController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane ap;

    @FXML
    Accordion accordion;
    @FXML
    TitledPane tpAnamnesis;

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
    private JFXComboBox<Doctor> jcbDoctor;

    //examen auxiliar
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

    //II-Requerido
    @FXML
    private JFXTextField jtfenfermedadActual, jtfenfermedadSistemica, jtfantecedentesPersonales;

    //II. Enfermedad actual
    //III. Antecedentes
    //IV. Exploración fisica
    //V. Diagnostico
    //VI. Plan de tratamiento
    //VII. Pronostico
    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
    List<CheckBox> listcheck = new ArrayList<>();
    AlertController oAlert = new AlertController();
    VerPacienteController oVerPacienteController;
    ObservableList<PlanTratamiento> listPlanTratamiento = FXCollections.observableArrayList();
    ObservableList<ExamenAuxiliar> listExamenAuxiliar = FXCollections.observableArrayList();
    HttpMethods http = new HttpMethods();
    Gson json = new Gson();

    @FXML
    void consultar() {
        if (jtfDni.getText().length() == 8) {
            HttpResponse<String> response = http.consultarDNI(jtfDni.getText());
            if (response != null) {
                if (response.statusCode() == 200) {
                    PersonaReniec personReniec = json.fromJson(response.body(), PersonaReniec.class);
                    if (personReniec.getApellidoMaterno() == null) {
                        alertWarning.setHeaderText(null);
                        alertWarning.setTitle("Búsqueda");
                        alertWarning.setContentText("No se encontró DNI");
                        alertWarning.showAndWait();

                    } else {
                        jtfNombresyApellidos.setText(personReniec.getNombres() + " " + personReniec.getApellidoPaterno() + " " + personReniec.getApellidoMaterno());
                        jtfTelefono.setFocusTraversable(true);
                    }
                } else {
                    alertWarning.setHeaderText(null);
                    alertWarning.setTitle("Búsqueda");
                    alertWarning.setContentText("Error " + response.statusCode());
                    alertWarning.showAndWait();
                }

            }
            else{
                  alertWarning.setHeaderText(null);
                    alertWarning.setTitle("Búsqueda");
                    alertWarning.setContentText("Sin conexión a internet ");
                    alertWarning.showAndWait();
            }

        } else {
            alertWarning.setHeaderText(null);
            alertWarning.setTitle("Búsqueda");
            alertWarning.setContentText("Ingrese un DNI válido");
            alertWarning.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> OCUPACION = FXCollections.observableArrayList("ESTUDIANTE", "UNIVERSITARIO", "TRABAJADOR");
        ObservableList<String> SEXO = FXCollections.observableArrayList("VARÓN", "MUJER");
        jcbocupacion.setItems(OCUPACION);
        jcbsexo.setItems(SEXO);
        accordion.setExpandedPane(tpAnamnesis);
        //accordion2.setExpandedPane(tpEnfermedades);
        initRestricciones();
        cargarDoctor();
        initTablePlanTratamiento();
        initTableExamenesAuxiliares();
        tablePlandetratamiento.setItems(listPlanTratamiento);
        tableExamenAuxiliar.setItems(listExamenAuxiliar);
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
            opersona.setTutorDni("");
            opersona.setTutorNombre("");
            opersona.setTutorTelefono("");

            Paciente opaciente = new Paciente(
                    opersona,
                    "",
                    "",
                    "",
                    jtfantecedentesPersonales.getText().trim(),
                    jtfenfermedadActual.getText().trim(),
                    jtfenfermedadSistemica.getText().trim());
            opaciente.setEmergenciaNombre("");
            opaciente.setEmergenciaParentesco("");
            opaciente.setEmergenciaTelefono("");

            List<Paciente_Enfermedad> Lista_enfermedadesPaciente = new ArrayList<>();
            List<Paciente_Pregunta> Lista_preguntasPaciente = new ArrayList<>();

            Historia_clinica ohistoria = new Historia_clinica(
                    opaciente,
                    jcbDoctor.getSelectionModel().getSelectedItem(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    jtaConsulta.getText().trim(),
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

            for (PlanTratamiento oplantratamiento : listPlanTratamiento) {
                oplantratamiento.setHistoria_clinica(ohistoria);
                App.jpa.persist(oplantratamiento);
            }

            for (ExamenAuxiliar oexam : listExamenAuxiliar) {
                oexam.setHistoria_clinica(ohistoria);
                App.jpa.persist(oexam);
            }

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

    void setController(VerPacienteController aThis) {
        this.oVerPacienteController = aThis;
        jtflugarprocedencia.setText("Huanta");
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    @FXML
    void agregarPlanTratamiento() {
        if (jtfPlandetratamiento.getText().length() != 0) {
            PlanTratamiento oplantratamiento = new PlanTratamiento();
            oplantratamiento.setDescripcion(jtfPlandetratamiento.getText());
            listPlanTratamiento.add(oplantratamiento);
            jtfPlandetratamiento.setStyle("");
            jtfPlandetratamiento.setText("");
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
            listExamenAuxiliar.add(oExamenAuxiliar);
            jtfExamenAuxiliar.setStyle("");
            jtfExamenAuxiliar.setText("");
        } else {
            jtfExamenAuxiliar.setStyle("-fx-border-color: #ff052b");
            alertWarning.setHeaderText(null);
            alertWarning.setTitle("Examen Auxiliar");
            alertWarning.setContentText("Espacio en blanco");
            alertWarning.showAndWait();
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
                    ExamenAuxiliar exa = (ExamenAuxiliar) imag.getUserData();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Info");
                    alert.setContentText("¿Desea eliminar el plan de tratamiento: " + exa.getDescripcion() + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        listExamenAuxiliar.remove(exa);
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

    void initRestricciones() {
        jtfNombresyApellidos.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloLetras(event));
        jtfTelefono.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEntero9(event));
        jtfDni.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros8(event));
        jtfDia.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros2(event));
        jtfMes.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros2(event));
        jtfanio.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros4(event));

    }

    void SoloNumerosEntero9(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 9) {
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
        /*
        if (jtaConsulta.getText().trim().length() == 0) {
            jtaConsulta.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtaConsulta.setStyle("");
        }*/

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
        /*
        if (jtfenfermedadActual.getText().trim().length() == 0) {
            jtfenfermedadActual.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtfenfermedadActual.setStyle("");
        }

        if (jtfenfermedadSistemica.getText().trim().length() == 0) {
            jtfenfermedadSistemica.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtfenfermedadSistemica.setStyle("");
        }

        if (jtfantecedentesPersonales.getText().trim().length() == 0) {
            jtfantecedentesPersonales.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtfantecedentesPersonales.setStyle("");
        }*/

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
