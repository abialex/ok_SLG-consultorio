/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerCita;

import Entidades.Cita;
import Entidades.Doctor;
import Entidades.HoraAtencion;
import Entidades.Persona;
import EntidadesSettings.SettingsDoctor;
import Pdf.Historiaclinicapdf;
import Util.FileImagUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import controller.App;
import controller.CajaVerController;
import controller.ModificarPacienteController;
import controller.VerPacienteController;
import emergente.AlertConfirmarController;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class CitaVerController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private FlowPane fpDias;

    @FXML
    private TableView<HoraAtencion> tableDoctor1, tableDoctor2, tableDoctor3, tableDoctor4;

    @FXML
    private TableColumn<HoraAtencion, HoraAtencion> columnHoraAtencion1, columnHoraAtencion2, columnHoraAtencion3, columnHoraAtencion4;

    @FXML
    private TableColumn<HoraAtencion, HoraAtencion> columnCitas1, columnCitas2, columnCitas3, columnCitas4;

    @FXML
    private TableColumn<HoraAtencion, HoraAtencion> columnEstado1, columnEstado2, columnEstado3, columnEstado4;

    @FXML
    private JFXComboBox<Doctor> jcbDoctor1, jcbDoctor2, jcbDoctor3, jcbDoctor4;

    @FXML
    private JFXComboBox<String> jcbMes;

    @FXML
    private JFXComboBox<String> jcbAnio;

    @FXML
    private Label lblfecha;

    ObservableList<HoraAtencion> listHoraatencion = FXCollections.observableArrayList();
    LocalDate oFecha;
    CitaVerController odc = this;
    private double x = 0;
    private double y = 0;
    VerPacienteController oVerPacienteController;
    JFXButton btn;//usado para desmarcar n
    String colorDefault = "-fx-background-color: #ffffff; -fx-border-color: #000000";
    String colorRed = "-fx-background-color: RED; -fx-border-color: #000000";
    String colorPlomo = "-fx-background-color:GRAY; -fx-border-color: #000000";
    String colorBlue = "-fx-background-color:BLUE; -fx-border-color: #000000";
    String colorYellow = "-fx-background-color:yellow; -fx-border-color: #000000";
    Doctor doctorNinguno;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateListHoraatencion();
        cargarDoctor();
        tableDoctor1.setItems(listHoraatencion);
        tableDoctor2.setItems(listHoraatencion);
        tableDoctor3.setItems(listHoraatencion);
        tableDoctor4.setItems(listHoraatencion);

        oFecha = LocalDate.now();
        jcbMes.getSelectionModel().select(getMesNum(LocalDate.now().getMonthValue()));
        cargarAnio();
        cargarMes();
        changueMes();
        lblfecha.setText(getNombreDia(oFecha.getDayOfWeek().getValue()) + " " + oFecha.getDayOfMonth() + " de " + getMesNum(oFecha.getMonthValue()));
        initTable();
    }

    @FXML
    void updateListHoraatencion() {
        List<HoraAtencion> olistHoraatencion = App.jpa.createQuery("select p from HoraAtencion p order by idhoraatencion ASC").setMaxResults(10).getResultList();
        listHoraatencion.clear();
        for (HoraAtencion oDoc : olistHoraatencion) {
            listHoraatencion.add(oDoc);
        }
    }

    @FXML
    void reiniciar() {
        oFecha = LocalDate.now();
        jcbMes.getSelectionModel().select(getMesNum(LocalDate.now().getMonthValue()));
        jcbAnio.getSelectionModel().select(LocalDate.now().getYear() + "");
        changueMes();
        lblfecha.setText(getNombreDia(oFecha.getDayOfWeek().getValue()) + " " + oFecha.getDayOfMonth() + " de " + getMesNum(oFecha.getMonthValue()));
        initTable();
    }

    void cargarSettingsDoctor() {
        //1- poniendo a ninguno pordefcto
        //2- si existe el doctor configurado y está activo: aparece en el combo box
        List<SettingsDoctor> listDoctorG = getSettingsDoctor();
        jcbDoctor1.getSelectionModel().select(doctorNinguno);
        jcbDoctor2.getSelectionModel().select(doctorNinguno);
        jcbDoctor3.getSelectionModel().select(doctorNinguno);
        jcbDoctor4.getSelectionModel().select(doctorNinguno);
        for (SettingsDoctor settingsDoctor : listDoctorG) {
            if (settingsDoctor.getName().equals("jcbDoctor1") && settingsDoctor.getDoctor().isActivo() && !settingsDoctor.getDoctor().isFlag()) {
                jcbDoctor1.getSelectionModel().select(settingsDoctor.getDoctor());
            } else if (settingsDoctor.getName().equals("jcbDoctor2") && settingsDoctor.getDoctor().isActivo() && !settingsDoctor.getDoctor().isFlag()) {
                jcbDoctor2.getSelectionModel().select(settingsDoctor.getDoctor());
            } else if (settingsDoctor.getName().equals("jcbDoctor3") && settingsDoctor.getDoctor().isActivo() && !settingsDoctor.getDoctor().isFlag()) {
                jcbDoctor3.getSelectionModel().select(settingsDoctor.getDoctor());
            } else if (settingsDoctor.getDoctor().isActivo() && !settingsDoctor.getDoctor().isFlag()) {
                jcbDoctor4.getSelectionModel().select(settingsDoctor.getDoctor());
            }
        }
    }

    List<SettingsDoctor> getSettingsDoctor() {
        List<SettingsDoctor> listdc = App.jpa.createQuery("select p from SettingsDoctor p ").getResultList();
        return listdc;
    }

    public void cargarDoctor() {
        List<Doctor> listDoctorG = App.jpa.createQuery("select p from Doctor p where flag = false and activo = true").getResultList();
        ObservableList<Doctor> listDoctor = FXCollections.observableArrayList();
        Persona oper = new Persona();
        doctorNinguno = new Doctor();
        oper.setNombres_apellidos("NINGUNO");
        doctorNinguno.setPersona(oper);
        listDoctor.add(doctorNinguno);
        for (Doctor odoct : listDoctorG) {
            listDoctor.add(odoct);
        }
        jcbDoctor1.setItems(listDoctor);
        jcbDoctor2.setItems(listDoctor);
        jcbDoctor3.setItems(listDoctor);
        jcbDoctor4.setItems(listDoctor);
        cargarSettingsDoctor();
    }

    void cargarMes() {
        ObservableList<String> MES = FXCollections.observableArrayList("ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SETIEMBRE",
                "OCTUBRE", "NOVIEMBRE", "DICIEMBRE");
        jcbMes.setItems(MES);
        jcbMes.getSelectionModel().select(getMesNum(LocalDate.now().getMonthValue()));
    }

    void cargarAnio() {
        ObservableList<String> ANIO = FXCollections.observableArrayList("2022", "2023", "2024", "2025");
        jcbAnio.setItems(ANIO);
        jcbAnio.getSelectionModel().select(LocalDate.now().getYear() + "");
    }

    @FXML
    void changueMes() {
        mostrarDias(numeroDeDiasMes(jcbMes.getSelectionModel().getSelectedItem()));
    }

    void initTable() {

        initTableView1();
        initTableView2();
        initTableView3();
        initTableView4();
    }

    //usado en button
    void setFecha(ActionEvent event) {
        if (btn != null) {
            //evaluando el button seleccionado anteriormente
            btn.setStyle(colorDefault);
            LocalDate locald = (LocalDate) btn.getUserData();
            if (locald.getDayOfWeek().getValue() == 7) {
                btn.setStyle(colorRed);
            }
            if (locald.equals(LocalDate.now())) {
                btn.setStyle(colorPlomo);
            }
        }
        JFXButton buton = (JFXButton) event.getSource();
        btn = buton;
        buton.setStyle(colorYellow);
        oFecha = (LocalDate) buton.getUserData();
        initTable();
        lblfecha.setText(getNombreDia(oFecha.getDayOfWeek().getValue()) + " " + oFecha.getDayOfMonth() + " de " + getMesNum(oFecha.getMonthValue()));
    }

    void modificarSettingsDoctor(JFXComboBox jcb) {
        List<SettingsDoctor> listDoctorSettings = getSettingsDoctor();
        Doctor doctor = (Doctor) jcb.getSelectionModel().getSelectedItem();
        boolean isNuevo = true;
        for (SettingsDoctor oDoctorSettings : listDoctorSettings) {
            if (jcb.getId().equals(oDoctorSettings.getName())) {
                isNuevo = false;
                if (doctor != doctorNinguno) {
                    if (oDoctorSettings.getDoctor() != doctor) {
                        oDoctorSettings.setDoctor(doctor);
                        App.jpa.getTransaction().begin();
                        App.jpa.persist(oDoctorSettings);
                        App.jpa.getTransaction().commit();
                    }
                } else {
                    App.jpa.getTransaction().begin();
                    App.jpa.remove(oDoctorSettings);
                    App.jpa.getTransaction().commit();
                }
                break;
            }
        }
        if (isNuevo) {
            SettingsDoctor sd = new SettingsDoctor(doctor, jcb.getId());
            App.jpa.getTransaction().begin();
            App.jpa.persist(sd);
            App.jpa.getTransaction().commit();
        }
    }

    //usado en combobox
    @FXML
    void setFechaComboBox1(ActionEvent event) {
        initTableView1();
        modificarSettingsDoctor((JFXComboBox) event.getSource());
    }

    @FXML
    void setFechaComboBox2(ActionEvent event) {
        initTableView2();
        modificarSettingsDoctor((JFXComboBox) event.getSource());
    }

    @FXML
    void setFechaComboBox3(ActionEvent event) {
        initTableView3();
        modificarSettingsDoctor((JFXComboBox) event.getSource());
    }

    @FXML
    void setFechaComboBox4(ActionEvent event) {
        initTableView4();
        modificarSettingsDoctor((JFXComboBox) event.getSource());
    }

    void mostrarDias(int Dias) {
        fpDias.getChildren().clear();
        LocalDate fechaNow = LocalDate.now();
        for (int i = 1; i <= Dias; i++) {

            LocalDate fechaCita = LocalDate.of(Integer.parseInt(jcbAnio.getSelectionModel().getSelectedItem()), getNumMes(jcbMes.getSelectionModel().getSelectedItem()), i);
            JFXButton bt = new JFXButton();
            bt.setUserData(fechaCita);
            bt.addEventHandler(ActionEvent.ACTION, event -> setFecha(event));
            bt.setStyle(colorDefault);
            int diaSemana = fechaCita.getDayOfWeek().getValue();
            if (diaSemana == 7) {
                bt.setStyle(colorRed);
            }
            if (fechaCita.equals(fechaNow)) {
                bt.setStyle(colorPlomo);
            }

            bt.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                    if (newPropertyValue) {
                        bt.setStyle(colorBlue);

                        //el button anterior volviendo a filtrar para cambiar sus colores
                        if (btn != null) {
                            LocalDate locald = (LocalDate) btn.getUserData();
                            if (locald.getDayOfWeek().getValue() == 7) {
                                btn.setStyle(colorRed);
                            }
                            if (locald.equals(fechaNow)) {
                                btn.setStyle(colorPlomo);
                            }
                            if (locald.equals(oFecha)) {
                                btn.setStyle(colorYellow);
                            }
                        }

                    } else {
                        bt.setStyle(colorDefault);
                        if (diaSemana == 7) {
                            bt.setStyle(colorRed);
                        }
                        if (fechaCita.equals(fechaNow)) {
                            bt.setStyle(colorPlomo);
                        }
                        if (fechaCita.equals(oFecha)) {
                            bt.setStyle(colorYellow);
                        }
                    }
                }
            });
            bt.setText("" + i);
            FlowPane.setMargin(bt, new Insets(2, 4, 2, 4));
            if (diaSemana != 7) {
                fpDias.getChildren().add(bt);
            }

        }
    }

    void initTableView1() {
        if (jcbDoctor1.getSelectionModel().getSelectedItem() != doctorNinguno) {
            columnHoraAtencion1.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnCitas1.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnEstado1.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnHoraAtencion1.setCellFactory(column -> {
                TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {
                    @Override
                    protected void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText("");
                        } else {
                            setGraphic(null);
                            setText(item.getHora() + " " + item.getAbreviatura());

                        }
                    }
                };
                return cell;
            });

            columnCitas1.setCellFactory(column -> {
                TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {
                    @Override
                    protected void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText("");
                        } else {
                            List<Cita> listCita = App.jpa.createQuery("select p from Cita p  where "
                                    + "iddoctor=" + jcbDoctor1.getSelectionModel().getSelectedItem().getIddoctor() + " and "
                                    + "idhoraatencion = " + item.getIdhoraatencion() + " and "
                                    + "fechacita=" + "'" + oFecha.toString() + "'"
                                    + "order by minuto asc").getResultList();

                            FlowPane fp = new FlowPane();
                            fp.setStyle("");
                            boolean isOcupado = false;
                            double tam = 48.16;
                            for (Cita cita : listCita) {
                                isOcupado = cita.getPaciente() == null;
                                if (isOcupado) {
                                    fp.setStyle("-fx-background-color:YELLOW");
                                    break;
                                }
                                JFXButton button = new JFXButton();
                                button.setUserData(cita);
                                button.setPrefWidth(110);
                                button.setStyle("-fx-font-size: 10; -fx-background-color:#b6deb7; -fx-border-color:#000000");
                                button.setMaxHeight(9);
                                button.setText(cita.getHoraatencion().getHora() + ":" + cita.getMinuto() + " " + cita.getPaciente().getPersona().getNombres_apellidos());
                                button.addEventHandler(ActionEvent.ACTION, event -> modificarCita(event));
                                fp.getChildren().add(button);
                            }
                            fp.setMinHeight(tam);
                            setGraphic(fp);
                            setText(null);
                            setStyle("-fx-pref-height: 0px");
                        }
                    }

                    void modificarCita(ActionEvent event) {
                        JFXButton buton = (JFXButton) event.getSource();
                        Cita oCita = (Cita) buton.getUserData();
                        CitaModificarController oCitaModificarController = (CitaModificarController) mostrarVentana(CitaModificarController.class, "CitaModificar");
                        oCitaModificarController.setController(odc);
                        oCitaModificarController.setCita(oCita);
                        lockedPantalla();

                    }
                };

                return cell;
            });

            Callback<TableColumn<HoraAtencion, HoraAtencion>, TableCell<HoraAtencion, HoraAtencion>> cellFoctory = (TableColumn<HoraAtencion, HoraAtencion> param) -> {
                // make cell containing buttons
                final TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {

                    @Override
                    public void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        //that cell created only on non-empty rows                    
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            int tamHightImag = 20;
                            int tamWidthImag = 20;

                            ImageView editIcon = newImage("add-1.png", tamHightImag, tamWidthImag, item);
                            editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarAgregar(event));
                            editIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagModificarMoved(event));
                            editIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagModificarFuera(event));

                            ImageView editIcon2 = newImage("block-1.png", tamHightImag, tamWidthImag, item);
                            editIcon2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guardarEliminarBloqueo(event));
                            editIcon2.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagBlockMoved(event));
                            editIcon2.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagBlockFuera(event));

                            VBox managebtn = new VBox(editIcon, editIcon2);
                            managebtn.setStyle("-fx-alignment:center");
                            VBox.setMargin(editIcon, new Insets(4, 1, 4, 1));
                            setGraphic(managebtn);
                            setText(null);
                        }
                    }

                    void mostrarAgregar(MouseEvent event) {
                        ImageView buton = (ImageView) event.getSource();
                        HoraAtencion oHora = (HoraAtencion) buton.getUserData();

                        CitaAgregarController oCitaAgregarController = (CitaAgregarController) mostrarVentana(CitaAgregarController.class, "CitaAgregar");
                        oCitaAgregarController.setController(odc);
                        oCitaAgregarController.setPersona(oHora, jcbDoctor1.getSelectionModel().getSelectedItem(), oFecha);
                        lockedPantalla();

                    }

                    @FXML
                    void guardarEliminarBloqueo(MouseEvent event) {
                        ImageView buton = (ImageView) event.getSource();
                        HoraAtencion oHora = (HoraAtencion) buton.getUserData();
                        List<Cita> listCitaOcupada = App.jpa.createQuery("select p from Cita p  where "
                                + "iddoctor=" + jcbDoctor1.getSelectionModel().getSelectedItem().getIddoctor() + " and "
                                + "idhoraatencion = " + oHora.getIdhoraatencion() + " and "
                                + "fechacita=" + "'" + oFecha.toString() + "' and"
                                + " razon = 'OCUPADO'"
                                + "order by minuto asc").getResultList();

                        if (listCitaOcupada.isEmpty()) {
                            Cita ocita = new Cita(jcbDoctor1.getSelectionModel().getSelectedItem(), oHora, oFecha, "OCUPADO");
                            App.jpa.getTransaction().begin();
                            App.jpa.persist(ocita);
                            App.jpa.getTransaction().commit();
                            initTable();
                        } else {
                            App.jpa.getTransaction().begin();
                            App.jpa.remove(listCitaOcupada.get(0));
                            App.jpa.getTransaction().commit();
                            initTable();
                        }
                    }

                    private void imagModificarMoved(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/add-2.png").toExternalForm()));
                    }

                    private void imagModificarFuera(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/add-1.png").toExternalForm()));
                    }

                    private void imagBlockMoved(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/block-2.png").toExternalForm()));
                    }

                    private void imagBlockFuera(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/block-1.png").toExternalForm()));
                    }
                };
                return cell;
            };
            columnEstado1.setCellFactory(cellFoctory);
        }
    }

    void initTableView2() {
        if (jcbDoctor2.getSelectionModel().getSelectedItem() != doctorNinguno) {
            columnHoraAtencion2.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnCitas2.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnEstado2.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnHoraAtencion2.setCellFactory(column -> {
                TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {
                    @Override
                    protected void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText("");
                        } else {
                            setGraphic(null);
                            setText(item.getHora() + " " + item.getAbreviatura());

                        }
                    }
                };
                return cell;
            });
            columnCitas2.setCellFactory(column -> {
                TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {
                    @Override
                    protected void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText("");
                        } else {
                            List<Cita> listCita = App.jpa.createQuery("select p from Cita p  where "
                                    + "iddoctor=" + jcbDoctor2.getSelectionModel().getSelectedItem().getIddoctor() + " and "
                                    + "idhoraatencion = " + item.getIdhoraatencion() + " and "
                                    + "fechacita=" + "'" + oFecha.toString() + "'"
                                    + "order by minuto asc").getResultList();

                            FlowPane fp = new FlowPane();
                            fp.setStyle("");
                            boolean isOcupado = false;
                            double tam = 48.16;
                            for (Cita cita : listCita) {
                                isOcupado = cita.getPaciente() == null;
                                if (isOcupado) {
                                    fp.setStyle("-fx-background-color:YELLOW");
                                    break;
                                }
                                JFXButton button = new JFXButton();
                                button.setUserData(cita);
                                button.setPrefWidth(110);
                                button.setStyle("-fx-font-size: 10; -fx-background-color:#b6deb7; -fx-border-color:#000000");
                                button.setMaxHeight(9);
                                button.setText(cita.getHoraatencion().getHora() + ":" + cita.getMinuto() + " " + cita.getPaciente().getPersona().getNombres_apellidos());
                                button.addEventHandler(ActionEvent.ACTION, event -> modificarCita(event));
                                fp.getChildren().add(button);
                            }
                            fp.setMinHeight(tam);
                            setGraphic(fp);
                            setText(null);
                            setStyle("-fx-pref-height: 0px");
                        }
                    }

                    void modificarCita(ActionEvent event) {
                        JFXButton buton = (JFXButton) event.getSource();
                        Cita oCita = (Cita) buton.getUserData();
                        CitaModificarController oCitaModificarController = (CitaModificarController) mostrarVentana(CitaModificarController.class, "CitaModificar");
                        oCitaModificarController.setController(odc);
                        oCitaModificarController.setCita(oCita);
                        lockedPantalla();

                    }
                };

                return cell;
            });

            Callback<TableColumn<HoraAtencion, HoraAtencion>, TableCell<HoraAtencion, HoraAtencion>> cellFoctory = (TableColumn<HoraAtencion, HoraAtencion> param) -> {
                // make cell containing buttons
                final TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {

                    @Override
                    public void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        //that cell created only on non-empty rows                    
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            int tamHightImag = 20;
                            int tamWidthImag = 20;

                            ImageView editIcon = newImage("add-1.png", tamHightImag, tamWidthImag, item);
                            editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarAgregar(event));
                            editIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagModificarMoved(event));
                            editIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagModificarFuera(event));

                            ImageView editIcon2 = newImage("block-1.png", tamHightImag, tamWidthImag, item);
                            editIcon2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guardarEliminarBloqueo(event));
                            editIcon2.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagBlockMoved(event));
                            editIcon2.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagBlockFuera(event));

                            VBox managebtn = new VBox(editIcon, editIcon2);
                            managebtn.setStyle("-fx-alignment:center");
                            VBox.setMargin(editIcon, new Insets(4, 1, 4, 1));
                            setGraphic(managebtn);
                            setText(null);
                        }
                    }

                    void mostrarAgregar(MouseEvent event) {
                        ImageView buton = (ImageView) event.getSource();
                        HoraAtencion oHora = (HoraAtencion) buton.getUserData();

                        CitaAgregarController oCitaAgregarController = (CitaAgregarController) mostrarVentana(CitaAgregarController.class, "CitaAgregar");
                        oCitaAgregarController.setController(odc);
                        oCitaAgregarController.setPersona(oHora, jcbDoctor2.getSelectionModel().getSelectedItem(), oFecha);
                        lockedPantalla();

                    }

                    @FXML
                    void guardarEliminarBloqueo(MouseEvent event) {
                        ImageView buton = (ImageView) event.getSource();
                        HoraAtencion oHora = (HoraAtencion) buton.getUserData();
                        List<Cita> listCitaOcupada = App.jpa.createQuery("select p from Cita p  where "
                                + "iddoctor=" + jcbDoctor2.getSelectionModel().getSelectedItem().getIddoctor() + " and "
                                + "idhoraatencion = " + oHora.getIdhoraatencion() + " and "
                                + "fechacita=" + "'" + oFecha.toString() + "' and"
                                + " razon = 'OCUPADO'"
                                + "order by minuto asc").getResultList();

                        if (listCitaOcupada.isEmpty()) {
                            Cita ocita = new Cita(jcbDoctor2.getSelectionModel().getSelectedItem(), oHora, oFecha, "OCUPADO");
                            App.jpa.getTransaction().begin();
                            App.jpa.persist(ocita);
                            App.jpa.getTransaction().commit();
                            initTable();
                        } else {
                            App.jpa.getTransaction().begin();
                            App.jpa.remove(listCitaOcupada.get(0));
                            App.jpa.getTransaction().commit();
                            initTable();
                        }
                    }

                    private void imagModificarMoved(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/add-2.png").toExternalForm()));
                    }

                    private void imagModificarFuera(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/add-1.png").toExternalForm()));
                    }

                    private void imagBlockMoved(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/block-2.png").toExternalForm()));
                    }

                    private void imagBlockFuera(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/block-1.png").toExternalForm()));
                    }
                };
                return cell;
            };
            columnEstado2.setCellFactory(cellFoctory);
        }
    }

    void initTableView3() {
        if (jcbDoctor3.getSelectionModel().getSelectedItem() != doctorNinguno) {
            columnHoraAtencion3.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnCitas3.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnEstado3.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnHoraAtencion3.setCellFactory(column -> {
                TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {
                    @Override
                    protected void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText("");
                        } else {
                            setGraphic(null);
                            setText(item.getHora() + " " + item.getAbreviatura());

                        }
                    }
                };
                return cell;
            });

            columnCitas3.setCellFactory(column -> {
                TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {
                    @Override
                    protected void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText("");
                        } else {
                            List<Cita> listCita = App.jpa.createQuery("select p from Cita p  where "
                                    + "iddoctor=" + jcbDoctor3.getSelectionModel().getSelectedItem().getIddoctor() + " and "
                                    + "idhoraatencion = " + item.getIdhoraatencion() + " and "
                                    + "fechacita=" + "'" + oFecha.toString() + "'"
                                    + "order by minuto asc").getResultList();

                            FlowPane fp = new FlowPane();
                            double tam = 48.16;
                            for (Cita cita : listCita) {
                                JFXButton button = new JFXButton();
                                button.setUserData(cita);
                                button.setPrefWidth(110);
                                button.setStyle("-fx-font-size: 10; -fx-background-color:#b6deb7; -fx-border-color:#000000");
                                button.setMaxHeight(9);
                                button.setText(cita.getHoraatencion().getHora() + ":" + cita.getMinuto() + " " + cita.getPaciente().getPersona().getNombres_apellidos());
                                button.addEventHandler(ActionEvent.ACTION, event -> modificarCita(event));
                                fp.getChildren().add(button);
                            }
                            fp.setMinHeight(tam);
                            setGraphic(fp);
                            setText(null);
                            setStyle("-fx-pref-height: 0px");
                        }
                    }

                    void modificarCita(ActionEvent event) {
                        JFXButton buton = (JFXButton) event.getSource();
                        Cita oCita = (Cita) buton.getUserData();
                        CitaModificarController oCitaModificarController = (CitaModificarController) mostrarVentana(CitaModificarController.class, "CitaModificar");
                        oCitaModificarController.setController(odc);
                        oCitaModificarController.setCita(oCita);
                        lockedPantalla();

                    }
                };

                return cell;
            });

            Callback<TableColumn<HoraAtencion, HoraAtencion>, TableCell<HoraAtencion, HoraAtencion>> cellFoctory = (TableColumn<HoraAtencion, HoraAtencion> param) -> {
                // make cell containing buttons
                final TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {

                    @Override
                    public void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        //that cell created only on non-empty rows                    
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            int tamHightImag = 20;
                            int tamWidthImag = 20;

                            ImageView editIcon = newImage("add-1.png", tamHightImag, tamWidthImag, item);
                            editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarAgregar(event));
                            editIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagModificarMoved(event));
                            editIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagModificarFuera(event));

                            ImageView editIcon2 = newImage("block-1.png", tamHightImag, tamWidthImag, item);
                            editIcon2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarAgregar(event));
                            editIcon2.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagBlockMoved(event));
                            editIcon2.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagBlockFuera(event));

                            VBox managebtn = new VBox(editIcon, editIcon2);
                            managebtn.setStyle("-fx-alignment:center");
                            VBox.setMargin(editIcon, new Insets(4, 1, 4, 1));
                            setGraphic(managebtn);
                            setText(null);
                        }
                    }

                    void mostrarAgregar(MouseEvent event) {
                        ImageView buton = (ImageView) event.getSource();
                        HoraAtencion oHora = (HoraAtencion) buton.getUserData();

                        CitaAgregarController oCitaAgregarController = (CitaAgregarController) mostrarVentana(CitaAgregarController.class, "CitaAgregar");
                        oCitaAgregarController.setController(odc);
                        oCitaAgregarController.setPersona(oHora, jcbDoctor3.getSelectionModel().getSelectedItem(), oFecha);
                        lockedPantalla();

                    }

                    private void imagModificarMoved(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/add-2.png").toExternalForm()));
                    }

                    private void imagModificarFuera(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/add-1.png").toExternalForm()));
                    }

                    private void imagBlockMoved(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/block-2.png").toExternalForm()));
                    }

                    private void imagBlockFuera(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/block-1.png").toExternalForm()));
                    }
                };
                return cell;
            };
            columnEstado3.setCellFactory(cellFoctory);
        }
    }

    void initTableView4() {
        if (jcbDoctor4.getSelectionModel().getSelectedItem() != doctorNinguno) {
            columnHoraAtencion4.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnCitas4.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnEstado4.setCellValueFactory(new PropertyValueFactory<HoraAtencion, HoraAtencion>("horaatencion"));
            columnHoraAtencion4.setCellFactory(column -> {
                TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {
                    @Override
                    protected void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText("");
                        } else {
                            setGraphic(null);
                            setText(item.getHora() + " " + item.getAbreviatura());

                        }
                    }
                };
                return cell;
            });

            columnCitas4.setCellFactory(column -> {
                TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {
                    @Override
                    protected void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText("");
                        } else {
                            List<Cita> listCita = App.jpa.createQuery("select p from Cita p  where "
                                    + "iddoctor=" + jcbDoctor4.getSelectionModel().getSelectedItem().getIddoctor() + " and "
                                    + "idhoraatencion = " + item.getIdhoraatencion() + " and "
                                    + "fechacita=" + "'" + oFecha.toString() + "'"
                                    + "order by minuto asc").getResultList();

                            FlowPane fp = new FlowPane();
                            fp.setStyle("");
                            boolean isOcupado = false;
                            double tam = 48.16;
                            for (Cita cita : listCita) {
                                isOcupado = cita.getPaciente() == null;
                                if (isOcupado) {
                                    fp.setStyle("-fx-background-color:YELLOW");
                                    break;
                                }
                                JFXButton button = new JFXButton();
                                button.setUserData(cita);
                                button.setPrefWidth(110);
                                button.setStyle("-fx-font-size: 10; -fx-background-color:#b6deb7; -fx-border-color:#000000");
                                button.setMaxHeight(9);
                                button.setText(cita.getHoraatencion().getHora() + ":" + cita.getMinuto() + " " + cita.getPaciente().getPersona().getNombres_apellidos());
                                button.addEventHandler(ActionEvent.ACTION, event -> modificarCita(event));
                                fp.getChildren().add(button);
                            }
                            fp.setMinHeight(tam);
                            setGraphic(fp);
                            setText(null);
                            setStyle("-fx-pref-height: 0px");
                        }
                    }

                    void modificarCita(ActionEvent event) {
                        JFXButton buton = (JFXButton) event.getSource();
                        Cita oCita = (Cita) buton.getUserData();
                        CitaModificarController oCitaModificarController = (CitaModificarController) mostrarVentana(CitaModificarController.class, "CitaModificar");
                        oCitaModificarController.setController(odc);
                        oCitaModificarController.setCita(oCita);
                        lockedPantalla();

                    }
                };

                return cell;
            });

            Callback<TableColumn<HoraAtencion, HoraAtencion>, TableCell<HoraAtencion, HoraAtencion>> cellFoctory = (TableColumn<HoraAtencion, HoraAtencion> param) -> {
                // make cell containing buttons
                final TableCell<HoraAtencion, HoraAtencion> cell = new TableCell<HoraAtencion, HoraAtencion>() {

                    @Override
                    public void updateItem(HoraAtencion item, boolean empty) {
                        super.updateItem(item, empty);
                        //that cell created only on non-empty rows                    
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            int tamHightImag = 20;
                            int tamWidthImag = 20;

                            ImageView editIcon = newImage("add-2.png", tamHightImag, tamWidthImag, item);
                            editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarAgregar(event));
                            editIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagModificarMoved(event));
                            editIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagModificarFuera(event));

                            ImageView editIcon2 = newImage("block-2.png", tamHightImag, tamWidthImag, item);
                            editIcon2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guardarEliminarBloqueo(event));
                            editIcon2.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagBlockMoved(event));
                            editIcon2.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagBlockFuera(event));

                            VBox managebtn = new VBox(editIcon, editIcon2);
                            managebtn.setStyle("-fx-alignment:center");
                            VBox.setMargin(editIcon, new Insets(4, 0, 4, 0));
                            setGraphic(managebtn);
                            setText(null);
                        }
                    }

                    void mostrarAgregar(MouseEvent event) {
                        ImageView buton = (ImageView) event.getSource();
                        HoraAtencion oHora = (HoraAtencion) buton.getUserData();

                        CitaAgregarController oCitaAgregarController = (CitaAgregarController) mostrarVentana(CitaAgregarController.class, "CitaAgregar");
                        oCitaAgregarController.setController(odc);
                        oCitaAgregarController.setPersona(oHora, jcbDoctor4.getSelectionModel().getSelectedItem(), oFecha);
                        lockedPantalla();
                    }
                    
                     @FXML
                    void guardarEliminarBloqueo(MouseEvent event) {
                        ImageView buton = (ImageView) event.getSource();
                        HoraAtencion oHora = (HoraAtencion) buton.getUserData();
                        List<Cita> listCitaOcupada = App.jpa.createQuery("select p from Cita p  where "
                                + "iddoctor=" + jcbDoctor2.getSelectionModel().getSelectedItem().getIddoctor() + " and "
                                + "idhoraatencion = " + oHora.getIdhoraatencion() + " and "
                                + "fechacita=" + "'" + oFecha.toString() + "' and"
                                + " razon = 'OCUPADO'"
                                + "order by minuto asc").getResultList();

                        if (listCitaOcupada.isEmpty()) {
                            Cita ocita = new Cita(jcbDoctor2.getSelectionModel().getSelectedItem(), oHora, oFecha, "OCUPADO");
                            App.jpa.getTransaction().begin();
                            App.jpa.persist(ocita);
                            App.jpa.getTransaction().commit();
                            initTable();
                        } else {
                            App.jpa.getTransaction().begin();
                            App.jpa.remove(listCitaOcupada.get(0));
                            App.jpa.getTransaction().commit();
                            initTable();
                        }
                    }


                    private void imagModificarMoved(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/add-1.png").toExternalForm()));
                    }

                    private void imagModificarFuera(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/add-2.png").toExternalForm()));
                    }

                    private void imagBlockMoved(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/block-1.png").toExternalForm()));
                    }

                    private void imagBlockFuera(MouseEvent event) {
                        ImageView imag = (ImageView) event.getSource();
                        imag.setImage(new Image(getClass().getResource("/imagenes/block-2.png").toExternalForm()));
                    }
                };
                return cell;
            };
            columnEstado4.setCellFactory(cellFoctory);
        }
    }

    public void lockedPantalla() {
        if (ap.isDisable()) {
            ap.setDisable(false);
        } else {
            ap.setDisable(true);
        }
    }

    ImageView newImage(String nombreImagen, int hight, int width, Object item) {
        ImageView imag = new ImageView(new Image(getClass().getResource("/imagenes/" + nombreImagen).toExternalForm()));
        imag.setFitHeight(hight);
        imag.setFitWidth(width);
        imag.setUserData(item);
        imag.setStyle(
                " -fx-cursor: hand;"
        );
        return imag;
    }

    @FXML
    void imagSettingsMoved(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/settings-2.png").toExternalForm()));
    }

    @FXML
    void imagSettingsFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/settings-1.png").toExternalForm()));
    }

    @FXML
    void imagFeriadoMoved(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/feriado-2.png").toExternalForm()));
    }

    @FXML
    void imagFeriadoFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/feriado-1.png").toExternalForm()));
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
        stage.initOwner(((Stage) ap.getScene().getWindow()));
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

    public int numeroDeDiasMes(String mes) {
        int numeroDias = -1;
        switch (mes) {
            case "ENERO":
            case "MARZO":
            case "MAYO":
            case "JULIO":
            case "AGOSTO":
            case "OCTUBRE":
            case "DICIEMBRE":
                numeroDias = 31;
                break;
            case "ABRIL":
            case "JUNIO":
            case "SETIEMBRE":
            case "NOVIEMBRE":
                numeroDias = 30;
                break;
            case "FEBRERO":

                Date anioActual = new Date();
                if (esBisiesto(Integer.parseInt(jcbAnio.getSelectionModel().getSelectedItem()))) {
                    numeroDias = 29;
                } else {
                    numeroDias = 28;
                }
                break;
        }
        return numeroDias;
    }

    public boolean esBisiesto(int anio) {
        GregorianCalendar calendar = new GregorianCalendar();
        boolean esBisiesto = false;
        if (calendar.isLeapYear(anio)) {
            esBisiesto = true;
        }
        return esBisiesto;
    }

    public int getNumMes(String mes) {
        int numMes = -1;
        switch (mes) {
            case "ENERO":
                numMes = 1;
                break;
            case "FEBRERO":
                numMes = 2;
                break;
            case "MARZO":
                numMes = 3;
                break;
            case "ABRIL":
                numMes = 4;
                break;
            case "MAYO":
                numMes = 5;
                break;
            case "JUNIO":
                numMes = 6;
                break;
            case "JULIO":
                numMes = 7;
                break;
            case "AGOSTO":
                numMes = 8;
                break;
            case "SETIEMBRE":
                numMes = 9;
                break;
            case "OCTUBRE":
                numMes = 10;
                break;
            case "NOVIEMBRE":
                numMes = 11;
                break;
            case "DICIEMBRE":
                numMes = 12;
                break;
        }
        return numMes;
    }

    public String getMesNum(int mes) {
        String mesNum = "";
        switch (mes) {
            case 1:
                mesNum = "ENERO";
                break;
            case 2:
                mesNum = "FEBRERO";
                break;
            case 3:
                mesNum = "MARZO";
                break;
            case 4:
                mesNum = "ABRIL";
                break;
            case 5:
                mesNum = "MAYO";
                break;
            case 6:
                mesNum = "JUNIO";
                break;
            case 7:
                mesNum = "JULIO";
                break;
            case 8:
                mesNum = "AGOSTO";
                break;
            case 9:
                mesNum = "SETIEMBRE";
                break;
            case 10:
                mesNum = "OCTUBRE";
                break;
            case 11:
                mesNum = "NOVIEMBRE";
                break;
            case 12:
                mesNum = "DICIEMBRE";
                break;
        }
        return mesNum;
    }

    public String getNombreDia(int dia) {
        String nombreDia = "";
        switch (dia) {
            case 1:
                nombreDia = "LUNES";
                break;
            case 2:
                nombreDia = "MARTES";
                break;
            case 3:
                nombreDia = "MIÉRCOLES";
                break;
            case 4:
                nombreDia = "JUEVES";
                break;
            case 5:
                nombreDia = "VIERNES";
                break;
            case 6:
                nombreDia = "SÁBADO";
                break;
            case 7:
                nombreDia = "DOMINGO";
                break;
        }
        return nombreDia;
    }

    public void setController(VerPacienteController odc) {
        this.oVerPacienteController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    @FXML
    void cerrar() {
        oVerPacienteController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }

}
