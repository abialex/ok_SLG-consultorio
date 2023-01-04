/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Persona;
import Entidades.Detalle_Presupuesto;
import Entidades.Historia_clinica;
import Entidades.Paciente;
import Entidades.Presupuesto;
import Entidades.Tratamiento;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertConfirmarController;
import java.io.IOException;
import java.net.URL;
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
    private TableColumn<Detalle_Presupuesto, String> ColumnCantidad;

    @FXML
    private TableColumn<Detalle_Presupuesto, Float> ColumnMonto;

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
    ObservableList<Detalle_Presupuesto> listPresupuesto = FXCollections.observableArrayList();
    PresupuestoVerController odc = this;
    AlertConfirmarController oAlertConfimarController = new AlertConfirmarController();
    Detalle_Presupuesto oPresupuesto_detalleEliminar;
    Presupuesto oPresupuesto;
    Alert alert = new Alert(Alert.AlertType.WARNING);

    int indexEliminar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initRestricciones();
    }

    void getPresupuesto(Persona opersona) {
        //Paciente opaciente = (Paciente) App.jpa.createQuery("select p from Paciente p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        Historia_clinica oHistoriaclinica = (Historia_clinica) App.jpa.createQuery("select p from Historia_clinica p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        List<Presupuesto> list_presupuesto = App.jpa.createQuery("select p from Presupuesto p where idhistoria_clinica=" + oHistoriaclinica.getIdhistoria_clinica()).getResultList();
        if (!list_presupuesto.isEmpty()) {
            oPresupuesto = list_presupuesto.get(0);
            btnGuardarPresupuesto.setDisable(!oPresupuesto.isActivo());
        } else {
            oPresupuesto = new Presupuesto(oHistoriaclinica, 0, LocalDate.now(), true, false);
            App.jpa.getTransaction().begin();
            App.jpa.persist(oPresupuesto);
            App.jpa.refresh(oPresupuesto);
            App.jpa.getTransaction().commit();
        }
    }

    void setPersona(Persona opersona) {
        //Initialize
        getPresupuesto(opersona);
        this.oPersona = opersona;
        lblnombre.setText(opersona.getNombres_apellidos());
        updateListaPresupuesto();
        initTable();
        tableTratamiento.setItems(listPresupuesto);
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
        if (!listPresupuesto.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Info");
            alert.setContentText("Podrá seguir agregando presupuesto");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                oPresupuesto.setActivo(false);
                App.jpa.getTransaction().begin();
                App.jpa.persist(oPresupuesto);
                App.jpa.getTransaction().commit();
                cerrar();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Info");
            alert.setContentText("No tiene ni un presupuesto");
            alert.showAndWait();
            
        }
    }

    @FXML
    void guardarPresupuesto() {
        if (isCompleto()) {
            Detalle_Presupuesto odetelle_presupuesto = new Detalle_Presupuesto(oPresupuesto,
                    jtfDescripcion.getText(),
                    Integer.parseInt(jtfCantidad.getText()),
                    Float.parseFloat(jtfMonto.getText()));
            oPresupuesto.setMonto_total(oPresupuesto.getMonto_total() + Float.parseFloat(jtfMonto.getText()) * Float.parseFloat(jtfCantidad.getText()));
            App.jpa.getTransaction().begin();
            App.jpa.persist(odetelle_presupuesto);
            App.jpa.persist(oPersona);
            App.jpa.persist(oPresupuesto);
            App.jpa.getTransaction().commit();
            updateListaPresupuesto();
            limpiar();
            //oAlertController.Mostrar("successful", "Agregado");
        } else {
            //oAlertController.Mostrar("error", "Llene los espacios en blanco");
        }
    }

    @FXML
    void updateListaPresupuesto() {
        List<Detalle_Presupuesto> olistPresupuesto = App.jpa.createQuery("select p from Detalle_Presupuesto p where idpresupuesto= " + oPresupuesto.getIdpresupuesto() + " order by idpresupuesto DESC").setMaxResults(10).getResultList();
        listPresupuesto.clear();
        for (Detalle_Presupuesto opresupuesto : olistPresupuesto) {
            listPresupuesto.add(opresupuesto);
        }
        updateMontoAviso(olistPresupuesto);
    }

    void updateMontoAviso(List<Detalle_Presupuesto> list) {
        float acumMontoTotal = 0;
        for (Detalle_Presupuesto opresupuesto : list) {
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
        ColumnCantidad.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, String>("cantidad"));
        columnTratamiento.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, String>("descripcion"));
        ColumnMonto.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, Float>("monto"));
        ColumnEstado.setCellValueFactory(new PropertyValueFactory<Detalle_Presupuesto, Detalle_Presupuesto>("DetallePresupuesto"));

        ColumnMonto.setCellFactory(column -> {
            TableCell<Detalle_Presupuesto, Float> cell = new TableCell<Detalle_Presupuesto, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
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
                    oPresupuestoModificarController.setController(odc);
                    lockedPantalla();

                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    Detalle_Presupuesto opresupuesto = (Detalle_Presupuesto) imag.getUserData();
                    oPresupuesto_detalleEliminar = opresupuesto;
                    oAlertConfimarController = (AlertConfirmarController) mostrarVentana(AlertConfirmarController.class, "/fxml/AlertConfirmar");
                    oAlertConfimarController.setController(odc);
                    oAlertConfimarController.setMensaje(" ¿Está seguro de eliminar \n el presupuesto?");
                    lockedPantalla();

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

    public void eliminar() {
        if (indexEliminar != -1) {
            oPresupuesto.setMonto_total(oPresupuesto.getMonto_total() - oPresupuesto_detalleEliminar.getMonto() * oPresupuesto_detalleEliminar.getCantidad());
            App.jpa.getTransaction().begin();
            App.jpa.remove(oPresupuesto_detalleEliminar);
            App.jpa.getTransaction().commit();
            listPresupuesto.remove(indexEliminar);
            updateListaPresupuesto();
        }
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
