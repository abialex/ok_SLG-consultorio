/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Persona;
import Pdf.Historiaclinicapdf;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertConfirmarController;
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
import javafx.event.ActionEvent;
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
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class VerPacienteController implements Initializable {

    @FXML
    AnchorPane ap;

    @FXML
    private JFXTextField jtfbuscar;

    @FXML
    private TableView<Persona> tablePersona;

    @FXML
    private TableColumn<Persona, String> tableDni, tableNombre, tableTelefono, tableDomicilio, tableOcupacion;

    @FXML
    private TableColumn<Persona, Integer> tableOpcion;

    @FXML
    private TableColumn<Persona, LocalDate> tableAdulto;

    RegistrarPacienteController oRegistrarPacienteController;
    ObservableList<Persona> listPersona = FXCollections.observableArrayList();
    private double x = 0;
    private double y = 0;
    Stage stagePrincipal;
    VerPacienteController odc = this;
    AlertConfirmarController oAlertConfimarController1 = new AlertConfirmarController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateListPersona();
        initTableView();
        tablePersona.setItems(listPersona);
        // TODO
    }

    void setController(RegistrarPacienteController aThis) {
        this.oRegistrarPacienteController = aThis;
    }

    @FXML
    void updateListPersona() {
        List<Persona> olistPerson = App.jpa.createQuery("select p from Persona p where (dni like "+"'"+jtfbuscar.getText()+"%'"
                +" or "+"nombres_apellidos like "+"'%"+jtfbuscar.getText()+"%') and flag = false").setMaxResults(10).getResultList();
        listPersona.clear();
        for (Persona ocarta : olistPerson) {
            listPersona.add(ocarta);
        }
    }
    
    void setStagePrincipall(Stage aThis) {
        this.stagePrincipal = aThis;
    }

    @FXML
    void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();//cerrando la ventanada anterior
    }

    int selectItem() {
        return listPersona.indexOf(tablePersona.getSelectionModel().getSelectedItem());
    }

    public void eliminar(Persona oPersona, int index) {
        if (index != -1) {
            oPersona.setFlag(true);
            App.jpa.getTransaction().begin();
            App.jpa.persist(oPersona);
            App.jpa.getTransaction().commit();
            listPersona.remove(index);
            updateListPersona();
            //getitem para limpiar
            //getItem();
        }
    }

    void initTableView() {
        tableDni.setCellValueFactory(new PropertyValueFactory<Persona, String>("dni"));
        tableNombre.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombres_apellidos"));
        tableTelefono.setCellValueFactory(new PropertyValueFactory<Persona, String>("telefono"));
        tableDomicilio.setCellValueFactory(new PropertyValueFactory<Persona, String>("domicilio"));
        tableOcupacion.setCellValueFactory(new PropertyValueFactory<Persona, String>("ocupacion"));
        tableAdulto.setCellValueFactory(new PropertyValueFactory<Persona, LocalDate>("fechaNacimiento"));
        tableOpcion.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("idpersona"));

        tableAdulto.setCellFactory(column -> {
            TableCell<Persona, LocalDate> cell = new TableCell<Persona, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Period period = Period.between(item, LocalDate.now());
                        long edad = period.getYears();
                        Label label = new Label();
                        String color = "";
                        if (edad >= 18) {
                            label.setText("SI");
                            color = "-fx-text-fill: BLUE;";
                        } else {
                            label.setText("NO");
                            color = "-fx-text-fill: RED;";
                        }
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999; " + color);
                        setGraphic(label);
                        setText(null);
                    }
                }
            };

            return cell;
        });

        Callback<TableColumn<Persona, Integer>, TableCell<Persona, Integer>> cellFoctory = (TableColumn<Persona, Integer> param) -> {
            // make cell containing buttons
            final TableCell<Persona, Integer> cell = new TableCell<Persona, Integer>() {

                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows                    
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        int tamHightImag = 29;
                        int tamWidthImag = 29;
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

                        ImageView PrintIcon = new ImageView(new Image(getClass().getResource("/imagenes/printer-1.png").toExternalForm()));
                        PrintIcon.setFitHeight(tamHightImag);
                        PrintIcon.setFitWidth(tamWidthImag);
                        PrintIcon.setUserData(item);
                        PrintIcon.setStyle(
                                " -fx-cursor: hand;"
                        );
                        PrintIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarImprimir(event));
                        PrintIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagImprimirMoved(event));
                        PrintIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagImprimirFuera(event));

                        ImageView cajaIcon = new ImageView(new Image(getClass().getResource("/imagenes/money-1.png").toExternalForm()));
                        cajaIcon.setFitHeight(tamHightImag);
                        cajaIcon.setFitWidth(tamWidthImag);
                        cajaIcon.setUserData(item);
                        cajaIcon.setStyle(
                                " -fx-cursor: hand;"
                        );
                        cajaIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarCaja(event));
                        cajaIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagMoneyMoved(event));
                        cajaIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagMoneyFuera(event));

                        HBox managebtn = new HBox(PrintIcon, editIcon, cajaIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(PrintIcon, new Insets(0, 2.5, 0, 2.5));
                        HBox.setMargin(editIcon, new Insets(0, 2.5, 0, 2.5));
                        HBox.setMargin(deleteIcon, new Insets(0, 2.5, 0, 2.5));
                        HBox.setMargin(cajaIcon, new Insets(0, 2.5, 0, 2.5));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

                void mostrarModificar(MouseEvent event) {
                    ImageView buton = (ImageView) event.getSource();
                    for (Persona opersona : listPersona) {
                        if (opersona.getIdpersona() == (Integer) buton.getUserData()) {
                            ModificarPacienteController oModificarPacienteController = (ModificarPacienteController) mostrarVentana(ModificarPacienteController.class, "ModificarPaciente");
                            oModificarPacienteController.setPersona(opersona);
                            break;
                        }
                    }
                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listPersona.size(); i++) {
                        if (listPersona.get(i).getIdpersona() == (Integer) imag.getUserData()) {
                            Persona carta = listPersona.get(i);
                            oAlertConfimarController1 = (AlertConfirmarController) mostrarVentana(AlertConfirmarController.class, "/fxml/AlertConfirmar");
                            oAlertConfimarController1.setController(odc);
                            oAlertConfimarController1.setMensaje(" ¿Está seguro de eliminar?");
                            oAlertConfimarController1.setCartaIndex(carta, i);
                            break;
                        }
                    }
                }

                void mostrarImprimir(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listPersona.size(); i++) {
                        if (listPersona.get(i).getIdpersona() == (Integer) imag.getUserData()) {
                            try {
                                Persona opersona = listPersona.get(i);

                                Historiaclinicapdf.ImprimirHistoriaClinica(opersona);
                                File file = new File("pdf\\historia_clinica.pdf");
                                Desktop.getDesktop().open(file);

                                break;
                            } catch (IOException ex) {
                                Logger.getLogger(VerPacienteController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }

                void mostrarCaja(MouseEvent event) {
                    ImageView buton = (ImageView) event.getSource();
                    for (Persona opersona : listPersona) {
                        if (opersona.getIdpersona() == (Integer) buton.getUserData()) {
                            CajaVerController oCajaVerController = (CajaVerController) mostrarVentana(CajaVerController.class, "CajaVer");
                            oCajaVerController.setPersona(opersona);
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

                private void imagImprimirMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/printer-2.png").toExternalForm()));
                }

                private void imagImprimirFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/printer-1.png").toExternalForm()));
                }

                private void imagMoneyMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/money-2.png").toExternalForm()));
                }

                private void imagMoneyFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/money-1.png").toExternalForm()));
                }
            };
            return cell;
        };
        tableOpcion.setCellFactory(cellFoctory);

        tableDni.setCellFactory(column -> {
            TableCell<Persona, String> cell = new TableCell<Persona, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();

                        label.setText(item);
                        //fin
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999;");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };

            return cell;
        });

        tableOcupacion.setCellFactory(column -> {
            TableCell<Persona, String> cell = new TableCell<Persona, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();

                        label.setText(item);
                        //fin
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999;");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };

            return cell;
        });
    }

    @FXML
    void imagAddpacienteMoved(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/medical-2.png").toExternalForm()));
    }

    @FXML
    void imagAddpacienteFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/medical-1.png").toExternalForm()));
    }

    @FXML
    void mostrarRegistrarpaciente() {
        RegistrarPacienteController oRegistrarController = (RegistrarPacienteController) mostrarVentana(RegistrarPacienteController.class, "RegistrarPaciente");
        //oRegistrarController.setController(VerPacienteController.this);
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
        stage.initOwner(stagePrincipal);
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
