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
    private Persona persona;
    
    @Column(name = "fechaRealizada", nullable = false)
    private LocalDate fechaRealizada;
    
    @Column(name = "tratamiento", nullable = false)
    private String tratamiento;
    
    @Column(name = "monto", nullable = false)
    private int monto;
    
    @Column(name = "flag", nullable = false)
    private boolean flag;

    public Tratamiento() {
    }

    public Tratamiento(Persona persona, LocalDate fechaRealizada, String tratamiento, int monto) {
        this.persona = persona;
        this.fechaRealizada = fechaRealizada;
        this.tratamiento = tratamiento;
        this.monto = monto;
    }

    

    public int getIdtratamiento() {
        return idtratamiento;
    }

    public void setIdtratamiento(int idtratamiento) {
        this.idtratamiento = idtratamiento;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public LocalDate getFechaRealizada() {
        return fechaRealizada;
    }

    public void setFechaRealizada(LocalDate fechaRealizada) {
        this.fechaRealizada = fechaRealizada;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
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
    
    
    
    
    
    
    
}
