/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.*;
import Util.HttpMethods;
import Util.UtilClass;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertConfirmarController;
import emergente.AlertController;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class CajaVerController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private TableView<Tratamiento> tableTratamiento;

    @FXML
    private TableColumn<Tratamiento, LocalDate> columnFecha;

    @FXML
    private TableColumn<Tratamiento, String> columnTratamiento;

    @FXML
    private TableColumn<Tratamiento, Integer> ColumnMonto;

    @FXML
    private TableColumn<Tratamiento, Doctor> ColumnDoctor;

    @FXML
    private TableColumn<Tratamiento, Tratamiento> ColumnEstado;

    @FXML
    private TableView<Detalle_Presupuesto> tablePresupuesto;

    @FXML
    private TableColumn<Detalle_Presupuesto, String> columnDetallePresupuesto;

    @FXML
    private TableColumn<Detalle_Presupuesto, Float> columnMontoPresupuesto;

    @FXML
    private TableColumn<Detalle_Presupuesto, Integer> columnCantidad;

    @FXML
    private TableColumn<Detalle_Presupuesto, Detalle_Presupuesto> columnTotal;

    @FXML
    private JFXTextField jtfTratamiento, jtfMonto;

    @FXML
    private Label lblnombre, lblMontototal, lblAviso;

    @FXML
    private JFXButton btnAgregar;

    @FXML
    private JFXComboBox<Doctor> jcb_doctor;

    double x = 0, y = 0;
    Persona oPersona;
    ObservableList<Tratamiento> listTratamiento = FXCollections.observableArrayList();
     ObservableList<Detalle_Presupuesto> listPresupuesto = FXCollections.observableArrayList();
    Stage stagePrincipal;
    AlertConfirmarController oAlertConfimarController = new AlertConfirmarController();
    CajaVerController odc = this;
    Tratamiento oTratamientoEliminar;
    HttpMethods http = new HttpMethods();
    int indexEliminar;
    AlertController oAlertController = new AlertController();
    VerPacienteController oVerPacienteController;
    Presupuesto oPresupuesto;
    float MontoTotal = 0;
    float acumMonto = 0;
    List<Tratamiento> olistTratamiento_response;
    ObservableList<Doctor> olistDoctor = FXCollections.observableArrayList();
    UtilClass oUtilClass = new UtilClass();
    Historia_clinica oHistoria_clinica=new Historia_clinica();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // el resto de init está en setPersona()
        initRestricciones();
        cargar_doctores();


    }
    void cargar_doctores() {
        List<Doctor> olistDoctor_response = http.getList(Doctor.class, "cita/DoctorAll");
        for (Doctor oDoctor : olistDoctor_response) {
            olistDoctor.add(oDoctor);
        }
        jcb_doctor.setItems(olistDoctor);
    }

    @FXML
    void guardarTratamiento() {
        float montoacum = 0;
        for (Tratamiento tratamiento : listTratamiento) {
            montoacum = montoacum + tratamiento.getMonto();
        }

        if (isCompleto()) {
            float resta = MontoTotal - (montoacum + Integer.parseInt(jtfMonto.getText()));
            if (resta >= 0) {
                Tratamiento otratamiento = new Tratamiento(
                        oHistoria_clinica,
                        jcb_doctor.getSelectionModel().getSelectedItem(),
                        LocalDate.now(),
                        jtfTratamiento.getText(),
                        Integer.parseInt(jtfMonto.getText()));
                listTratamiento.add(otratamiento);
                updateMontoAviso();
                initTable();
                limpiar();

                oUtilClass.mostrar_alerta_success("Exitoso","Tratamiento guardado correctamente");
            } else {
                oUtilClass.mostrar_alerta_error("Error", "Se excedio el monto total");

            }
        } else {
            oUtilClass.mostrar_alerta_error("Error", "Llene los espacios en blanco");
        }
    }

    void setPersona(Historia_clinica ohistoria_clinica, Presupuesto opresupuesto) {
        //Initialize
        this.oPersona = ohistoria_clinica.getPersona();
        this.oPresupuesto =  opresupuesto;
        this.oHistoria_clinica = ohistoria_clinica;
        lblMontototal.setText(oPresupuesto.getMonto_total() + "");
        lblnombre.setText(ohistoria_clinica.getPersona().getNombres()+" "+ ohistoria_clinica.getPersona().getAp_paterno()+ " " + ohistoria_clinica.getPersona().getAp_materno());
        MontoTotal = oPresupuesto.getMonto_total();
        tableTratamiento.setItems(listTratamiento);
        updateListaTratamiento();
        getListaPresupuesto(ohistoria_clinica.getPersona());
        initTable();
        initTablePresupuesto();
        tableTratamiento.setItems(listTratamiento);
        tablePresupuesto.setItems(listPresupuesto);
    }

    @FXML
    void actualizar_tratamiento() {
        Optional<ButtonType> result= oUtilClass.mostrar_confirmación("info","¿Desea guardar el tratamiento?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            JsonObject jsonResponse=new JsonObject();
            JsonArray jsonDetalle_presupuesto=new JsonArray();

            for (Tratamiento otratamiento : listTratamiento) {
                JsonObject jsonDetalle_presupuesto_aux=new JsonObject();
                jsonDetalle_presupuesto_aux.addProperty("idtratamiento", otratamiento.getIdtratamiento());
                jsonDetalle_presupuesto_aux.addProperty("historia_clinica_id", otratamiento.getHistoria_clinica().getIdhistoria_clinica());
                jsonDetalle_presupuesto_aux.addProperty("doctor_id", otratamiento.getDoctor().getIddoctor());
                jsonDetalle_presupuesto_aux.addProperty("nombre", otratamiento.getNombre());
                jsonDetalle_presupuesto_aux.addProperty("fecha_realizada_formato", otratamiento.getFecha_realizada()+"");
                jsonDetalle_presupuesto_aux.addProperty("monto", otratamiento.getMonto());
                jsonDetalle_presupuesto.add(jsonDetalle_presupuesto_aux);
            }
            jsonResponse.addProperty("historia_clinica_id", oHistoria_clinica.getIdhistoria_clinica());
            jsonResponse.add("list_tratamiento", jsonDetalle_presupuesto);
            System.out.println(jsonResponse);
            HttpResponse<String> response = http.AddObjects(jsonResponse,"historia_clinica/TratamientoUpdate");
            switch (response.statusCode()) {
                case 200:
                    oUtilClass.mostrar_alerta_success("info", "Tratamientos guardados con éxito");
                    cerrar();
                    break;
                case 400:
                    oUtilClass.mostrar_alerta_error("error", response.body());
                    break;
                case 404:
                    oUtilClass.mostrar_alerta_error("error", "no encontrado");
                    break;
                case 500:
                    oUtilClass.mostrar_alerta_error("error", response.body());
                    break;
            }

        }
    }

    void getListaPresupuesto(Persona opersona) {
        List<Detalle_Presupuesto> olistDetallePresupuesto = http.getList(Detalle_Presupuesto.class, "historia_clinica/DetallePresupuestoList/"+oPresupuesto.getIdpresupuesto());
        for (Detalle_Presupuesto detalle_Presupuesto : olistDetallePresupuesto) {
            listPresupuesto.add(detalle_Presupuesto);
            
        }
    }

    void setController(VerPacienteController odc) {
        this.oVerPacienteController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    @FXML
    void updateListaTratamiento() {
        olistTratamiento_response =  http.getList(Tratamiento.class, "historia_clinica/TratamientoList/"+oPresupuesto.getHistoria_clinica().getIdhistoria_clinica()+"");
        listTratamiento.clear();
        acumMonto = 0;
        for (Tratamiento otratamiento : olistTratamiento_response) {
            listTratamiento.add(otratamiento);
            acumMonto = acumMonto + otratamiento.getMonto();
        }
        if (MontoTotal == acumMonto) {
            btnAgregar.setDisable(true);
        } else {
            btnAgregar.setDisable(false);
        }
        updateMontoAviso();

    }

    void updateMontoAviso() {
        int acumMontoTotal = 0;
        for (Tratamiento otratamiento : listTratamiento) {
            acumMontoTotal = acumMontoTotal + otratamiento.getMonto();
        }
        lblAviso.setText("SALDO: " + (MontoTotal - acumMontoTotal));
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

    void initTable() {
        columnFecha.setCellValueFactory(new PropertyValueFactory<Tratamiento, LocalDate>("fecha_realizada"));
        columnTratamiento.setCellValueFactory(new PropertyValueFactory<Tratamiento, String>("nombre"));
        ColumnMonto.setCellValueFactory(new PropertyValueFactory<Tratamiento, Integer>("monto"));
        ColumnDoctor.setCellValueFactory(new PropertyValueFactory<Tratamiento, Doctor>("doctor"));
        ColumnEstado.setCellValueFactory(new PropertyValueFactory<Tratamiento, Tratamiento>("tratamiento"));

        ColumnMonto.setCellFactory(column -> {
            TableCell<Tratamiento, Integer> cell = new TableCell<Tratamiento, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();
                        label.setText(item + "");
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999; ");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });

        ColumnDoctor.setCellFactory(column -> {
            TableCell<Tratamiento, Doctor> cell = new TableCell<Tratamiento, Doctor>() {
                @Override
                protected void updateItem(Doctor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Label label = new Label();
                        //MontoTotal = MontoTotal - item;
                        label.setText( item.getPersona().getNombres() + " " + item.getPersona().getAp_paterno());
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999; ");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });

        Callback<TableColumn<Tratamiento, Tratamiento>, TableCell<Tratamiento, Tratamiento>> cellFoctory = (TableColumn<Tratamiento, Tratamiento> param) -> {
            // make cell containing buttons
            final TableCell<Tratamiento, Tratamiento> cell = new TableCell<Tratamiento, Tratamiento>() {

                @Override
                public void updateItem(Tratamiento item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows                    
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        int tamHightImag = 23;
                        int tamWidthImag = 23;
                        ImageView deleteIcon = new ImageView(new Image(getClass().getResource("/imagenes/delete-1.png").toExternalForm()));
                        deleteIcon.setFitHeight(tamHightImag);
                        deleteIcon.setFitWidth(tamWidthImag);
                        deleteIcon.setUserData(item);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand;"
                        );
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarEliminar(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagEliminarFuera(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagEliminarMoved(event));
                        //deleteIcon.setText("Eliminar");

                        ImageView editIcon = new ImageView(new Image(getClass().getResource("/imagenes/modify-1.png").toExternalForm()));
                        editIcon.setFitHeight(tamHightImag);
                        editIcon.setFitWidth(tamWidthImag);
                        editIcon.setUserData(item);
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                        );
                        editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarModificar(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagModificarMoved(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagModificarFuera(event));

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(editIcon, new Insets(0, 2.5, 0, 2.5));
                        HBox.setMargin(deleteIcon, new Insets(0, 2.5, 0, 2.5));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

                void mostrarModificar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    Tratamiento otratamiento = (Tratamiento) imag.getUserData();
                    CajaModificarController oCajaModificarController = (CajaModificarController) mostrarVentana(CajaModificarController.class, "CajaModificar");
                    oCajaModificarController.setTratamiento(otratamiento, MontoTotal - acumMonto, olistDoctor);
                    oCajaModificarController.setController(odc);
                    lockedPantalla();
                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    Tratamiento otratamiento = (Tratamiento) imag.getUserData();
                    Optional<ButtonType> result= oUtilClass.mostrar_confirmación("Eliminar Tratamiento", "¿Está seguro que desea eliminar el tratamiento?");
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        for (Tratamiento otratamiento_o : listTratamiento) {
                            if (otratamiento_o.getIdtratamiento()==(otratamiento.getIdtratamiento())){
                                listTratamiento.remove(otratamiento_o);
                                initTable();
                                updateMontoAviso();
                                break;
                            }
                        }
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

                private void imagModificarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/modify-2.png").toExternalForm()));
                }

                private void imagModificarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/modify-1.png").toExternalForm()));
                }
            };
            return cell;
        };
        ColumnEstado.setCellFactory(cellFoctory);

    }

    void initTablePresupuesto() {
        columnDetallePresupuesto.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, String>("descripcion"));
        columnMontoPresupuesto.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, Float>("monto"));
        columnCantidad.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, Integer>("cantidad"));
        columnTotal.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, Detalle_Presupuesto>("Detalle_Presupuesto"));

        columnDetallePresupuesto.setCellFactory(column -> {
            TableCell<Detalle_Presupuesto, String> cell = new TableCell<Detalle_Presupuesto, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();
                        label.setText(item+ "");
                        label.setStyle("-fx-font-size: 10; -fx-alignment: CENTER_LEFT; -fx-max-width:999; ");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });

        columnTotal.setCellFactory(column -> {
            TableCell<Detalle_Presupuesto, Detalle_Presupuesto> cell = new TableCell<Detalle_Presupuesto, Detalle_Presupuesto>() {
                @Override
                protected void updateItem(Detalle_Presupuesto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();
                        label.setText(item.getCantidad()*item.getMonto() + "");
                        label.setStyle("-fx-font-size: 10; -fx-alignment: CENTER_LEFT; -fx-max-width:999; ");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });
        
        columnCantidad.setCellFactory(column -> {
            TableCell<Detalle_Presupuesto, Integer> cell = new TableCell<Detalle_Presupuesto, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();
                        label.setText(item+ "");
                        label.setStyle("-fx-font-size: 10; -fx-alignment: center; -fx-max-width:999; ");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });
        
        columnMontoPresupuesto.setCellFactory(column -> {
            TableCell<Detalle_Presupuesto, Float> cell = new TableCell<Detalle_Presupuesto, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();
                        label.setText(item+ "");
                        label.setStyle("-fx-font-size: 10; -fx-alignment: center; -fx-max-width:999; ");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });

    }

    public void eliminar() {
        if (indexEliminar != -1) {
            oTratamientoEliminar.setFlag(true);
            //App.jpa.getTransaction().begin();
            //App.jpa.persist(oTratamientoEliminar);
            //App.jpa.getTransaction().commit();
            listTratamiento.remove(indexEliminar);
            updateListaTratamiento();
        }
    }

    void limpiar() {
        jtfMonto.setText("");
        jtfTratamiento.setText("");
    }

    public void lockedPantalla() {
        if (ap.isDisable()) {
            ap.setDisable(false);
        } else {
            ap.setDisable(true);
        }
    }

    @FXML
    void cerrar() {
        oVerPacienteController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();//cerrando la ventanada anterior
    }

    public Object mostrarVentana(Class generico, String nameFXML) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(generico.getResource(nameFXML + ".fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(generico.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);//instancia el controlador (!)
        scene.getStylesheets().add(VerPacienteController.class.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner((Stage) ap.getScene().getWindow());
        stage.setScene(scene);
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
        return loader.getController();
    }

    boolean isCompleto() {
        boolean aux = true;
        if (jcb_doctor.getSelectionModel().getSelectedItem() == null) {
            jcb_doctor.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jcb_doctor.setStyle("");
        }

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
