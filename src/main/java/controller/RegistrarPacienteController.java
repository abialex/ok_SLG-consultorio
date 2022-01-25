/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Enfermedad;
import Entidades.Historia_clinica;
import Entidades.Paciente;
import Entidades.Paciente_Enfermedad;
import Entidades.Paciente_Pregunta;
import Entidades.Persona;
import Entidades.Pregunta;
import Util.FileImagUtil;
import Pdf.Historiaclinicapdf;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import controller.emergente.AlertController;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
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
    @FXML ImageView jimagperfil;
    
    
    
    /*--------------Atributos---------------*/
    //Anamnesis
    @FXML TextField jtfNombresyApellidos;
    @FXML TextField jtfDomicilio;
    @FXML TextField jtfDni;
    @FXML TextField jtfDia;
    @FXML TextField jtfMes;
    @FXML TextField jtfanio;
    @FXML TextField jtfTelefono;
    @FXML JFXTextField jtfedad;
    @FXML TextField jtflugarprocedencia;
    @FXML ComboBox<String> jcbocupacion;
    @FXML ComboBox<String> jcbsexo;
    @FXML TextField jtfmotivoconsulta;
    
    //Enfermedad actual
    @FXML TextField jtfsintomasEnfermedadActual;
    @FXML TextField jtftiempoEnfermedadActual;
    

    //Antecedentes
    @FXML CheckBox checkalergia;;@FXML TextField jtfantAque;
    @FXML CheckBox checkfiebrereumatica;
    @FXML CheckBox checkanemia;
    @FXML CheckBox checkdiabetes;
    @FXML CheckBox checktuberculosis;
    @FXML CheckBox checkhepatitis;
    @FXML CheckBox checkinfeccionveneria;
    @FXML CheckBox checkenfermedadcardiaca;
    @FXML CheckBox checkgastritis;
    @FXML CheckBox checkepilepsia;
    @FXML CheckBox checkdolordepecho;
    @FXML CheckBox checkneuralgia;
    @FXML CheckBox checkenfermedaddelapiel;
    @FXML CheckBox checkenfermedadrenal;
    @FXML CheckBox checkhipertensionarterial;
        
    @FXML TextField jtfantecedentesFamiliares;
    @FXML TextField jtfotrasEnfermedades;
    
    @FXML CheckBox checkpregunta1;@FXML TextField jtfantPregunta1;
    @FXML CheckBox checkpregunta2;@FXML TextField jtfantPregunta2;
    @FXML CheckBox checkpregunta3;@FXML TextField jtfantPregunta3;
    @FXML CheckBox checkpregunta4;@FXML TextField jtfantPregunta4;
    @FXML CheckBox checkpreguntamujer1;@FXML TextField jtfantpreguntamujer1;  
    @FXML CheckBox checkpreguntamujer2;@FXML TextField jtfantpreguntamujer2; 
    /*----------Fin Atributos---------------*/
    
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
    @FXML ComboBox<String> jcbocupacionAct;
    @FXML ComboBox<String> jcbsexoAct;
    
    //Fin Atributos Actualización  
    
    File fileImag;
    FileImagUtil oFileUtilImag=new FileImagUtil("user.home","Buscar Imagen");
    Persona oPersona;
    
    
    //Botones y métodos de prueba   
    @FXML Button jbtnpruebita;
    @FXML
    void test(ActionEvent event) throws IOException{
        AlertController.Mostrar();
   
    }
    @FXML
    void testClick(ActionEvent event){
        if(jtfantAque.isDisable()){ jtfantAque.setDisable(false);}
        else {                      jtfantAque.setDisable(true); }
             
    }
    
    //Fin Botones y métodos de prueba
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> OCUPACION=FXCollections.observableArrayList("ESTUDIANTE", "UNIVERSITARIO", "TRABAJADOR");
        ObservableList<String> SEXO=FXCollections.observableArrayList("VARÓN", "MUJER", "MARCIANO");
        jcbocupacion.setItems(OCUPACION);
        jcbsexo.setItems(SEXO);
        
        jcbsexoAct.setItems(SEXO);
        jcbocupacionAct.setItems(OCUPACION);
     
        
        
        accordion.setExpandedPane(tpAnamnesis);
        accordionAct.setExpandedPane(tpAnamnesisAct);
 
    }    
    @FXML
    void BuscarPaciente(ActionEvent evt){
        oPersona=(Persona)App.jpa.createQuery("select p from Persona p where dni="+"'"+jtfbuscar.getText().trim()+"'"
                +" or "+"nombres_apellidos like "+"'%"+jtfbuscar.getText()+"%'").getSingleResult();
        jlblnombresyapellidos.setText(oPersona.getNombres_apellidos());
        jlbldni.setText(oPersona.getDni());
    }
    
    
    @FXML
    void GuardarPaciente(ActionEvent evt) throws IOException{
        
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
        jtfTelefono.getText().trim(),
        "alex@gmail",
         oFileUtilImag.guardarImagen(fileImag)        
        );
        
        Paciente opaciente= new Paciente( 
        opersona,
        jtfmotivoconsulta.getText().trim(),
        jtfsintomasEnfermedadActual.getText().trim(),
        jtftiempoEnfermedadActual.getText().trim(),
        jtfotrasEnfermedades.getText().trim(),
        jtfantecedentesFamiliares.getText().trim());     
        
        List<Paciente_Enfermedad> Lista_enfermedadesPaciente=Paciente_relacionar_enfermedad(opaciente);
        List<Paciente_Pregunta> Lista_preguntasPaciente=Paciente_relacionar_pregunta(opaciente);
        
        Historia_clinica ohistoria=new Historia_clinica(
        opaciente,
        new Date(),
        new Date());
        
        //GuardarPaciente
        App.jpa.getTransaction().begin();
        App.jpa.persist(opersona);

        App.jpa.persist(opaciente);

        for (Paciente_Enfermedad paciente_Enfermedad : Lista_enfermedadesPaciente) {
            App.jpa.persist(paciente_Enfermedad);
        }
        for (Paciente_Pregunta paciente_Pregunta : Lista_preguntasPaciente) {
            App.jpa.persist(paciente_Pregunta);
        }
        App.jpa.persist(ohistoria);
        App.jpa.getTransaction().commit(); 
    }
    
    public List<Paciente_Enfermedad> Paciente_relacionar_enfermedad(Paciente opaciente){
        List<Enfermedad> list_enfermedad =App.jpa.createQuery("select p from Enfermedad p ").getResultList();
        List<Paciente_Enfermedad> list_enfermedades_paciente=new ArrayList<Paciente_Enfermedad>();
        Paciente_Enfermedad opaciente_enfermedad = null;
        if(checkalergia.isSelected()){
            for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Alergia")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                jtfantAque.getText().trim());
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }              
            }     
        }
        if(checkanemia.isSelected()){
             for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Anemia")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            } 
            
        }
        
        if(checkdiabetes.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Diabetes")){;
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            }      
        }
        if(checktuberculosis.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Tuberculosis")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            } 
        }
        
        if(checkhepatitis.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Hepatitis infecciosa")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            } 
        }
        
        if(checkinfeccionveneria.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Infección Venérea")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            } 
        }
        if(checkenfermedadcardiaca.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Enfermedad Cardiaca")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            } 
        }
        if(checkgastritis.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Gastritis")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        if(checkepilepsia.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Epilepsia")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        if(checkdolordepecho.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Dolor de Pecho")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        if(checkneuralgia.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Neuralgia")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        if(checkenfermedaddelapiel.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Enfermedad de la piel")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        if(checkenfermedadrenal.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Enfermedad Renal")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        if(checkhipertensionarterial.isSelected()){
                 for (Enfermedad enfermedad : list_enfermedad) {
                if(enfermedad.getNombre().equals("Hipertensión Arterial")){
                opaciente_enfermedad=new Paciente_Enfermedad(
                opaciente,
                enfermedad,
                "");
                list_enfermedades_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        
        
       
        
        
        return list_enfermedades_paciente;
       
        
    }
    
    public List<Paciente_Pregunta> Paciente_relacionar_pregunta(Paciente opaciente){
        List<Pregunta> list_pregunta =App.jpa.createQuery("select p from Pregunta p ").getResultList();
        List<Paciente_Pregunta> list_pregunta_paciente=new ArrayList<Paciente_Pregunta>();
        Paciente_Pregunta opaciente_enfermedad = null;
        boolean isMujer=opaciente.getPersona().getSexo().equals("MUJER");
        if(checkpregunta1.isSelected()){
                 for (Pregunta pregunta : list_pregunta) {
                if(pregunta.getTextopregunta().equals("¿Reacciona  anormalmente a algún medicamento?")){
                opaciente_enfermedad=new Paciente_Pregunta(
                opaciente,
                pregunta,
                jtfantPregunta1.getText().trim(),
                isMujer);
                list_pregunta_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        if(checkpregunta2.isSelected()){
                 for (Pregunta pregunta : list_pregunta) {
                if(pregunta.getTextopregunta().equals("¿Le han realizado alguna intervención quirúrjica?")){
                opaciente_enfermedad=new Paciente_Pregunta(
                opaciente,
                pregunta,
                jtfantPregunta2.getText().trim(),
                isMujer);
                list_pregunta_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        if(checkpregunta3.isSelected()){
                 for (Pregunta pregunta : list_pregunta) {
                if(pregunta.getTextopregunta().equals("¿Está usted tomando algún medicamento?")){
                opaciente_enfermedad=new Paciente_Pregunta(
                opaciente,
                pregunta,
                jtfantPregunta3.getText().trim(),
                isMujer);
                list_pregunta_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        if(checkpregunta4.isSelected()){
                 for (Pregunta pregunta : list_pregunta) {
                if(pregunta.getTextopregunta().equals("¿Es alérgico a la anestesia dental?")){
                opaciente_enfermedad=new Paciente_Pregunta(
                opaciente,
                pregunta,
                jtfantPregunta4.getText().trim(),
                isMujer);
                list_pregunta_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        if(checkpreguntamujer1.isSelected()){
                 for (Pregunta pregunta : list_pregunta) {
                if(pregunta.getTextopregunta().equals("¿Está usted embarazada?")){
                opaciente_enfermedad=new Paciente_Pregunta(
                opaciente,
                pregunta,
                jtfantpreguntamujer1.getText().trim(),
                isMujer);
                list_pregunta_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        if(checkpreguntamujer2.isSelected()){
                 for (Pregunta pregunta : list_pregunta) {
                if(pregunta.getTextopregunta().equals("¿Está dando de lactar?")){
                opaciente_enfermedad=new Paciente_Pregunta(
                opaciente,
                pregunta,
                "",
                isMujer);
                list_pregunta_paciente.add(opaciente_enfermedad);
                }
            }            
        }
        
        return list_pregunta_paciente;
    }
    
     @FXML
    public void seleccionarImagPerfil() throws IOException, IOException{ 
        fileImag = oFileUtilImag.buscarImagen();
        if (fileImag != null) {
            jimagperfil.setImage(new Image(fileImag.getAbsolutePath()));
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
        jtflugarprocedenciaAct.setText(opersona.getLugar_de_procedencia());
        jtfDomicilioAct.setText(opersona.getDomicilio());
        jtfTelefonoAct.setText(opersona.getTelefono());
        jcbocupacionAct.getSelectionModel().select(opersona.getOcupacion());  
    
    }
    
    
    
    @FXML
    void ImprimirPaciente(ActionEvent evt) throws IOException{
        Historiaclinicapdf.ImprimirHistoriaClinica(oPersona);
        File file=new File("pdf\\historia_clinica.pdf");
        Desktop.getDesktop().open(file);
    }
 
    
    
    /*----------Metodos de ventana---------------*/
    @FXML 
    void unlockecdAlergia(ActionEvent event){
        if(jtfantAque.isDisable()){ jtfantAque.setDisable(false);}
        else {                      jtfantAque.setDisable(true); }
    }
     @FXML 
    void unlockecdPregunta1(ActionEvent event){
        if(jtfantPregunta1.isDisable()){ jtfantPregunta1.setDisable(false);}
        else {                      jtfantPregunta1.setDisable(true); }
    }
     @FXML 
    void unlockecdPregunta2(ActionEvent event){
        if(jtfantPregunta2.isDisable()){ jtfantPregunta2.setDisable(false);}
        else {                      jtfantPregunta2.setDisable(true); }
    }
     @FXML 
    void unlockecdPregunta3(ActionEvent event){
        if(jtfantPregunta3.isDisable()){ jtfantPregunta3.setDisable(false);}
        else {                      jtfantPregunta3.setDisable(true); }
    }
     @FXML 
    void unlockecdPregunta4(ActionEvent event){
        if(jtfantPregunta4.isDisable()){ jtfantPregunta4.setDisable(false);}
        else {                      jtfantPregunta4.setDisable(true); }
    }
    
      @FXML 
    void unlockecdPreguntamujer1(ActionEvent event){
        if(jtfantpreguntamujer1.isDisable()){ jtfantpreguntamujer1.setDisable(false);}
        else {                      jtfantpreguntamujer1.setDisable(true); }
    }
    

    
    /*------Fin Metodos de ventana---------------*/
}
