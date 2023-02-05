/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalDate;
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
    
    @JoinColumn(insertable = true,updatable = true,name="idhistoria_clinica",nullable = false)
    @ManyToOne
    private Historia_clinica historia_clinica;
    
    @Column(name = "monto_total", nullable = false)
    private float  monto_total;
    
    @Column(name = "fechapresupuesto", nullable = true)
    private LocalDate fecha_realizada;
    
    @Column(name = "activo", nullable = false)
    private boolean activo;
    
    @Column(name = "flag", nullable = false)
    private boolean flag;

    public Presupuesto() {
    }

    public Presupuesto(Historia_clinica historia_clinica, int monto_total, LocalDate fecha_realizada, boolean activo, boolean flag) {
        this.historia_clinica = historia_clinica;
        this.monto_total = monto_total;
        this.fecha_realizada = fecha_realizada;
        this.activo = activo;
        this.flag = flag;
    }
    
    public int getIdpresupuesto() {
        return idpresupuesto;
    }

    public void setIdpresupuesto(int idpresupuesto) {
        this.idpresupuesto = idpresupuesto;
    }

    public Historia_clinica getHistoria_clinica() {
        return historia_clinica;
    }

    public void setHistoria_clinica(Historia_clinica historia_clinica) {
        this.historia_clinica = historia_clinica;
    }

    public float getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(float monto_total) {
        this.monto_total = monto_total;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Presupuesto getPresupuesto(){
        return this;
    }

    public LocalDate getFecha_realizada() {
        return fecha_realizada;
    }

    public void setFecha_realizada(LocalDate fecha_realizada) {
        this.fecha_realizada = fecha_realizada;
    }
    
       
     
}
