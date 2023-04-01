/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller.Doctor;

import Entidades.Doctor;
import Entidades.Persona;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import controller.Paciente.VerPacienteController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
public class DoctorVerController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfNombres, jtf_ap_paterno, jtf_ap_materno;

    @FXML
    private TableView<Doctor> tableDoctor;

    @FXML
    private TableColumn<Doctor, Persona> columnNombres;

    @FXML
    private TableColumn<Doctor, Integer> columnEstado;

    @FXML
    private TableColumn<Doctor, Doctor> columnActivo;

    ObservableList<Doctor> listDoctor = FXCollections.observableArrayList();
    VerPacienteController oVerPacienteController;
    private double x = 0;
    private double y = 0;
    DoctorVerController odc = this;
    Doctor oDoctorEliminar;
    int indexEliminar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateListDoctor();
        initTableView();
        tableDoctor.setItems(listDoctor);
    }

    @FXML
    void updateListDoctor() {
        /*
        List<Doctor> olistDoc = App.jpa.createQuery("select p from Doctor p where flag = false order by iddoctor DESC").setMaxResults(10).getResultList();
        listDoctor.clear();
        for (Doctor oDoc : olistDoc) {
            listDoctor.add(oDoc);
        }*/
    }

    @FXML
    void guardarDoctor() {
        if (jtfNombres.getText().length() != 0) {
            Persona opersona = new Persona(
                    jtfNombres.getText(),
                    jtf_ap_paterno.getText(),
                    jtf_ap_materno.getText(),
                    "NA",
                    "NA",
                    "NA",
                    LocalDate.now(),
                    "NA", "DOCTOR", "NA",0);
            Doctor odoctor = new Doctor(opersona);
           /* App.jpa.getTransaction().begin();
            App.jpa.persist(opersona);
            App.jpa.persist(odoctor);
            App.jpa.getTransaction().commit();*/
            updateListDoctor();
        }
    }

    void initTableView() {
        columnNombres.setCellValueFactory(new PropertyValueFactory<Doctor, Persona>("persona"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("iddoctor"));
        columnActivo.setCellValueFactory(new PropertyValueFactory<Doctor, Doctor>("doctor"));

        columnActivo.setCellFactory(column -> {
            TableCell<Doctor, Doctor> cell = new TableCell<Doctor, Doctor>() {
                @Override
                protected void updateItem(Doctor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        JFXCheckBox checkbox = new JFXCheckBox();
                        checkbox.setUserData(item);
                        checkbox.setStyle("-fx-alignment: center; -fx-max-width:999; -fx-cursor: hand;");
                        checkbox.addEventHandler(ActionEvent.ACTION, event -> changueActivo(event));
                        checkbox.setSelected(item.isActivo());
                        setGraphic(checkbox);
                        setText(null);
                    }
                }

                void changueActivo(ActionEvent event) {
                    JFXCheckBox check = (JFXCheckBox) event.getSource();
                    Doctor odoc = (Doctor) check.getUserData();
                    odoc.setActivo(check.isSelected());
                    /*
                    App.jpa.getTransaction().begin();
                    App.jpa.persist(odoc);
                    App.jpa.getTransaction().commit();*/
                }

            };

            return cell;
        });

        columnNombres.setCellFactory(column -> {
            TableCell<Doctor, Persona> cell = new TableCell<Doctor, Persona>() {
                @Override
                protected void updateItem(Persona item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        JFXTextField field = new JFXTextField();
                        field.setUserData(item);

                        field.setText(item.getNombres());
                        field.setEditable(false);
                        field.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> changueActivo(event));
                        field.addEventHandler(KeyEvent.KEY_RELEASED, event -> modificar(event));
                        field.focusedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                                if (newPropertyValue) {
                                    field.setStyle("-fx-border-color:BLACK;");
                                } else {
                                    field.setStyle("");
                                }
                            }
                        });
                        setGraphic(null);
                        setText(item.getNombres()+" "+item.getAp_paterno()+" "+item.getAp_materno());
                    }
                }

                void changueActivo(MouseEvent event) {
                    JFXTextField check = (JFXTextField) event.getSource();
                    check.setStyle("-fx-border-color:BLACK;");

                    check.setEditable(true);

                }

                void modificar(KeyEvent event) {
                    JFXTextField check = (JFXTextField) event.getSource();
                    Persona oper = (Persona) check.getUserData();
                    if (event.getCode() == (KeyCode.ENTER)) {
                        if (check.getText().length() != 0) {
                            oper.setNombres(check.getText());
                         /*   App.jpa.getTransaction().begin();
                            App.jpa.persist(oper);
                            App.jpa.getTransaction().commit();*/
                            updateListDoctor();
                        }
                    }
                    if (event.getCode() == (KeyCode.ESCAPE)) {
                        updateListDoctor();
                    }
                }
            };
            return cell;
        });

        Callback<TableColumn<Doctor, Integer>, TableCell<Doctor, Integer>> cellFoctory = (TableColumn<Doctor, Integer> param) -> {
            // make cell containing buttons
            final TableCell<Doctor, Integer> cell = new TableCell<Doctor, Integer>() {

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

                        ImageView editIcon = newImage("delete-1.png", tamHightImag, tamWidthImag, item);
                        editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarEliminar(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagModificarMoved(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagModificarFuera(event));

                        HBox managebtn = new HBox(editIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(editIcon, new Insets(0, 2.75, 0, 2.75));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

                ImageView newImage(String nombreImagen, int hight, int width, Integer item) {
                    ImageView imag = new ImageView(new Image(getClass().getResource("/imagenes/" + nombreImagen).toExternalForm()));
                    imag.setFitHeight(hight);
                    imag.setFitWidth(width);
                    imag.setUserData(item);
                    imag.setStyle(
                            " -fx-cursor: hand;"
                    );
                    return imag;
                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listDoctor.size(); i++) {
                        if (listDoctor.get(i).getIddoctor() == (Integer) imag.getUserData()) {
                            oDoctorEliminar = listDoctor.get(i);
                            indexEliminar = i;
                            lockedPantalla();
                            break;
                        }
                    }
                }

                private void imagModificarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-2.png").toExternalForm()));
                }

                private void imagModificarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-1.png").toExternalForm()));
                }

            };
            return cell;
        };
        columnEstado.setCellFactory(cellFoctory);
    }

    public void eliminar() {
        if (indexEliminar != -1) {
            oDoctorEliminar.setFlag(true);/*
            App.jpa.getTransaction().begin();
            App.jpa.persist(oDoctorEliminar);
            App.jpa.getTransaction().commit();*/
            listDoctor.remove(indexEliminar);
            updateListDoctor();
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

    public void lockedPantalla() {
        if (ap.isDisable()) {
            ap.setDisable(false);
        } else {
            ap.setDisable(true);
        }
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
