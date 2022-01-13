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
    
     
    @JoinColumn(name = "idhistoriaclinica", nullable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private Historia_clinica historia_clinica;
    
    @Column(name = "caracteristicas", nullable = false)
    private String caracteristicas;

    public Paciente(Persona persona, Historia_clinica historia_clinica) {
        this.persona = persona;
        this.historia_clinica = historia_clinica;
    }
    

    public Paciente(){
    }

  

    
      
      
    
}
