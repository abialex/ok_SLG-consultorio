/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

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
 * @author yalle
 */
@Entity
public class Persona_Enfermedad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idpaciente_enfermedad;
    
    @JoinColumn(insertable = true,updatable = true,name="idpersona",nullable = false)
    @ManyToOne
    private Persona persona;
    
    @JoinColumn(insertable = true,updatable = true,name="idenfermedad",nullable = false)
    @ManyToOne
    private Enfermedad enfermedad;
    
    @Column(name = "detalleEnfermedad", nullable = false)
    private String detalleEnfermedad;

    public Persona_Enfermedad() {
    }

    public Persona_Enfermedad(Persona persona, Enfermedad enfermedad, String detalleEnfermedad) {
        this.persona = persona;
        this.enfermedad = enfermedad;
        this.detalleEnfermedad = detalleEnfermedad;
    }

    public int getIdpaciente_enfermedad() {
        return idpaciente_enfermedad;
    }

    public void setIdpaciente_enfermedad(int idpaciente_enfermedad) {
        this.idpaciente_enfermedad = idpaciente_enfermedad;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(Enfermedad enfermedad) {
        this.enfermedad = enfermedad;
    }

    public String getDetalleEnfermedad() {
        return detalleEnfermedad;
    }

    public void setDetalleEnfermedad(String detalleEnfermedad) {
        this.detalleEnfermedad = detalleEnfermedad;
    }
       
}
