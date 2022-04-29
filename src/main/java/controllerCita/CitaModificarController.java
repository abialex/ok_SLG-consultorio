/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerCita;

import Entidades.Cita;
import Entidades.HoraAtencion;
import Entidades.Paciente;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.App;
import controller.VerPacienteController;
import emergente.AlertConfirmarController;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class CitaModificarController implements Initializable {

    @FXML
    private AnchorPane ap;
    @FXML
    private JFXTextField jtfDoctor;

    @FXML
    private JFXTextField jtfFecha;

    @FXML
    private JFXComboBox<HoraAtencion> jcbHora;

    @FXML
    private JFXTextField jtfminuto;

    @FXML
    private JFXTextField jtfPaciente;

    @FXML
    private JFXTextField jtfrazon;

    CitaVerController oCitaVerController;
    Cita Cita;
    AlertConfirmarController oAlertConfimarController = new AlertConfirmarController();
    TableView<HoraAtencion> table;
    private double x = 0;
    private double y = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarHora();
        initRestricciones();
    }

    @FXML
    void modificarCita() {
        if (isComplete()) {
            Cita.setHoraatencion(jcbHora.getSelectionModel().getSelectedItem());
            Cita.setMinuto(jtfminuto.getText());
            Cita.setRazon(jtfrazon.getText());
            App.jpa.getTransaction().begin();
            App.jpa.persist(Cita);
            App.jpa.getTransaction().commit();
            oCitaVerController.actualizarListMesCita();
            table.refresh();
            cerrar();
        }
    }

    @FXML
    void eliminarCita() {
        oAlertConfimarController = (AlertConfirmarController) mostrarVentana(AlertConfirmarController.class, "/fxml/AlertConfirmar");
        oAlertConfimarController.setController(this);
        oAlertConfimarController.setMensaje(" ¿Está seguro de eliminar \n la cita de\n" + " " + Cita.getPaciente().getPersona().getNombres_apellidos() + "?");
        lockedPantalla();
    }

    public void eliminar() {
        App.jpa.getTransaction().begin();
        App.jpa.remove(Cita);
        App.jpa.getTransaction().commit();
        oCitaVerController.actualizarListMesCita();
        table.refresh();
        cerrar();
    }

    public void lockedPantalla() {
        if (ap.isDisable()) {
            ap.setDisable(false);
        } else {
            ap.setDisable(true);
        }
    }

    void cargarHora() {
        List<HoraAtencion> listHora = App.jpa.createQuery("select p from HoraAtencion p order by idhoraatencion asc").getResultList();
        ObservableList<HoraAtencion> listhora = FXCollections.observableArrayList();
        for (HoraAtencion oHora : listHora) {
            listhora.add(oHora);
        }
        jcbHora.setItems(listhora);
    }

    void setController(CitaVerController odc, TableView<HoraAtencion> table) {
        this.table = table;
        this.oCitaVerController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    void setCita(Cita oCita) {
        this.Cita = oCita;
        jtfDoctor.setText(oCita.getDoctor().getPersona().getNombres_apellidos());
        jtfFecha.setText(oCita.getFechacita() + "");
        jcbHora.getSelectionModel().select(oCita.getHoraatencion());
        jtfminuto.setText(oCita.getMinuto());
        jtfPaciente.setText(oCita.getPaciente().getPersona().getNombres_apellidos());
        jtfrazon.setText(oCita.getRazon());
    }

    void initRestricciones() {
        jtfminuto.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros2(event));
    }

    void SoloNumerosEnteros2(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 2) {
            event.consume();
        }
    }

    @FXML
    void cerrar() {
        oCitaVerController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }

    boolean isComplete() {
        boolean aux = true;
        if (jtfminuto.getText().trim().length() == 0) {
            jtfminuto.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfminuto.setStyle("");
        }

        if (jtfrazon.getText().trim().length() == 0) {
            jtfrazon.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfrazon.setStyle("");
        }

        return aux;
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
        scene.getStylesheets().add(generico.getResource("/css/bootstrap3.css").toExternalForm());;
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

}
