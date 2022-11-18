/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Historia_clinica;
import Entidades.Paciente;
import Entidades.Persona;
import Entidades.Presupuesto;
import Pdf.Historiaclinicapdf;
import Util.FileImagUtil;
import com.jfoenix.controls.JFXTextField;
import controllerCita.CitaVerController;
import controllerDoctor.DoctorVerController;
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
import javafx.scene.control.Alert;
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
import javafx.scene.paint.Color;
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
    private TableColumn<Persona, String> tableDni, tableTelefono, tableDomicilio, tableOcupacion;

    @FXML
    private TableColumn<Persona, Integer> tableOpcion;
    
    @FXML
    private TableColumn<Persona, Persona> columnID, tableNombre;

    @FXML
    private TableColumn<Persona, LocalDate> tableAdulto;

    RegistrarPacienteController oRegistrarPacienteController;
    ObservableList<Persona> listPersona = FXCollections.observableArrayList();
    private double x = 0;
    private double y = 0;
    Stage stagePrincipal;
    VerPacienteController odc = this;
    AlertConfirmarController oAlertConfimarController = new AlertConfirmarController();
    Persona oPersonaEliminar;
    int indexEliminar;
    Alert alert = new Alert(Alert.AlertType.WARNING);

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
        List<Persona> olistPerson = App.jpa.createQuery("select p from Persona p where (dni like " + "'" + jtfbuscar.getText() + "%'"
                + " or " + "nombres_apellidos like " + "'%" + jtfbuscar.getText() + "%') and flag = false and ocupacion <> 'DOCTOR' order by idpersona DESC").getResultList();
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

    public void lockedPantalla() {
        if (ap.isDisable()) {
            ap.setDisable(false);
        } else {
            ap.setDisable(true);
        }
    }

    void selectAgregado() {
        if (!listPersona.isEmpty()) {
            tablePersona.getSelectionModel().select(listPersona.get(0));
        }
    }

    void selectModificado(Persona opersona) {
        if (!listPersona.isEmpty()) {
            for (Persona persona : listPersona) {
                if (opersona == persona) {
                    tablePersona.getSelectionModel().select(persona);
                    break;
                }
            }
        }
    }

    public void eliminar() {
        if (indexEliminar != -1) {
            oPersonaEliminar.setFlag(true);
            App.jpa.getTransaction().begin();
            App.jpa.persist(oPersonaEliminar);
            App.jpa.getTransaction().commit();
            listPersona.remove(indexEliminar);
            updateListPersona();
        }
    }

    void initTableView() {
        columnID.setCellValueFactory(new PropertyValueFactory<Persona, Persona>("persona"));
        tableDni.setCellValueFactory(new PropertyValueFactory<Persona, String>("dni"));
        tableNombre.setCellValueFactory(new PropertyValueFactory<Persona, Persona>("persona"));
        tableTelefono.setCellValueFactory(new PropertyValueFactory<Persona, String>("telefono"));
        tableDomicilio.setCellValueFactory(new PropertyValueFactory<Persona, String>("domicilio"));
        tableOcupacion.setCellValueFactory(new PropertyValueFactory<Persona, String>("ocupacion"));
        tableAdulto.setCellValueFactory(new PropertyValueFactory<Persona, LocalDate>("fechaNacimiento"));
        tableOpcion.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("idpersona"));
        
        tableNombre.setCellFactory(column -> {
            TableCell<Persona, Persona> cell = new TableCell<Persona, Persona>() {
                @Override
                protected void updateItem(Persona item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        item.getPaciente();                     
                        setText(item.getNombres_apellidos()+" "+item.getAp_paterno()+" "+item.getAp_materno());
                    }
                }
            };

            return cell;
        });
        
        
        columnID.setCellFactory(column -> {
            TableCell<Persona, Persona> cell = new TableCell<Persona, Persona>() {
                @Override
                protected void updateItem(Persona item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        item.getPaciente();
                     
                        setText(item.getPaciente().getHistoriaClinica().getIdhistoria_clinica()+"");
                    }
                }
            };

            return cell;
        });

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

                        ImageView presupuestoIcon = new ImageView(new Image(getClass().getResource("/imagenes/presupuesto-1.png").toExternalForm()));
                        presupuestoIcon.setFitHeight(tamHightImag);
                        presupuestoIcon.setFitWidth(tamWidthImag);
                        presupuestoIcon.setUserData(item);
                        presupuestoIcon.setStyle(
                                " -fx-cursor: hand;"
                        );
                        presupuestoIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarPresupuesto(event));
                        presupuestoIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagPresupuestoMoved(event));
                        presupuestoIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagPresupuestoFuera(event));

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

                        HBox managebtn = new HBox(PrintIcon, editIcon, cajaIcon, presupuestoIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(PrintIcon, new Insets(0, 2.75, 0, 2.75));
                        HBox.setMargin(editIcon, new Insets(0, 2.75, 0, 2.75));
                        HBox.setMargin(deleteIcon, new Insets(0, 2.75, 0, 2.75));
                        HBox.setMargin(cajaIcon, new Insets(0, 2.75, 0, 2.75));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

                void mostrarModificar(MouseEvent event) {
                    ImageView buton = (ImageView) event.getSource();
                    for (Persona opersona : listPersona) {
                        if (opersona.getIdpersona() == (Integer) buton.getUserData()) {
                            ModificarPacienteController oModificarPacienteController = (ModificarPacienteController) mostrarVentana(ModificarPacienteController.class, "ModificarPaciente");
                            oModificarPacienteController.setController(odc);
                            oModificarPacienteController.setPersona(opersona);
                            lockedPantalla();
                            break;
                        }
                    }
                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listPersona.size(); i++) {
                        if (listPersona.get(i).getIdpersona() == (Integer) imag.getUserData()) {
                            oPersonaEliminar = listPersona.get(i);
                            indexEliminar = i;
                            oAlertConfimarController = (AlertConfirmarController) mostrarVentana(AlertConfirmarController.class, "/fxml/AlertConfirmar");
                            oAlertConfimarController.setController(odc);
                            oAlertConfimarController.setMensaje(" ¿Está seguro de eliminar al \n paciente? \n \n" + " " + oPersonaEliminar.getNombres_apellidos());
                            lockedPantalla();
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
                                File file = new File("Pdf\\historia_clinica_" + opersona.getNombres_apellidos() + "_" + opersona.getDni() + ".pdf");
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
                            Presupuesto opresupuesto = getPresupuesto(opersona);
                            if (opresupuesto.isActivo() || opresupuesto.getIdpresupuesto() == 0) {
                                alert.setHeaderText(null);
                                alert.setTitle("Tratamiento");
                                alert.setContentText("No tiene presupuesto finalizado");
                                alert.showAndWait();

                            } else {
                                CajaVerController oCajaVerController = (CajaVerController) mostrarVentana(CajaVerController.class, "CajaVer");
                                oCajaVerController.setPersona(opersona, opresupuesto);
                                oCajaVerController.setController(odc);
                                lockedPantalla();

                            }
                            break;
                        }
                    }
                }

                void mostrarPresupuesto(MouseEvent event) {
                    ImageView buton = (ImageView) event.getSource();
                    for (Persona opersona : listPersona) {
                        if (opersona.getIdpersona() == (Integer) buton.getUserData()) {
                            PresupuestoVerController oPresupuestoVerController = (PresupuestoVerController) mostrarVentana(PresupuestoVerController.class, "PresupuestoVer");
                            oPresupuestoVerController.setPersona(opersona);
                            oPresupuestoVerController.setController(odc);
                            lockedPantalla();
                            break;
                        }
                    }
                }

                void mostrarCarpeta(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listPersona.size(); i++) {
                        if (listPersona.get(i).getIdpersona() == (Integer) imag.getUserData()) {

                            Persona opersona = listPersona.get(i);
                            String url = (new File(".").getAbsolutePath()) + "/Archivos paciente/" + opersona.getNombres_apellidos();
                            FileImagUtil oFileImagUtil = new FileImagUtil(url, "Archivos de " + opersona.getNombres_apellidos());
                            try {
                                oFileImagUtil.buscarArchivo();
                                //   lblpdf.setText(oPdf.getName());
                            } catch (IOException ex) {
                                Logger.getLogger(VerPacienteController.class.getName()).log(Level.SEVERE, null, ex);
                            }
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

                private void imagPresupuestoMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/presupuesto-2.png").toExternalForm()));
                }

                private void imagPresupuestoFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/presupuesto-1.png").toExternalForm()));
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
    void imagDoctoreMoved(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/doctor-2.png").toExternalForm()));
    }

    @FXML
    void imagDoctorFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/doctor-1.png").toExternalForm()));
    }

    @FXML
    void imagCitaMoved(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/cita-2.png").toExternalForm()));
    }

    @FXML
    void imagCitaFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/cita-1.png").toExternalForm()));
    }

    @FXML
    void mostrarRegistrarpaciente() {
        RegistrarPacienteController oRegistrarController = (RegistrarPacienteController) mostrarVentana(RegistrarPacienteController.class, "RegistrarPaciente");
        oRegistrarController.setController(odc);
        lockedPantalla();
    }

    @FXML
    void mostrarDoctor() {
        DoctorVerController oRegistrarController = (DoctorVerController) mostrarVentana(DoctorVerController.class, "DoctorVer");
        oRegistrarController.setController(odc);
        lockedPantalla();
    }

    @FXML
    void mostrarCita() {
        CitaVerController oCitaVerController = (CitaVerController) mostrarVentana(CitaVerController.class, "CitaVer");
        oCitaVerController.setController(odc);
        lockedPantalla();
    }

    Presupuesto getPresupuesto(Persona opersona) {
        Paciente opaciente = (Paciente) App.jpa.createQuery("select p from Paciente p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        Historia_clinica oHistoriaclinica = (Historia_clinica) App.jpa.createQuery("select p from Historia_clinica p where idpaciente=" + opaciente.getIdpaciente()).getSingleResult();
        List<Presupuesto> list_presupuesto = App.jpa.createQuery("select p from Presupuesto p where idhistoria_clinica=" + oHistoriaclinica.getIdhistoria_clinica()).getResultList();
        if (!list_presupuesto.isEmpty()) {
            return list_presupuesto.get(0);
        } else {
            Presupuesto opre = new Presupuesto();
            opre.setActivo(false);
            return opre;
        }
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
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
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
