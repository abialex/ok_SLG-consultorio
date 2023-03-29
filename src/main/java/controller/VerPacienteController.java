/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Historia_clinica;
import Entidades.Persona;
import Entidades.Presupuesto;
import Pdf.Historiaclinicapdf;
import Util.HttpMethods;
import Util.UtilClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllerDoctor.DoctorVerController;
import emergente.AlertConfirmarController;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
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
    FlowPane fp_pagination;

    @FXML
    private JFXTextField jtfbuscar, jtf_buscar_hcl;

    @FXML
    private TableView<Historia_clinica> tablePersona;

    @FXML
    private TableColumn<Persona, Persona> tableDni, tableTelefono, tableDomicilio, tableOcupacion;

    @FXML
    private TableColumn<Persona, Persona> tableNombre;

    @FXML
    private TableColumn<Historia_clinica, Historia_clinica> tableOpcion;

    @FXML
    private TableColumn<Persona, Integer> columnID;

    @FXML
    private TableColumn<Persona, Persona> tableAdulto;

    RegistrarPacienteController oRegistrarPacienteController;
    ObservableList<Historia_clinica> list_observable_hcl = FXCollections.observableArrayList();
    private double x = 0;
    private double y = 0;
    Stage stagePrincipal;
    VerPacienteController odc = this;
    AlertConfirmarController oAlertConfimarController = new AlertConfirmarController();
    Persona oPersonaEliminar;
    int indexEliminar;
    Alert alert = new Alert(Alert.AlertType.WARNING);
    HttpMethods http = new HttpMethods();
    UtilClass oUtilClass = new UtilClass();
    List<Historia_clinica> list_hcl_response;
    List<Historia_clinica> list_hcl_filter=new ArrayList<>();
    int indexPagina;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getListHistoriaClinica();
        initTableView();

        tablePersona.setItems(list_observable_hcl);
        initRestricciones();
        // TODO
    }
    
     void initRestricciones() {
        jtf_buscar_hcl.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros4(event));
        
   
    }

    void setController(RegistrarPacienteController aThis) {
        this.oRegistrarPacienteController = aThis;
    }

    void addHistoriaClinica(Historia_clinica hcl){
        list_hcl_response.add(hcl);
        list_hcl_filter.add(hcl);
    }

    @FXML
    void getListHistoriaClinica() {
        String filtro=jtfbuscar.getText();
        filtro=filtro.length()==0? "_": filtro;//para el link get
        list_hcl_response= http.getList(Historia_clinica.class, "historia_clinica/HistoriaClinicaList/"+filtro);
        Collections.reverse(list_hcl_response);
        for (Historia_clinica hcl: list_hcl_response) {
            list_hcl_filter.add(hcl);

        }
        displayPaginas(true);
    }
    @FXML
    void buscar_hcl() {
        if (!jtf_buscar_hcl.getText().isEmpty()) {
            int idhistoria_clinica=Integer.parseInt(jtf_buscar_hcl.getText());
            list_hcl_filter.clear();
            for(Historia_clinica hcl: list_hcl_response){
                if(hcl.getIdhistoria_clinica()==idhistoria_clinica){
                    list_hcl_filter.add(hcl);

                }
            }
            displayPaginas(false);
        }
    }
    @FXML
    void lookforHistoriaClinica(){
        list_hcl_filter.clear();
        for (Historia_clinica hcl: list_hcl_response) {
                if(hcl.getPersona().getNombres().contains(jtfbuscar.getText()) || hcl.getPersona().getDni().contains(jtfbuscar.getText())){
                    list_hcl_filter.add(hcl);
                }
        }
        if(jtfbuscar.getText().isEmpty()){
        displayPaginas(true);}
        else{
            displayPaginas(false);
        }
    }

    List<JFXButton> listaBotonesPagina = new ArrayList<>();
    void displayPaginas(boolean iSLastPagina){
        fp_pagination.getChildren().clear();
        int paginas = (list_hcl_filter.size()/10);
        paginas = list_hcl_filter.size()-paginas*10>0 ? paginas+1:paginas;
        for (int i = 0; i <  paginas; i++) {
            JFXButton btn_pagina = new JFXButton(i+1 < 10 ? "0" + (i+1) : "" + (i+1));
            FlowPane.setMargin(btn_pagina, new Insets(5, 2, 1, 2));
            btn_pagina.getStyleClass().add("button-forma1");
            btn_pagina.setUserData(((i+1)));
            btn_pagina.addEventHandler(ActionEvent.ACTION, event -> setPagina(event));
            listaBotonesPagina.add(btn_pagina);
            fp_pagination.getChildren().add(btn_pagina);
        }

        if(iSLastPagina){
            displayHistoriaClinica(paginas);
            indexPagina=paginas;
        }
        else{
            displayHistoriaClinica(1);
            indexPagina=1;
        }

    }

    void setPagina(ActionEvent event){
        JFXButton buton = (JFXButton) event.getSource();
        int pagina=(int) buton.getUserData();
        indexPagina=pagina;
        displayHistoriaClinica(pagina);
    }
    void displayHistoriaClinica(int pagina){
        boolean isLasPage = (pagina*10) > list_hcl_filter.size();
        int index=pagina-1;
        list_observable_hcl.clear();
        if(isLasPage){

            for(Historia_clinica hcl: list_hcl_filter.subList(index*10,list_hcl_filter.size())) {
                list_observable_hcl.add(hcl);}
        }
        else{
            for(Historia_clinica hcl: list_hcl_filter.subList(index*10,(index*10)+10)){
                list_observable_hcl.add(hcl);
            }
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
        if (!list_observable_hcl.isEmpty()) {
            tablePersona.getSelectionModel().select(list_observable_hcl.get(list_observable_hcl.size() - 1));
        }
    }

    void selectModificado(Historia_clinica historia_clinica) {
        if (!list_observable_hcl.isEmpty()) {
            for (Historia_clinica hcl : list_observable_hcl) {
                if (historia_clinica == hcl) {
                    tablePersona.getSelectionModel().select(hcl);
                    break;
                }
            }
        }
    }

    public void eliminar() {
        if (indexEliminar != -1) {
            oPersonaEliminar.setFlag(true);
            list_observable_hcl.remove(indexEliminar);
        }
    }

    void initTableView() {
        columnID.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("idhistoria_clinica"));
        tableDni.setCellValueFactory(new PropertyValueFactory<Persona, Persona>("persona"));
        tableNombre.setCellValueFactory(new PropertyValueFactory<Persona, Persona>("persona"));
        tableTelefono.setCellValueFactory(new PropertyValueFactory<Persona, Persona>("persona"));
        tableDomicilio.setCellValueFactory(new PropertyValueFactory<Persona, Persona>("persona"));
        tableOcupacion.setCellValueFactory(new PropertyValueFactory<Persona, Persona>("persona"));
        tableAdulto.setCellValueFactory(new PropertyValueFactory<Persona, Persona>("persona"));
        tableOpcion.setCellValueFactory(new PropertyValueFactory<Historia_clinica, Historia_clinica>("historia_clinica"));

        tableNombre.setCellFactory(column -> {
            TableCell<Persona, Persona> cell = new TableCell<Persona, Persona>() {
                @Override
                protected void updateItem(Persona item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {                      
                        setText(item.getNombres() + " " + item.getAp_paterno() + " " + item.getAp_materno());
                    }
                }
            };

            return cell;
        });

        columnID.setCellFactory(column -> {
            TableCell<Persona, Integer> cell = new TableCell<Persona, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {


                        setText(item+"");
                        setStyle("-fx-alignment: center;");
                    }
                }
            };

            return cell;
        });

        tableAdulto.setCellFactory(column -> {
            TableCell<Persona, Persona> cell = new TableCell<Persona, Persona>() {
                @Override
                protected void updateItem(Persona item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Period period = Period.between(item.getFecha_cumple(), LocalDate.now());
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

        Callback<TableColumn<Historia_clinica, Historia_clinica>, TableCell<Historia_clinica, Historia_clinica>> cellFoctory = (TableColumn<Historia_clinica, Historia_clinica> param) -> {
            // make cell containing buttons
            final TableCell<Historia_clinica, Historia_clinica> cell = new TableCell<Historia_clinica, Historia_clinica>() {

                @Override
                public void updateItem(Historia_clinica item, boolean empty) {
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
                    Historia_clinica ohistoria_clinica = (Historia_clinica) buton.getUserData();
                    ModificarPacienteController oModificarPacienteController = (ModificarPacienteController) mostrarVentana(ModificarPacienteController.class, "ModificarPaciente");
                    oModificarPacienteController.setController(odc);
                    oModificarPacienteController.setPersona(ohistoria_clinica.getPersona(),ohistoria_clinica,indexPagina);
                    lockedPantalla();
                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    /*
                    for (int i = 0; i < list_historia_clinica.size(); i++) {
                        if (list_historia_clinica.get(i).getIdpersona() == (Integer) imag.getUserData()) {
                            oPersonaEliminar = list_historia_clinica.get(i);
                            indexEliminar = i;
                            oAlertConfimarController = (AlertConfirmarController) mostrarVentana(AlertConfirmarController.class, "/fxml/AlertConfirmar");
                            oAlertConfimarController.setController(odc);
                            oAlertConfimarController.setMensaje(" ¿Está seguro de eliminar al \n paciente? \n \n" + " " + oPersonaEliminar.getNombres_apellidos());
                            lockedPantalla();
                            break;
                        }
                    }
                    */
                }

                void mostrarImprimir(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    Historia_clinica oHistoria_clinica = (Historia_clinica) imag.getUserData();
                    System.out.println(oHistoria_clinica);

                    try {
                        Historiaclinicapdf.ImprimirHistoriaClinica(oHistoria_clinica);
                        File file = new File("Pdf\\historia_clinica_" + oHistoria_clinica.getPersona().getNombres()+ "_" + oHistoria_clinica.getPersona().getDni() + ".pdf");
                        Desktop.getDesktop().open(file);

                    } catch (IOException ex) {
                        Logger.getLogger(VerPacienteController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                void mostrarCaja(MouseEvent event) {
                    ImageView buton = (ImageView) event.getSource();
                    Historia_clinica oHistoria_clinica = (Historia_clinica) buton.getUserData();

                    Presupuesto opresupuesto=http.ConsultObject(Presupuesto.class, "historia_clinica/GetPresupuesto",+oHistoria_clinica.getIdhistoria_clinica()+"");
                    if(opresupuesto!=null){
                        CajaVerController oCajaVerController = (CajaVerController) mostrarVentana(CajaVerController.class, "CajaVer");
                        oCajaVerController.setPersona(oHistoria_clinica, opresupuesto);
                        oCajaVerController.setController(odc);
                        lockedPantalla();
                    }
                    else{
                        oUtilClass.mostrar_alerta_warning("Tratamiento","No tiene un presupuesto");

                    }
                    }

                void mostrarPresupuesto(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    Historia_clinica oHistoria_clinica = (Historia_clinica) imag.getUserData();
                    PresupuestoVerController oPresupuestoVerController = (PresupuestoVerController) mostrarVentana(PresupuestoVerController.class, "PresupuestoVer");
                    oPresupuestoVerController.setPersona(oHistoria_clinica.getPersona(), oHistoria_clinica);
                    oPresupuestoVerController.setController(odc);
                    lockedPantalla();
                }

                void mostrarCarpeta(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    /* for (int i = 0; i < list_historia_clinica.size(); i++) {
                        if (list_historia_clinica.get(i).getIdpersona() == (Integer) imag.getUserData()) {

                            Persona opersona = list_historia_clinica.get(i);
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
                    } */
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
            TableCell<Persona, Persona> cell = new TableCell<Persona, Persona>() {
                @Override
                protected void updateItem(Persona item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();

                        label.setText(item.getDni());
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
            TableCell<Persona, Persona> cell = new TableCell<Persona, Persona>() {
                @Override
                protected void updateItem(Persona item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();

                        label.setText(item.getOcupacion());
                        //fin
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999;");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });
        
        tableTelefono.setCellFactory(column -> {
            TableCell<Persona, Persona> cell = new TableCell<Persona, Persona>() {
                @Override
                protected void updateItem(Persona item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();

                        label.setText(item.getTelefono());
                        //fin
                        label.setStyle("-fx-font-size: 12; -fx-alignment: center; -fx-max-width:999;");
                        setGraphic(label);
                        setText(null);
                    }
                }
            };
            return cell;
        });
        
        tableDomicilio.setCellFactory(column -> {
            TableCell<Persona, Persona> cell = new TableCell<Persona, Persona>() {
                @Override
                protected void updateItem(Persona item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label label = new Label();

                        label.setText(item.getDomicilio());
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
    void SoloNumerosEnteros4(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 4) {
            event.consume();
        }
    }
}
