/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import controller.App;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author yalle
 */
@Entity

public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idpersona;

    @Column(name = "nombres_apellidos", nullable = false)
    private String nombres;

    @Column(name = "ap_paterno", nullable = true)
    private String ap_paterno;

    @Column(name = "ap_materno", nullable = true)
    private String ap_materno;

    @Column(name = "sexo", length = 15, nullable = false)
    private String sexo;

    @Column(name = "domicilio", length = 60, nullable = false)
    private String domicilio;

    @Column(name = "dni", length = 8, nullable = false)
    private String dni;

    @Column(name = "fechaNacimiento", nullable = false)
    private LocalDate fecha_cumple;

    @Column(name = "lugar_de_procedencia", length = 100, nullable = false)
    private String lugar_de_procedencia;

    @Column(name = "ocupacion", nullable = false)
    private String ocupacion;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    //@Column(name = "tutorNombre", nullable = true)
    //private String tutorNombre;

    //@Column(name = "tutorDni", nullable = true)
    //private String tutorDni;

    //@Column(name = "tutorTelefono", nullable = true)
    //private String tutorTelefono;

    @Column(name = "", nullable = false)
    private boolean flag;

    public Persona() {
    }

    public Persona(String nombres_apellidos, String ap_paterno, String ap_materno, String sexo, String domicilio, String dni, LocalDate fecha_nacimiento, String lugar_de_procedencia, String ocupacion, String telefono, float presupuesto) {
        this.nombres = nombres_apellidos;
        this.ap_paterno = ap_paterno;
        this.ap_materno = ap_materno;
        this.sexo = sexo;
        this.domicilio = domicilio;
        this.dni = dni;
        this.fecha_cumple = fecha_nacimiento;
        this.lugar_de_procedencia = lugar_de_procedencia;
        this.ocupacion = ocupacion;
        this.telefono = telefono;

    }

    public Persona getPersona() {
        return this;
    }

    public int getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(int idpersona) {
        this.idpersona = idpersona;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres_apellidos) {
        this.nombres = nombres_apellidos;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFecha_cumple() {
        return fecha_cumple;
    }

    public void setFecha_cumple(LocalDate fechaNacimiento) {
        this.fecha_cumple = fechaNacimiento;
    }

    public String getLugar_de_procedencia() {
        return lugar_de_procedencia;
    }

    public void setLugar_de_procedencia(String lugar_de_procedencia) {
        this.lugar_de_procedencia = lugar_de_procedencia;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getAp_paterno() {
        return ap_paterno;
    }

    public void setAp_paterno(String ap_paterno) {
        this.ap_paterno = ap_paterno;
    }

    public String getAp_materno() {
        return ap_materno;
    }

    public void setAp_materno(String ap_materno) {
        this.ap_materno = ap_materno;
    }

    @Override
    public String toString() {
        return this.nombres;
    }
    /*
    public Paciente getPaciente() {
        List<Paciente> listpaciente = App.jpa.createQuery("select p from Paciente p where idpersona=" + idpersona).getResultList();
        return listpaciente.get(0);
    }*/
    
      public Historia_clinica getHistoriaClinica(){
        List<Historia_clinica> listhc=App.jpa.createQuery("select p from Historia_clinica p where idpersona="+ idpersona).getResultList();
        return listhc.get(0);
    }
}
