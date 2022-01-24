/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author yalle
 */
@Entity
public class Historia_clinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idhistoria_clinica;

    @JoinColumn(insertable = true,updatable = true,name="idpaciente",nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Paciente idpaciente;
    
    @Column (name = "fechainscripcion", nullable = true)
    private Date fechainscripcion;
    
    @Column (name = "fechaultimaatencion", nullable = true)
    private Date fechaultimaatencion;

    public Historia_clinica(Paciente idpaciente, Date fechainscripcion, Date fechaultimaatencion) {
        this.idpaciente = idpaciente;
        this.fechainscripcion = fechainscripcion;
        this.fechaultimaatencion = fechaultimaatencion;
    }
    
    public Historia_clinica() {
    }

    public int getIdhistoria_clinica() {
        return idhistoria_clinica;
    }

    public void setIdhistoria_clinica(int idhistoria_clinica) {
        this.idhistoria_clinica = idhistoria_clinica;
    }

    public Paciente getIdpaciente() {
        return idpaciente;
    }

    public void setIdpaciente(Paciente idpaciente) {
        this.idpaciente = idpaciente;
    }

    public Date getFechainscripcion() {
        return fechainscripcion;
    }

    public void setFechainscripcion(Date fechainscripcion) {
        this.fechainscripcion = fechainscripcion;
    }

    public Date getFechaultimaatencion() {
        return fechaultimaatencion;
    }

    public void setFechaultimaatencion(Date fechaultimaatencion) {
        this.fechaultimaatencion = fechaultimaatencion;
    }
    
 
}
