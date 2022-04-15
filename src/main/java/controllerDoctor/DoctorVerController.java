/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerDoctor;

import Entidades.Doctor;
import Entidades.Persona;
import Pdf.Historiaclinicapdf;
import Util.FileImagUtil;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
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
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class DoctorVerController implements Initializable {

    @FXML
    private JFXTextField jtfNombres;

    @FXML
    private TableView<Doctor> tableDoctor;

    @FXML
    private TableColumn<Doctor, Persona> columnNombres;

    @FXML
    private TableColumn<Doctor, Integer> columnEstado;

    @FXML
    private TableColumn<Doctor, Doctor> columnActivo;

    ObservableList<Doctor> listDoctor = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateListDoctor();
        initTableView();
        tableDoctor.setItems(listDoctor);
    }

    @FXML
    void updateListDoctor() {
        List<Doctor> olistDoc = App.jpa.createQuery("select p from Doctor p").setMaxResults(10).getResultList();
        listDoctor.clear();
        for (Doctor oDoc : olistDoc) {
            listDoctor.add(oDoc);
        }
    }
    
    @FXML
    void guardarDoctor(){
        if(jtfNombres.getText().length()!=0){
            Persona opersona=new Persona(
                    jtfNombres.getText(),
                    "NA",
                    "NA",
                    "NA", 
                    LocalDate.now(), 
                    "NA", "DOCTOR", "NA");
            Doctor odoctor=new Doctor(opersona);
            App.jpa.getTransaction().begin();
            App.jpa.persist(opersona);
            App.jpa.persist(odoctor);
            App.jpa.getTransaction().commit();
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
                        checkbox.setStyle("-fx-alignment: center; -fx-max-width:999;");
                        checkbox.addEventHandler(ActionEvent.ACTION, event -> changueActivo(event));
                        checkbox.setSelected(!item.isFlag());
                        setGraphic(checkbox);
                        setText(null);
                    }
                }

                void changueActivo(ActionEvent event) {
                    JFXCheckBox check = (JFXCheckBox) event.getSource();
                    Doctor odoc=(Doctor) check.getUserData();
                    odoc.setFlag(!check.isSelected());
                    App.jpa.getTransaction().begin();
                    App.jpa.persist(odoc);
                    App.jpa.getTransaction().commit();
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

                        ImageView editIcon = newImage("modify-1.png", tamHightImag, tamWidthImag, item);
                        editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarModificar(event));
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

                void mostrarModificar(MouseEvent event) {/*
                    ImageView buton = (ImageView) event.getSource();
                    for (Doctor odoc : listDoctor) {
                        if (odoc.getIdpersona() == (Integer) buton.getUserData()) {
                            ModificarPacienteController oModificarPacienteController = (ModificarPacienteController) mostrarVentana(ModificarPacienteController.class, "ModificarPaciente");
                            oModificarPacienteController.setPersona(odoc);
                            oModificarPacienteController.setController(odc);
                            lockedPantalla();
                            break;
                        }
                    }*/
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
        columnEstado.setCellFactory(cellFoctory);
    }

}
