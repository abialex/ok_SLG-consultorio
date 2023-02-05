/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Detalle_Presupuesto;
import Entidades.Historia_clinica;
import Entidades.Persona;
import Entidades.Presupuesto;
import Entidades.Tratamiento;
import Pdf.Historiaclinicapdf;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertConfirmarController;
import emergente.AlertController;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
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
    private TableColumn<Tratamiento, Integer> ColumnPagado;

    @FXML
    private TableColumn<Tratamiento, Integer> ColumnEstado;

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

    double x = 0, y = 0;
    Persona oPersona;
    ObservableList<Tratamiento> listTratamiento = FXCollections.observableArrayList();
     ObservableList<Detalle_Presupuesto> listPresupuesto = FXCollections.observableArrayList();
    Stage stagePrincipal;
    AlertConfirmarController oAlertConfimarController = new AlertConfirmarController();
    CajaVerController odc = this;
    Tratamiento oTratamientoEliminar;
    int indexEliminar;
    AlertController oAlertController = new AlertController();
    VerPacienteController oVerPacienteController;
    Presupuesto oPresupuesto;
    float MontoTotal = 0;
    float acumMonto = 0;
    List<Tratamiento> olistTratamiento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // el resto de init está en setPersona()
        initRestricciones();
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
                Tratamiento otratamiento = new Tratamiento(oPersona,
                        LocalDate.now(),
                        jtfTratamiento.getText(),
                        Integer.parseInt(jtfMonto.getText()));
                App.jpa.getTransaction().begin();
                App.jpa.persist(otratamiento);
                App.jpa.getTransaction().commit();
                updateListaTratamiento();
                limpiar();
                oAlertController.Mostrar("successful", "Agregado");
            } else {
                oAlertController.Mostrar("error", "Se pasó del precio");

            }
        } else {
            oAlertController.Mostrar("error", "Llene los espacios en blanco");
        }
    }

    void setPersona(Persona opersona, Presupuesto opresupuesto) {
        //Initialize
        oPresupuesto = opresupuesto;
        lblMontototal.setText(oPresupuesto.getMonto_total() + "");
        this.oPersona = opersona;
        lblnombre.setText(opersona.getNombres());
        MontoTotal = opresupuesto.getMonto_total();
        tableTratamiento.setItems(listTratamiento);
        updateListaTratamiento();
        getListaPresupuesto(opersona);
        initTable();
        initTablePresupuesto();
        tableTratamiento.setItems(listTratamiento);
        tablePresupuesto.setItems(listPresupuesto);
    }

    void getListaPresupuesto(Persona opersona) {
        //Paciente opaciente = (Paciente) App.jpa.createQuery("select p from Paciente p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        Historia_clinica oHistoriaclinica = (Historia_clinica) App.jpa.createQuery("select p from Historia_clinica p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        Presupuesto Presupuesto = (Presupuesto) App.jpa.createQuery("select p from Presupuesto p where idhistoria_clinica=" + oHistoriaclinica.getIdhistoria_clinica()).getSingleResult();
        List<Detalle_Presupuesto> olistDetallePresupuesto = App.jpa.createQuery("select p from Detalle_Presupuesto p where idpresupuesto= " + Presupuesto.getIdpresupuesto() + " order by iddetalle_presupuesto ASC").getResultList();
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
        olistTratamiento = App.jpa.createQuery("select p from Tratamiento p where idpersona= " + oPersona.getIdpersona() + " and flag = false order by idtratamiento ASC").getResultList();
        listTratamiento.clear();
        acumMonto = 0;
        for (Tratamiento otratamiento : olistTratamiento) {
            listTratamiento.add(otratamiento);
            acumMonto = acumMonto + otratamiento.getMonto();
        }
        if (MontoTotal == acumMonto) {
            btnAgregar.setDisable(true);
        } else {
            btnAgregar.setDisable(false);
        }
        updateMontoAviso(olistTratamiento);

    }

    void updateMontoAviso(List<Tratamiento> list) {
        int acumMontoTotal = 0;
        for (Tratamiento otratamiento : list) {
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
        columnFecha.setCellValueFactory(new PropertyValueFactory<Tratamiento, LocalDate>("fechaRealizada"));
        columnTratamiento.setCellValueFactory(new PropertyValueFactory<Tratamiento, String>("tratamiento"));
        ColumnMonto.setCellValueFactory(new PropertyValueFactory<Tratamiento, Integer>("monto"));
        ColumnPagado.setCellValueFactory(new PropertyValueFactory<Tratamiento, Integer>("monto"));
        ColumnEstado.setCellValueFactory(new PropertyValueFactory<Tratamiento, Integer>("idtratamiento"));

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

        ColumnPagado.setCellFactory(column -> {
            TableCell<Tratamiento, Integer> cell = new TableCell<Tratamiento, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Label label = new Label();
                        //MontoTotal = MontoTotal - item;
                        label.setText( "N.A");
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999; ");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });

        Callback<TableColumn<Tratamiento, Integer>, TableCell<Tratamiento, Integer>> cellFoctory = (TableColumn<Tratamiento, Integer> param) -> {
            // make cell containing buttons
            final TableCell<Tratamiento, Integer> cell = new TableCell<Tratamiento, Integer>() {

                @Override
                public void updateItem(Integer item, boolean empty) {
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
                    for (int i = 0; i < listTratamiento.size(); i++) {
                        if (listTratamiento.get(i).getIdtratamiento() == (Integer) imag.getUserData()) {
                            CajaModificarController oCajaModificarController = (CajaModificarController) mostrarVentana(CajaModificarController.class, "CajaModificar");
                            oCajaModificarController.setTratamiento(listTratamiento.get(i), MontoTotal - acumMonto);
                            oCajaModificarController.setController(odc);
                            lockedPantalla();
                            break;
                        }
                    }
                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listTratamiento.size(); i++) {
                        if (listTratamiento.get(i).getIdtratamiento() == (Integer) imag.getUserData()) {
                            oTratamientoEliminar = listTratamiento.get(i);
                            oAlertConfimarController = (AlertConfirmarController) mostrarVentana(AlertConfirmarController.class, "/fxml/AlertConfirmar");
                            oAlertConfimarController.setController(odc);
                            oAlertConfimarController.setMensaje(" ¿Está seguro de eliminar \n el tratamiento?");
                            lockedPantalla();
                            break;
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
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999; ");
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
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999; ");
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
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999; ");
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
            App.jpa.getTransaction().begin();
            App.jpa.persist(oTratamientoEliminar);
            App.jpa.getTransaction().commit();
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

    void setStagePrincipall(Stage aThis) {
        this.stagePrincipal = aThis;
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
