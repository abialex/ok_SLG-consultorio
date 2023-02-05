package Entidades;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author yalle
 */
@Entity
public class Tratamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idtratamiento;
    
    @JoinColumn(insertable = true,updatable = true,name="idpersona",nullable = false)
    @ManyToOne
    private Historia_clinica historia_clinica;

    @JoinColumn(insertable = true,updatable = true,name="iddoctor",nullable = false)
    @ManyToOne
    private Doctor doctor;
    
    @Column(name = "fechaRealizada", nullable = false)
    private LocalDate fecha_realizada;
    
    @Column(name = "tratamiento", nullable = false)
    private String nombre;
    
    @Column(name = "monto", nullable = false)
    private int monto;
    
    @Column(name = "flag", nullable = false)
    private boolean flag;

    public Tratamiento() {
    }

    public Tratamiento(Historia_clinica ohistoria_clinica, LocalDate fechaRealizada, String tratamiento, int monto) {
        this.historia_clinica = ohistoria_clinica;
        this.fecha_realizada = fechaRealizada;
        this.nombre = tratamiento;
        this.monto = monto;
    }

    

    public int getIdtratamiento() {
        return idtratamiento;
    }

    public void setIdtratamiento(int idtratamiento) {
        this.idtratamiento = idtratamiento;
    }

    public Historia_clinica getHistoria_clinica() {
        return historia_clinica;
    }

    public void setHistoria_clinica(Historia_clinica historia_clinica) {
        this.historia_clinica = historia_clinica;
    }

    public LocalDate getFecha_realizada() {
        return fecha_realizada;
    }

    public void setFecha_realizada(LocalDate fecha_realizada) {
        this.fecha_realizada = fecha_realizada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }
    public Doctor getDoctor() {
        return doctor;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    
    
    
    
    
    
    
}
