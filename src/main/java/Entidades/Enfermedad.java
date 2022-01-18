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

/**
 *
 * @author yalle
 */
@Entity
public class Enfermedad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idenfermedad;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "iscontagiosa", nullable = false)
    private boolean iscontagiosa;

    public Enfermedad() {
    }
    
    public Enfermedad(String nombre, boolean iscontagiosa) {
        this.nombre = nombre;
        this.iscontagiosa = iscontagiosa;
    }

    public int getIdenfermedad() {
        return idenfermedad;
    }

    public void setIdenfermedad(int idenfermedad) {
        this.idenfermedad = idenfermedad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isContagiosa() {
        return iscontagiosa;
    }

    public void setIscontagiosa(boolean iscontagiosa) {
        this.iscontagiosa = iscontagiosa;
    }
    
    
}
