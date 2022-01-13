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
public class Historia_clinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idhistoria_clinica;

    @JoinColumn(insertable = true,updatable = true,name="idpaciente",nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Paciente idpaciente;
    
    
    @Column(name = "cirugia_externa", nullable = false)
    private String cirugia_externa;

    
    
}
