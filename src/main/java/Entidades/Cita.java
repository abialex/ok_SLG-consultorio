/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author alexis
 */
@Entity
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcita;

    @JoinColumn(insertable = true, updatable = true, name = "iddoctor", nullable = false)
    @ManyToOne
    private Doctor doctor;
    
    @JoinColumn(insertable = true, updatable = true, name = "idpersona", nullable = true)
    @ManyToOne
    private Persona persona;
    
    @JoinColumn(insertable = true, updatable = true, name = "idhoraatencion", nullable = false)
    @ManyToOne
    private HoraAtencion horaatencion;
    
    @Column(name = "minuto" , nullable = true)
    private String minuto;
    
    @Column(name = "fechacita" , nullable = true)
    private LocalDate fechacita;
    
    @Column(name = "razon" , nullable = true)
    private String razon;

    public Cita() {
    }

    public Cita(Doctor doctor, HoraAtencion horaatencion, LocalDate fechacita, String razon, String minuto) {
        this.doctor = doctor;
        //this.paciente = paciente;
        this.horaatencion = horaatencion;
        this.fechacita = fechacita;
        this.razon = razon;
        this.minuto = minuto;
    }
    
    public Cita(Doctor doctor, HoraAtencion horaatencion, LocalDate fechacita, String razon) {
        this.doctor = doctor;
        this.horaatencion = horaatencion;
        this.fechacita = fechacita;
        this.razon = razon;
    }

    public int getIdcita() {
        return idcita;
    }

    public void setIdcita(int idcita) {
        this.idcita = idcita;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public HoraAtencion getHoraatencion() {
        return horaatencion;
    }

    public void setHoraatencion(HoraAtencion horaatencion) {
        this.horaatencion = horaatencion;
    }

    public LocalDate getFechacita() {
        return fechacita;
    }

    public void setFechacita(LocalDate fechacita) {
        this.fechacita = fechacita;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getMinuto() {
        return minuto;
    }

    public void setMinuto(String minuto) {
        this.minuto = minuto;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    
    
         
}
