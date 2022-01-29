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
import javax.persistence.OneToOne;

/**
 *
 * @author yalle
 */
@Entity
public class Paciente {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idpaciente;
     
    @JoinColumn( insertable = true,updatable = true,name="idpersona",nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Persona persona;
    
    @Column(name = "sintomasEnfermedadActual" , nullable = true)
    private String sintomasEnfermedadActual;
    
    @Column(name = "tiempoEnfermedadActual" , nullable = true)
    private String tiempoEnfermedadActual;
    
    @Column (name = "otrasEnfermedades", nullable = true)
    private String otrasEnfermedades;
    
    @Column (name = "antecedentesFamiliares", nullable = true)
    private String antecedentesFamiliares;

    public Paciente(Persona persona, String sintomasEnfermedadActual, String tiempoEnfermedadActual, String otrasEnfermedades, String antecedentesFamiliares) {
        this.persona = persona;
        this.sintomasEnfermedadActual = sintomasEnfermedadActual;
        this.tiempoEnfermedadActual = tiempoEnfermedadActual;
        this.otrasEnfermedades = otrasEnfermedades;
        this.antecedentesFamiliares = antecedentesFamiliares;
    }
    
    

    public Paciente(){
    }

    public int getIdpaciente() {
        return idpaciente;
    }

    public void setIdpaciente(int idpaciente) {
        this.idpaciente = idpaciente;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getSintomasEnfermedadActual() {
        return sintomasEnfermedadActual;
    }

    public void setSintomasEnfermedadActual(String sintomasEnfermedadActual) {
        this.sintomasEnfermedadActual = sintomasEnfermedadActual;
    }

    public String getTiempoEnfermedadActual() {
        return tiempoEnfermedadActual;
    }

    public void setTiempoEnfermedadActual(String tiempoEnfermedadActual) {
        this.tiempoEnfermedadActual = tiempoEnfermedadActual;
    }

    public String getOtrasEnfermedades() {
        return otrasEnfermedades;
    }

    public void setOtrasEnfermedades(String otrasEnfermedades) {
        this.otrasEnfermedades = otrasEnfermedades;
    }

    public String getAntecedentesFamiliares() {
        return antecedentesFamiliares;
    }

    public void setAntecedentesFamiliares(String antecedentesFamiliares) {
        this.antecedentesFamiliares = antecedentesFamiliares;
    }
    
    

  

    
      
      
    
}