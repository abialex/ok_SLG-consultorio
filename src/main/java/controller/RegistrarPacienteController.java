/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Persona;
import com.itextpdf.layout.element.Image;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class RegistrarPacienteController implements Initializable {
    
    //Atributos de la ventana
    @FXML Accordion accordion;
    @FXML Accordion accordionAct;
    @FXML TitledPane tpAnamnesis;
    @FXML TitledPane tpAnamnesisAct;
    @FXML JFXDatePicker dtpicker;
    @FXML Button jbtnGuardar;
    @FXML JFXTextField jtfbuscar;
    @FXML Button jbtnbuscar;
    @FXML TextField jtfbuscarAct;
    @FXML Label jlblnombresyapellidos;
    @FXML Label jlbldni;
    @FXML Button jbtnimprimir;


    //Atributos
    @FXML TextField jtfNombresyApellidos;
    @FXML TextField jtfDomicilio;
    @FXML TextField jtfDni;
    @FXML TextField jtfDia;
    @FXML TextField jtfMes;
    @FXML TextField jtfanio;
    @FXML TextField jtfTelefono;
    @FXML JFXTextField jtfedad;
    @FXML TextField jtflugarprocedencia;
    @FXML ComboBox<String> jcbestadocivil;
    @FXML ComboBox<String> jcbocupacion;
    @FXML ComboBox<String> jcbsexo;
    //Fin Atributos
    
    //Atributos Actualización
    @FXML TextField jtfNombresyApellidosAct;
    @FXML TextField jtfDomicilioAct;
    @FXML TextField jtfDniAct;
    @FXML TextField jtfDiaAct;
    @FXML TextField jtfMesAct;
    @FXML TextField jtfanioAct;
    @FXML TextField jtfTelefonoAct;
    @FXML JFXTextField jtfedadAct;
    @FXML TextField jtflugarprocedenciaAct;
    @FXML ComboBox<String> jcbestadocivilAct;
    @FXML ComboBox<String> jcbocupacionAct;
    @FXML ComboBox<String> jcbsexoAct;
    
    //Fin Atributos Actualización  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> ESTADO_CIVIL=FXCollections.observableArrayList("CASADO" ,"SOLTERO", "VIUDO");
        ObservableList<String> OCUPACION=FXCollections.observableArrayList("ESTUDIANTE", "UNIVERSITARIO", "TRABAJADOR");
        ObservableList<String> SEXO=FXCollections.observableArrayList("VARÓN", "MUJER", "MARCIANO");
        jcbestadocivil.setItems(ESTADO_CIVIL);
        jcbocupacion.setItems(OCUPACION);
        jcbsexo.setItems(SEXO);
        
        jcbsexoAct.setItems(SEXO);
        jcbestadocivilAct.setItems(ESTADO_CIVIL);
        jcbocupacionAct.setItems(OCUPACION);
     
        
        
        accordion.setExpandedPane(tpAnamnesis);
        accordionAct.setExpandedPane(tpAnamnesisAct);
 
    }    
    @FXML
    void BuscarPaciente(ActionEvent evt){
        Persona opersona=(Persona)App.jpa.createQuery("select p from Persona p where dni="+"'"+jtfbuscar.getText().trim()+"'"
                +" or "+"nombres_apellidos like "+"'%"+jtfbuscar.getText()+"%'").getSingleResult();
        jlblnombresyapellidos.setText(opersona.getNombres_apellidos());
        jlbldni.setText(opersona.getDni());
    }
    
    
    @FXML
    void GuardarPaciente(ActionEvent evt){
       Date fechaNacimiento=new Date();
        fechaNacimiento.setYear(Integer.parseInt(jtfanio.getText().trim())-1900);
        fechaNacimiento.setMonth(Integer.parseInt(jtfMes.getText().trim())-1);
        fechaNacimiento.setDate(Integer.parseInt(jtfDia.getText().trim()));
        fechaNacimiento.setHours(0);
        fechaNacimiento.setMinutes(0);
        fechaNacimiento.setSeconds(0);
        
        
        Persona opersona= new Persona(
        jtfNombresyApellidos.getText().trim(),
        jcbsexo.getSelectionModel().getSelectedItem(),
        24,
        jtfDomicilio.getText().trim(),
        jtfDni.getText().trim(),
        fechaNacimiento,
        jtflugarprocedencia.getText().trim(),
        jcbocupacion.getSelectionModel().getSelectedItem(),
        jcbestadocivil.getSelectionModel().getSelectedItem(),        
        jtfTelefono.getText().trim(),
        "alex@gmail"
        );
        
        App.jpa.getTransaction().begin();
        App.jpa.persist(opersona);
        App.jpa.getTransaction().commit();
        
        List<Persona> list_persona= App.jpa.createQuery("select p from Persona p").getResultList();
        for (Persona persona : list_persona) {
            System.out.println(persona.getNombres_apellidos());
            
        }
    }
    
    @FXML
    void ActualizarPaciente(ActionEvent evt){
        Persona opersona=(Persona)App.jpa.createQuery("select p from Persona p where dni="+"'"+jtfbuscarAct.getText().trim()+"'"
                +" or "+"nombres_apellidos like "+"'%"+jtfbuscarAct.getText().trim()+"%'").getSingleResult();
        jtfNombresyApellidosAct.setText(opersona.getNombres_apellidos());
        jtfDniAct.setText(opersona.getDni());
        System.out.println(opersona.getSexo()+"--------------------");
        jcbsexoAct.getSelectionModel().select(opersona.getSexo());
        //dia
        //mes
        //año
        jtfedadAct.setText(opersona.getEdad()+"");
        jcbestadocivilAct.getSelectionModel().select(opersona.getEstado_civil());
        jtflugarprocedenciaAct.setText(opersona.getLugar_de_procedencia());
        jtfDomicilioAct.setText(opersona.getDomicilio());
        jtfTelefonoAct.setText(opersona.getTelefono());
        jcbocupacionAct.getSelectionModel().select(opersona.getOcupacion());
        
    }
    
    
    
    @FXML
    void ImprimirPaciente(ActionEvent evt) throws IOException{
         File file=new File("pdf\\prueba.pdf");
         Desktop.getDesktop().open(file);
    }
    
}
