/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Persona;
import Entidades.Presupuesto;
import Entidades.Tratamiento;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertConfirmarController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private TableView<Presupuesto> tableTratamiento;

    @FXML
    private TableColumn<Presupuesto, String> columnTratamiento;

    @FXML
    private TableColumn<Presupuesto, Float> ColumnMonto;

    @FXML
    private TableColumn<Presupuesto, Presupuesto> ColumnEstado;

    @FXML
    private JFXTextField jtfDescripcion;

    @FXML
    private JFXTextField jtfMonto;

    @FXML
    private Label lblnombre;

    @FXML
    private Label lblMontototal;

    Persona oPersona;
    double x = 0, y = 0;
    VerPacienteController oVerPacienteController;
    ObservableList<Presupuesto> listPresupuesto = FXCollections.observableArrayList();
    PresupuestoVerController odc= this;
    AlertConfirmarController oAlertConfimarController = new AlertConfirmarController();
    Presupuesto oPresupuestoEliminar;
    
    int indexEliminar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void setPersona(Persona opersona) {
        //Initialize
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
    void guardarPresupuesto() {
        if (isCompleto()) {
            Presupuesto opresupuesto = new Presupuesto(oPersona,
                    jtfDescripcion.getText(),
                    Float.parseFloat(jtfMonto.getText()));
            oPersona.setPresupuestoTotal(oPersona.getPresupuestoTotal()+opresupuesto.getMonto());
            App.jpa.getTransaction().begin();
            App.jpa.persist(opresupuesto);
            App.jpa.persist(oPersona);
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
        List<Presupuesto> olistPresupuesto = App.jpa.createQuery("select p from Presupuesto p where idpersona= " + oPersona.getIdpersona() + " order by idpresupuesto DESC").setMaxResults(10).getResultList();
        listPresupuesto.clear();
        for (Presupuesto opresupuesto : olistPresupuesto) {
            listPresupuesto.add(opresupuesto);
        }
        updateMontoAviso(olistPresupuesto);
    }

    void updateMontoAviso(List<Presupuesto> list) {
        float acumMontoTotal = 0;
        for (Presupuesto opresupuesto : list) {
            acumMontoTotal = acumMontoTotal + opresupuesto.getMonto();
        }
        lblMontototal.setText(acumMontoTotal + "");
    }

    void initTable() {
        columnTratamiento.setCellValueFactory(new PropertyValueFactory<Presupuesto, String>("descripcion"));
        ColumnMonto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Float>("monto"));
        ColumnEstado.setCellValueFactory(new PropertyValueFactory<Presupuesto, Presupuesto>("presupuesto"));

        ColumnMonto.setCellFactory(column -> {
            TableCell<Presupuesto, Float> cell = new TableCell<Presupuesto, Float>() {
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

        Callback<TableColumn<Presupuesto, Presupuesto>, TableCell<Presupuesto, Presupuesto>> cellFoctory = (TableColumn<Presupuesto, Presupuesto> param) -> {
            // make cell containing buttons
            final TableCell<Presupuesto, Presupuesto> cell = new TableCell<Presupuesto, Presupuesto>() {

                @Override
                public void updateItem(Presupuesto item, boolean empty) {
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
                    /*
                    ImageView imag = (ImageView) event.getSource();
                    Presupuesto opresupuesto = (Presupuesto) imag.getUserData();
                    PresupuestoVerController oPresupuestoVerController = (PresupuestoVerController) mostrarVentana(CajaModificarController.class, "CajaModificar");
                    oPresupuestoVerController.setTratamiento(listPresupuesto.get(i));
                    oPresupuestoVerController.setController(odc);
                    lockedPantalla();*/

                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    Presupuesto opresupuesto = (Presupuesto) imag.getUserData();
                    oPresupuestoEliminar = opresupuesto;
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
            oPersona.setPresupuestoTotal(oPersona.getPresupuestoTotal() - oPresupuestoEliminar.getMonto());
            App.jpa.getTransaction().begin();
            App.jpa.remove(oPresupuestoEliminar);
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
        return aux;
    }

    void limpiar() {
        jtfMonto.setText("");
        jtfMonto.setText("");
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
