/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Persona;
import Entidades.Detalle_Presupuesto;
import Entidades.Historia_clinica;
import Entidades.Presupuesto;
import Util.HttpMethods;
import Util.UtilClass;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertConfirmarController;
import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
 * @author alexis
 */
public class PresupuestoVerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane ap;

    @FXML
    private TableView<Detalle_Presupuesto> tableTratamiento;

    @FXML
    private TableColumn<Detalle_Presupuesto, String> columnTratamiento;

    @FXML
    private TableColumn<Detalle_Presupuesto, Integer> ColumnCantidad;

    @FXML
    private TableColumn<Detalle_Presupuesto, Integer> ColumnMonto;

    @FXML
    private TableColumn<Detalle_Presupuesto, Detalle_Presupuesto> ColumnEstado;

    @FXML
    private JFXTextField jtfDescripcion;

    @FXML
    private JFXTextField jtfMonto;

    @FXML
    private Label lblnombre;

    @FXML
    private Label lblMontototal;

    @FXML
    private JFXTextField jtfCantidad;

    @FXML
    private JFXButton btnGuardarPresupuesto, btnAgregar;

    Persona oPersona;
    double x = 0, y = 0;
    VerPacienteController oVerPacienteController;
    ObservableList<Detalle_Presupuesto> list_Detalle_presupuesto = FXCollections.observableArrayList();
    PresupuestoVerController odc = this;
    AlertConfirmarController oAlertConfimarController = new AlertConfirmarController();
    Presupuesto oPresupuesto;

    int indexEliminar;
    Historia_clinica oHistoriaclinica;
    HttpMethods http = new HttpMethods();
    UtilClass oUtilClass = new UtilClass();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initRestricciones();
    }



    void setPersona(Persona opersona, Historia_clinica ohistoria_clinica) {
        //Initialize
        this.oHistoriaclinica = ohistoria_clinica;
        this.oPersona = opersona;
        this.oPresupuesto = http.ConsultObject(Presupuesto.class, "historia_clinica/GetPresupuesto",ohistoria_clinica.getIdhistoria_clinica()+"" );
        if (oPresupuesto!= null) {
            update_list_detalle_presuesto();
        }
        else{
            oPresupuesto = new Presupuesto();
        }

        lblnombre.setText(opersona.getNombres()+" "+opersona.getAp_paterno());
        initTable();
        tableTratamiento.setItems(list_Detalle_presupuesto);
    }

    void setController(VerPacienteController odc) {
        this.oVerPacienteController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    public void lockedPantalla() {
        if (ap.isDisable()) {
            ap.setDisable(false);
        } else {
            ap.setDisable(true);
        }
    }

    @FXML
    void cerrarPresupuesto() {
        if (!list_Detalle_presupuesto.isEmpty()) {
            Optional<ButtonType> result= oUtilClass.mostrar_confirmación("info","¿Desea guardar el presupuesto?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //guardar presupuesto con la lista de detalle de presupuesto
                JsonObject jsonResponse=new JsonObject();
                JsonObject jsonPresupuesto=new JsonObject();
                jsonPresupuesto.addProperty("idpresupuesto", oPresupuesto.getIdpresupuesto());
                jsonPresupuesto.addProperty("historia_clinica_id",oHistoriaclinica.getIdhistoria_clinica());
                jsonPresupuesto.addProperty("monto_total", oPresupuesto.getMonto_total());
                jsonResponse.add("presupuesto", jsonPresupuesto);


                JsonArray jsonDetalle_presupuesto=new JsonArray();
                for (Detalle_Presupuesto oDetalle_presupuesto : list_Detalle_presupuesto) {
                    JsonObject jsonDetalle_presupuesto_aux=new JsonObject();
                    jsonDetalle_presupuesto_aux.addProperty("iddetalle_presupuesto", oDetalle_presupuesto.getIddetalle_presupuesto());
                    jsonDetalle_presupuesto_aux.addProperty("presupuesto_id", oPresupuesto.getIdpresupuesto());
                    jsonDetalle_presupuesto_aux.addProperty("descripcion", oDetalle_presupuesto.getDescripcion());
                    jsonDetalle_presupuesto_aux.addProperty("cantidad", oDetalle_presupuesto.getCantidad());
                    jsonDetalle_presupuesto_aux.addProperty("monto", oDetalle_presupuesto.getMonto());
                    jsonDetalle_presupuesto.add(jsonDetalle_presupuesto_aux);
                }
                jsonResponse.add("list_detalle_presupuesto", jsonDetalle_presupuesto);
                HttpResponse<String> response = http.AddObjects(jsonResponse,"historia_clinica/PresupuestoUpdate");
                switch (response.statusCode()) {
                    case 200:
                        oUtilClass.mostrar_alerta_success("info", "Presupuesto guardado con éxito");
                        cerrar();
                        break;
                    case 400:
                        oUtilClass.mostrar_alerta_error("error", response.body());
                        break;
                    case 404:
                        oUtilClass.mostrar_alerta_error("error", "El presupuesto no existe");
                        break;
                    case 500:
                        oUtilClass.mostrar_alerta_error("error", response.body());
                        break;
                }

            }
        } else {
            oUtilClass.mostrar_alerta_warning("info","No ha asignado un presupuesto" );
        }
    }

    @FXML
    void agregar_detalle_presupuesto() {
        if (isCompleto()) {
            Detalle_Presupuesto odetalle_presupuesto = new Detalle_Presupuesto(oPresupuesto,
                    jtfDescripcion.getText(),
                    Integer.parseInt(jtfCantidad.getText()),
                    Integer.parseInt(jtfMonto.getText()));
            oPresupuesto.setMonto_total(oPresupuesto.getMonto_total() +( Integer.parseInt(jtfMonto.getText()) * Integer.parseInt(jtfCantidad.getText())));
            list_Detalle_presupuesto.add(odetalle_presupuesto);
            initTable();
            updateMontoAviso();
            limpiar();

        } else {
            oUtilClass.mostrar_alerta_error("error","Debe completar todos los campos");
        }
    }

    @FXML
    void update_list_detalle_presuesto() {
        List<Detalle_Presupuesto> olist_detalle_presupuesto = http.getList(Detalle_Presupuesto.class, "historia_clinica/DetallePresupuestoList/"+oPresupuesto.getIdpresupuesto());
        list_Detalle_presupuesto.clear();
        for (Detalle_Presupuesto opresupuesto : olist_detalle_presupuesto) {
            list_Detalle_presupuesto.add(opresupuesto);
        }
        updateMontoAviso();
    }

    public void updateMontoAviso() {
        int acumMontoTotal = 0;
        for (Detalle_Presupuesto opresupuesto : list_Detalle_presupuesto) {
            acumMontoTotal = acumMontoTotal + opresupuesto.getMonto() * opresupuesto.getCantidad();
        }
        lblMontototal.setText(acumMontoTotal + "");
    }

    void initRestricciones() {
        //jtfDescripcion.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloLetras(event));
        jtfMonto.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros8(event));
        jtfCantidad.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros8(event));

    }

    void SoloLetras(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (Character.isDigit(key)) {
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

    void initTable() {
        ColumnCantidad.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, Integer>("cantidad"));
        columnTratamiento.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, String>("descripcion"));
        ColumnMonto.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, Integer>("monto"));
        ColumnEstado.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, Detalle_Presupuesto>("DetallePresupuesto"));

        ColumnCantidad.setCellFactory(column -> {
            TableCell<Detalle_Presupuesto, Integer> cell = new TableCell<Detalle_Presupuesto, Integer>() {
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


        ColumnMonto.setCellFactory(column -> {
            TableCell<Detalle_Presupuesto, Integer> cell = new TableCell<Detalle_Presupuesto, Integer>() {
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

        Callback<TableColumn<Detalle_Presupuesto, Detalle_Presupuesto>, TableCell<Detalle_Presupuesto, Detalle_Presupuesto>> cellFoctory = (TableColumn<Detalle_Presupuesto, Detalle_Presupuesto> param) -> {
            // make cell containing buttons
            final TableCell<Detalle_Presupuesto, Detalle_Presupuesto> cell = new TableCell<Detalle_Presupuesto, Detalle_Presupuesto>() {

                @Override
                public void updateItem(Detalle_Presupuesto item, boolean empty) {
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
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagEliminarMoved(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagEliminarFuera(event));
       

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
                    Detalle_Presupuesto oDetalle_Presupuesto = (Detalle_Presupuesto) imag.getUserData();
                    PresupuestoModificarController oPresupuestoModificarController = (PresupuestoModificarController) mostrarVentana(PresupuestoModificarController.class, "PresupuestoModificar");
                   oPresupuestoModificarController.setDetallePresupuesto(oDetalle_Presupuesto);
                    for (Detalle_Presupuesto odetalle_presu : list_Detalle_presupuesto){
                        if (odetalle_presu.getIddetalle_presupuesto()==(oDetalle_Presupuesto.getIddetalle_presupuesto())){
                            //oPresupuestoModificarController.setDetallePresupuesto(odetalle_presu);
                            }
                    }
                    oPresupuestoModificarController.setController(odc);
                    lockedPantalla();

                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    Detalle_Presupuesto opresupuesto = (Detalle_Presupuesto) imag.getUserData();
                    Optional<ButtonType> result= oUtilClass.mostrar_confirmación("Eliminar Presupuesto", "¿Está seguro que desea eliminar el sub presupuesto?");
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        for (Detalle_Presupuesto odetalle_presupuesto : list_Detalle_presupuesto){
                            if (odetalle_presupuesto.getIddetalle_presupuesto()==(opresupuesto.getIddetalle_presupuesto())){
                                list_Detalle_presupuesto.remove(odetalle_presupuesto);
                                oPresupuesto.setMonto_total(oPresupuesto.getMonto_total() - (opresupuesto.getMonto() * opresupuesto.getCantidad()));
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



    @FXML
    void cerrar() {
        oVerPacienteController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();//cerrando la ventanada anterior
    }

    boolean isCompleto() {
        boolean aux = true;

        if (jtfDescripcion.getText().trim().length() == 0) {
            jtfDescripcion.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfDescripcion.setStyle("");
        }

        if (jtfMonto.getText().trim().length() == 0) {
            jtfMonto.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfMonto.setStyle("");
        }

        if (jtfCantidad.getText().trim().length() == 0) {
            jtfCantidad.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfCantidad.setStyle("");
        }
        return aux;
    }

    void limpiar() {
        jtfDescripcion.setText("");
        jtfMonto.setText("");
        jtfMonto.setText("");
        jtfCantidad.setText("");
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

}
