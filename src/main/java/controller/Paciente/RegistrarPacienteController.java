/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller.Paciente;

import Entidades.*;
import Util.HttpMethods;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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

    @FXML
    HBox hbox_enfermedad;

    //I. Amnemesis
    @FXML
    private JFXTextField jtfNombresyApellidos, jtf_ap_paterno, jtf_ap_materno, jtfDni, jtfTelefono;
    @FXML
    private JFXComboBox<String> jcbsexo;
    @FXML
    private JFXTextField jtfDia, jtfMes, jtfanio;
    @FXML
    private JFXComboBox<String> jcbocupacion;
    @FXML
    private JFXTextField jtflugarprocedencia, jtfDomicilio;
    @FXML
    private JFXTextField jtf_motivo_consulta, jtf_examen_radiografico, jtf_diagnostico;
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
    private JFXTextField jtfenfermedadActual, jtf_examen_intraoral, jtfantecedentesPersonales;

    //II. Enfermedad actual
    //III. Antecedentes
    //IV. Exploración fisica
    //V. Diagnostico
    //VI. Plan de tratamiento
    //VII. Pronostico
    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
    List<CheckBox> listcheck = new ArrayList<>();
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
                        jtfNombresyApellidos.setText(personReniec.getNombres());
                        jtf_ap_paterno.setText(personReniec.getApellidoPaterno());
                        jtf_ap_materno.setText(personReniec.getApellidoMaterno());
                        jtfTelefono.setFocusTraversable(true);
                    }
                } else {
                    alertWarning.setHeaderText(null);
                    alertWarning.setTitle("Búsqueda");
                    alertWarning.setContentText("Error " + response.statusCode());
                    alertWarning.showAndWait();
                }

            } else {
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
        cargarEnfermedad();
        initTablePlanTratamiento();
        initTableExamenesAuxiliares();
        tablePlandetratamiento.setItems(listPlanTratamiento);
        tableExamenAuxiliar.setItems(listExamenAuxiliar);
    }

    void cargarDoctor() {
        List<Doctor> listDoctorG = http.getList(Doctor.class, "cita/DoctorAll");
        ObservableList<Doctor> listDoctor = FXCollections.observableArrayList();
        for (Doctor odoct : listDoctorG) {
            listDoctor.add(odoct);
        }
        jcbDoctor.setItems(listDoctor);
    }
    List<Enfermedad> lista_de_enfermedades_del_paciente = new ArrayList<>();

    void cargarEnfermedad() {
        List<Enfermedad> listEnfermedad = http.getList(Enfermedad.class,"historia_clinica/EnfermedadList");
        EventHandler evento = (event) -> {
            //Codigo de la función evento
            JFXCheckBox oCheck = ((JFXCheckBox) event.getSource());
            if (oCheck.isSelected()) {
                lista_de_enfermedades_del_paciente.add((Enfermedad) oCheck.getUserData());
            } else {
                lista_de_enfermedades_del_paciente.remove((Enfermedad) oCheck.getUserData());
            }
            for (Enfermedad enfermedad : lista_de_enfermedades_del_paciente) {
                System.out.println(enfermedad.getNombre());
            }
        };
        for (Enfermedad oEnferm : listEnfermedad) {
            JFXCheckBox JB = new JFXCheckBox();
            Label lbl = new Label();
            lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            lbl.setText(oEnferm.getNombre());
            JB.setOnAction(evento);
            Insets oInsetslbl = new Insets(0, 15, 0, 15);
            hbox_enfermedad.setMargin(lbl, oInsetslbl);
            JB.setStyle("-fx-background-color: white;\n" + "-fx-border-color:grey;\n" + "-fx-border-radius:3px; ");
            hbox_enfermedad.getChildren().add(JB);
            hbox_enfermedad.getChildren().add(lbl);
            hbox_enfermedad.getChildren();
            JB.setUserData(oEnferm);
        }
    }

    @FXML
    void GuardarPaciente(ActionEvent evt) throws IOException {
        if (isCompleto()) {
            JsonObject responseJSON = new JsonObject();
            JsonObject opersonaJSON = new JsonObject();
            Persona persona =new Persona();

            persona.setDni(jtfDni.getText());
            persona.setNombres(jtfNombresyApellidos.getText());
            persona.setAp_paterno(jtf_ap_paterno.getText());
            persona.setAp_materno(jtf_ap_materno.getText());
            persona.setTelefono(jtfTelefono.getText());
            persona.setSexo(jcbsexo.getSelectionModel().getSelectedItem());
            persona.setFecha_cumple(LocalDate.of(Integer.parseInt(jtfanio.getText()), Integer.parseInt(jtfMes.getText()), Integer.parseInt(jtfDia.getText())));
            persona.setOcupacion(jcbocupacion.getSelectionModel().getSelectedItem());
            persona.setLugar_de_procedencia(jtflugarprocedencia.getText());
            persona.setDomicilio(jtfDomicilio.getText());

            opersonaJSON.addProperty("dni", jtfDni.getText());
            opersonaJSON.addProperty("nombres", jtfNombresyApellidos.getText());
            opersonaJSON.addProperty("ap_paterno", jtf_ap_paterno.getText());
            opersonaJSON.addProperty("ap_materno", jtf_ap_materno.getText());
            opersonaJSON.addProperty("telefono", jtfTelefono.getText());
            opersonaJSON.addProperty("sexo", jcbsexo.getSelectionModel().getSelectedItem());
            opersonaJSON.addProperty("fecha_cumple_formato", LocalDate.of(Integer.parseInt(jtfanio.getText()), Integer.parseInt(jtfMes.getText()), Integer.parseInt(jtfDia.getText())).toString());
            opersonaJSON.addProperty("ocupacion", jcbocupacion.getSelectionModel().getSelectedItem());
            opersonaJSON.addProperty("lugar_de_procedencia", jtflugarprocedencia.getText());
            opersonaJSON.addProperty("domicilio", jtfDomicilio.getText());
            responseJSON.add("persona", opersonaJSON);

            JsonObject ohistoria_clinicaJSON = new JsonObject();
            Historia_clinica historia_clinica = new Historia_clinica();
            historia_clinica.setDoctor(jcbDoctor.getSelectionModel().getSelectedItem());
            historia_clinica.setMotivo_consulta(jtf_motivo_consulta.getText());
            historia_clinica.setEnfermedad_actual(jtfenfermedadActual.getText());
            historia_clinica.setExamen_intraoral(jtf_examen_intraoral.getText());
            historia_clinica.setExamen_radiografico(jtf_examen_radiografico.getText());
            historia_clinica.setAntecedentes(jtfantecedentesPersonales.getText());
            historia_clinica.setDiagnostico(jtf_diagnostico.getText());
            historia_clinica.setPersona(persona);

            ohistoria_clinicaJSON.addProperty("doctor_id",  jcbDoctor.getSelectionModel().getSelectedItem().getIddoctor());
            ohistoria_clinicaJSON.addProperty("motivo_consulta", jtf_motivo_consulta.getText());
            ohistoria_clinicaJSON.addProperty("enfermedad_actual", jtfenfermedadActual.getText());
            ohistoria_clinicaJSON.addProperty("examen_intraoral", jtf_examen_intraoral.getText());
            ohistoria_clinicaJSON.addProperty("examen_radiografico", jtf_examen_radiografico.getText());
            ohistoria_clinicaJSON.addProperty("antecedentes", jtfantecedentesPersonales.getText());
            ohistoria_clinicaJSON.addProperty("diagnostico", jtf_diagnostico.getText());
            responseJSON.add("historia_clinica", ohistoria_clinicaJSON);

            JsonArray list_enfermedades_del_persona = new JsonArray();
            for (Enfermedad oenfermedad :lista_de_enfermedades_del_paciente){
                JsonObject enfermedadJSON = new JsonObject();
                enfermedadJSON.addProperty("idenfermedad", oenfermedad.getIdenfermedad());
                enfermedadJSON.addProperty("nombre", oenfermedad.getNombre());
                list_enfermedades_del_persona.add(enfermedadJSON);
            }
            responseJSON.add("list_enfermedad", list_enfermedades_del_persona);

            JsonArray list_examen_auxiliar=new JsonArray();
            for (ExamenAuxiliar oexamen_auxiliar : listExamenAuxiliar) {
                JsonObject examen_auxiliarJSON = new JsonObject();
                examen_auxiliarJSON.addProperty("descripcion", oexamen_auxiliar.getDescripcion());
                list_examen_auxiliar.add(examen_auxiliarJSON);
            }
            responseJSON.add("list_examen_auxiliar", list_examen_auxiliar);

            JsonArray list_plan_tratamiento = new JsonArray();
            for(PlanTratamiento oplan_tratamiento:listPlanTratamiento){
                JsonObject plan_tratamientoJSON = new JsonObject();
                plan_tratamientoJSON.addProperty("descripcion", oplan_tratamiento.getDescripcion());
                list_plan_tratamiento.add(plan_tratamientoJSON);
            }
            responseJSON.add("list_plan_tratamiento", list_plan_tratamiento);

            HttpResponse<String> response=http.AddObjects(responseJSON,"historia_clinica/RegistrarHistoriaClinica");
            if(response.statusCode()==200){
               JsonObject jsonResponse =json.fromJson(response.body(), JsonObject.class);
                historia_clinica.setIdhistoria_clinica(jsonResponse.get("idhistoria_clinica").getAsInt());
                persona.setIdpersona(jsonResponse.get("idpersona").getAsInt());
                oVerPacienteController.addHistoriaClinica(historia_clinica);
                oVerPacienteController.displayPaginas(true);
                oVerPacienteController.selectAgregado();
            }
            cerrar();
        }
    }

    void setController(VerPacienteController aThis
    ) {
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
        Label columNumeader = new Label("N°");
        columNumeader.setStyle("-fx-text-fill: white");
        Label columDescripcionHeader = new Label("Plan de Tratamiento");
        columDescripcionHeader.setStyle("-fx-text-fill: white");
        columnNum.setCellValueFactory(new PropertyValueFactory<PlanTratamiento, String>("descripcion"));
        columnPlanTratamiento.setCellValueFactory(new PropertyValueFactory<PlanTratamiento, String>("descripcion"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<PlanTratamiento, PlanTratamiento>("PlanTratamiento"));
        columnNum.setGraphic(columNumeader);
        columnPlanTratamiento.setGraphic(columDescripcionHeader);

        columnPlanTratamiento.setCellFactory(column -> {
            TableCell<PlanTratamiento, String> cell = new TableCell<PlanTratamiento, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();
                        label.setText(item);
                        label.setStyle("-fx-text-fill: white;");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });

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
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999; -fx-text-fill: white;s");
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
        Label columNumeader = new Label("N°");
        columNumeader.setStyle("-fx-text-fill: white");
        Label columDescripcionHeader = new Label("Examen Auxiliar");
        columDescripcionHeader.setStyle("-fx-text-fill: white");
        columnNumExamenAuxiliar.setCellValueFactory(new PropertyValueFactory<ExamenAuxiliar, String>("descripcion"));
        columnExamenAuxiliar.setCellValueFactory(new PropertyValueFactory<ExamenAuxiliar, String>("descripcion"));
        columnEstadoExamenAuxiliar.setCellValueFactory(new PropertyValueFactory<ExamenAuxiliar, ExamenAuxiliar>("PlanTratamiento"));
        columnNumExamenAuxiliar.setGraphic(columNumeader);
        columnExamenAuxiliar.setGraphic(columDescripcionHeader);

        columnExamenAuxiliar.setCellFactory(column -> {
            TableCell<ExamenAuxiliar, String> cell = new TableCell<ExamenAuxiliar, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();
                        label.setText(item);
                        label.setStyle("-fx-text-fill: white;");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });

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
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999; -fx-text-fill: white;");
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

    void SoloNumerosEntero9(KeyEvent event
    ) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 9) {
            event.consume();
        }
    }

    void SoloNumerosEnteros2(KeyEvent event
    ) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 2) {
            event.consume();
        }
    }

    void SoloNumerosEnteros4(KeyEvent event
    ) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 4) {
            event.consume();
        }
    }

    void SoloNumerosEnteros8(KeyEvent event
    ) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 8) {
            event.consume();
        }
    }

    void SoloLetras(KeyEvent event
    ) {
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
        }
        return aux;
    }

    @FXML
    void cerrar() {
        oVerPacienteController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }
}
