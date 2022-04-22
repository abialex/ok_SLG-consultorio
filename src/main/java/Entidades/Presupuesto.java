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
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idpresupuesto;
    
    @JoinColumn(insertable = true,updatable = true,name="idpersona",nullable = false)
    @ManyToOne
    private Persona persona;
    
    @Column(name = "descripcion", nullable = false)
    private String descripcion;
    
    @Column(name = "monto", nullable = false)
    private float monto;

    public Presupuesto() {
    }

    public Presupuesto(Persona persona, String descripcion, float monto) {
        this.persona = persona;
        this.descripcion = descripcion;
        this.monto = monto;
    }
    
    public int getIdpresupuesto() {
        return idpresupuesto;
    }

    public void setIdpresupuesto(int idpresupuesto) {
        this.idpresupuesto = idpresupuesto;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }
    
    public Presupuesto getPresupuesto(){
        return this;
    }
    
    
    
    
    
}
