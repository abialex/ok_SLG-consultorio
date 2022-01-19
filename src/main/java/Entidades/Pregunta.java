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
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idpregunta;
    
    @Column(name = "textopregunta", nullable = false)
    private String textopregunta;

    public Pregunta(int idpregunta, String textopregunta) {
        this.idpregunta = idpregunta;
        this.textopregunta = textopregunta;
    }

    public Pregunta() {
    }

    public int getIdpregunta() {
        return idpregunta;
    }

    public void setIdpregunta(int idpregunta) {
        this.idpregunta = idpregunta;
    }

    public String getTextopregunta() {
        return textopregunta;
    }

    public void setTextopregunta(String textopregunta) {
        this.textopregunta = textopregunta;
    }
    
    
    
    
}
