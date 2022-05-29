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
public class Detalle_Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iddetalle_presupuesto;
    
    @JoinColumn(insertable = true,updatable = true,name="idpresupuesto",nullable = false)
    @ManyToOne
    private Presupuesto presupuesto;
    
    @Column(name = "descripcion", nullable = false)
    private String descripcion;
    
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    
    @Column(name = "monto", nullable = false)
    private float monto;

    public Detalle_Presupuesto() {
    }

    public Detalle_Presupuesto(Presupuesto presupuesto, String descripcion, int cantidad, float monto) {
        this.presupuesto = presupuesto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.monto = monto;
    }

    public int getIddetalle_presupuesto() {
        return iddetalle_presupuesto;
    }

    public void setIddetalle_presupuesto(int iddetalle_presupuesto) {
        this.iddetalle_presupuesto = iddetalle_presupuesto;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }
    
    public Detalle_Presupuesto getDetallePresupuesto(){
        return this;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    
    
    
     
}
