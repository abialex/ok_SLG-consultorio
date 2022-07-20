/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

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
public class ExamenAuxiliar {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idexamenauxiliar;

    @JoinColumn(insertable = true, updatable = true, name = "idhistoria_clinica", nullable = false)
    @ManyToOne
    private Historia_clinica historia_clinica;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    public ExamenAuxiliar() {
    }

    public ExamenAuxiliar(Historia_clinica historia_clinica, String descripcion) {
        this.historia_clinica = historia_clinica;
        this.descripcion = descripcion;
    }

    public int getIdexamenauxiliar() {
        return idexamenauxiliar;
    }

    public void setIdexamenauxiliar(int idexamenauxiliar) {
        this.idexamenauxiliar = idexamenauxiliar;
    } 

    public Historia_clinica getHistoria_clinica() {
        return historia_clinica;
    }

    public void setHistoria_clinica(Historia_clinica historia_clinica) {
        this.historia_clinica = historia_clinica;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    public ExamenAuxiliar getPlanTratamiento() {
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
