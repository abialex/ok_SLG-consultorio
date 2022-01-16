/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.util.Date;
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

public class Persona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idpersona;
        
    @Column(name = "nombres_apellidos", nullable = false)
    private String nombres_apellidos;
    
    @Column(name = "sexo",length = 15, nullable = false)
    private String sexo;
    
    @Column(name = "edad", nullable = false)
    private int edad;
    
    @Column(name = "domicilio",length = 60, nullable = false)
    private String domicilio;
    
    @Column(name = "dni",length = 8, nullable = false)
    private String dni;
    
    @Column(name = "fecha_nacimiento", nullable = false)
    private Date fecha_nacimiento;
    
    @Column(name = "lugar_de_procedencia",length = 100, nullable = false)
    private String lugar_de_procedencia;

    @Column(name = "ocupacion", nullable = false)
    private String ocupacion;
    
    @Column(name = "telefono", nullable = false)
    private String telefono;
    
    @Column(name = "correo",length = 40, nullable = true)
    private String correo;

    public Persona() {
    }

    public Persona(String nombres_apellidos, String sexo, int edad, String domicilio, String dni, Date fecha_nacimiento, String lugar_de_procedencia, String ocupacion, String telefono, String correo) {
        this.nombres_apellidos = nombres_apellidos;
        this.sexo = sexo;
        this.edad = edad;
        this.domicilio = domicilio;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.lugar_de_procedencia = lugar_de_procedencia;
        this.ocupacion = ocupacion;
        this.telefono = telefono;
        this.correo = correo;
    }
    
    public int getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(int idpersona) {
        this.idpersona = idpersona;
    }

    public String getNombres_apellidos() {
        return nombres_apellidos;
    }

    public void setNombres_apellidos(String nombres_apellidos) {
        this.nombres_apellidos = nombres_apellidos;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getLugar_de_procedencia() {
        return lugar_de_procedencia;
    }

    public void setLugar_de_procedencia(String lugar_de_procedencia) {
        this.lugar_de_procedencia = lugar_de_procedencia;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
      
}
