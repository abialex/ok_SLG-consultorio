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
public class Paciente_Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idpaciente_pregunta;
    
    @JoinColumn(insertable = true,updatable = true,name="idpaciente",nullable = false)
    @ManyToOne
    private Paciente paciente;
    
    @JoinColumn(insertable = true,updatable = true,name="idpregunta",nullable = false)
    @ManyToOne
    private Pregunta pregunta;
    
    @Column (name = "especificaciones", nullable = true)
    private String especificaciones;
    
    @Column (name = "isMujer", nullable = true)
    private boolean isMujer;
    
    public Paciente_Pregunta(Paciente paciente, Pregunta pregunta, String especificaciones,boolean isMujer) {
        this.paciente = paciente;
        this.pregunta = pregunta;
        this.especificaciones = especificaciones;
        this.isMujer=isMujer;
    }

    public Paciente_Pregunta() {
    }

    public int getIdpaciente_pregunta() {
        return idpaciente_pregunta;
    }

    public void setIdpaciente_pregunta(int idpaciente_pregunta) {
        this.idpaciente_pregunta = idpaciente_pregunta;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }

    public boolean IsMujer() {
        return isMujer;
    }

    public void setIsMujer(boolean isMujer) {
        this.isMujer = isMujer;
    }
    
    
  
}
