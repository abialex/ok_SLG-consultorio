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
public class Carro {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcarro;
     
    @JoinColumn(insertable = true,updatable = true,name="idpersona",nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Persona persona;
     
      @Column(name = "marca", nullable = false)
    private String marca;

    public Carro(Persona persona, String marca) {
        this.persona = persona;
        this.marca = marca;
    }

    public Carro() {
    }

    public int getIdcarro() {
        return idcarro;
    }

    public void setIdcarro(int idcarro) {
        this.idcarro = idcarro;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
    
      
      
    
}
