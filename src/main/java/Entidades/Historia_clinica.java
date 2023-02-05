/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalDate;
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

    @JoinColumn(insertable = true, updatable = true, name = "idpersona", nullable = true)
    @ManyToOne(cascade = CascadeType.ALL)
    private Persona persona;
    //EXPLORACIÓN FÍSICA
    @Column(name = "signosVitales", nullable = true)
    private String signosVitales;

    @Column(name = "saturacionOxigeno", nullable = true)
    private String saturacionOxigeno;

    @Column(name = "PA", nullable = true)
    private String PA;

    @Column(name = "FC", nullable = true)
    private String FC;

    @Column(name = "temperatura", nullable = true)
    private String temperatura;

    @Column(name = "FR", nullable = true)
    private String FR;

    @Column(name = "examenClinicoGeneral", length = 1000, nullable = true)
    private String examenClinicoGeneral;

    @Column(name = "examenClinicoOdontoestomtologico", nullable = true)
    private String examenClinicoOdontoestomtologico;
    //DIAGNÓSTICO
    @Column(name = "diagnostico", length = 1000, nullable = true)
    private String diagnostico;

    //PLAN DE TRATAMIENTO
    @Column(name = "recomendaciones", length = 1000, nullable = true)
    private String recomendaciones;
    //PRONÓSTiCO
    @Column(name = "pronostico", length = 1000, nullable = true)
    private String pronostico;
    //ALTA PACIENTE
    @Column(name = "altaPaciente", length = 1000, nullable = true)
    private String altaPaciente;

    @Column(name = "motivoConsulta", length = 1000, nullable = true)
    private String motivo_consulta;

    @Column(name = "examen_radiografico", length = 1000, nullable = true)
    private String examen_radiografico;

    @Column(name = "fechainscripcion", nullable = true)
    private LocalDate fechainscripcion;

    @Column(name = "fechaultimaatencion", nullable = true)
    private LocalDate fechaultimaatencion;

    @Column(name = "examenIntraoral", nullable = true)
    private String examen_intraoral;

    @Column(name = "enfermedadActual", nullable = true)
    private String enfermedadActual;

    @Column(name = "antecedentesFamiliares", nullable = true)
    private String antecedentes;

    public Historia_clinica(Doctor doctor, Persona persona, String motivoConsulta, String enfermedad_actual, String antecedentes, String examen_intraoral, String examen_radiografico, String diagnistico,
                            LocalDate fechainscripcion, LocalDate fechaultimaatencion) {
        this.doctor = doctor;
        this.persona = persona;
        this.motivo_consulta = motivoConsulta;
        this.enfermedadActual = enfermedad_actual;
        this.antecedentes = antecedentes;
        this.examen_intraoral = examen_intraoral;
        this.examen_radiografico = examen_radiografico;
        this.diagnostico = diagnistico;
        this.fechainscripcion = fechainscripcion;
        this.fechaultimaatencion = fechaultimaatencion;
    }
    
    @JoinColumn(insertable = true, updatable = true, name = "iddoctor", nullable = true)
    @ManyToOne
    private Doctor doctor;

    public Historia_clinica() {
    }

    public int getIdhistoria_clinica() {
        return idhistoria_clinica;
    }

    public void setIdhistoria_clinica(int idhistoria_clinica) {
        this.idhistoria_clinica = idhistoria_clinica;
    }

    public LocalDate getFechainscripcion() {
        return fechainscripcion;
    }

    public void setFechainscripcion(LocalDate fechainscripcion) {
        this.fechainscripcion = fechainscripcion;
    }

    public LocalDate getFechaultimaatencion() {
        return fechaultimaatencion;
    }

    public void setFechaultimaatencion(LocalDate fechaultimaatencion) {
        this.fechaultimaatencion = fechaultimaatencion;
    }

    public String getMotivo_consulta() {
        return motivo_consulta;
    }

    public void setMotivo_consulta(String motivoConsulta) {
        this.motivo_consulta = motivoConsulta;
    }

    public String getSignosVitales() {
        return signosVitales;
    }

    public void setSignosVitales(String signosVitales) {
        this.signosVitales = signosVitales;
    }

    public String getSaturacionOxigeno() {
        return saturacionOxigeno;
    }

    public void setSaturacionOxigeno(String saturacionOxigeno) {
        this.saturacionOxigeno = saturacionOxigeno;
    }

    public String getPA() {
        return PA;
    }

    public void setPA(String PA) {
        this.PA = PA;
    }

    public String getFC() {
        return FC;
    }

    public void setFC(String FC) {
        this.FC = FC;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getFR() {
        return FR;
    }

    public void setFR(String FR) {
        this.FR = FR;
    }

    public String getExamenClinicoGeneral() {
        return examenClinicoGeneral;
    }

    public void setExamenClinicoGeneral(String examenClinicoGeneral) {
        this.examenClinicoGeneral = examenClinicoGeneral;
    }

    public String getExamenClinicoOdontoestomtologico() {
        return examenClinicoOdontoestomtologico;
    }

    public void setExamenClinicoOdontoestomtologico(String examenClinicoOdontoestomtologico) {
        this.examenClinicoOdontoestomtologico = examenClinicoOdontoestomtologico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public String getPronostico() {
        return pronostico;
    }

    public void setPronostico(String pronostico) {
        this.pronostico = pronostico;
    }

    public String getAltaPaciente() {
        return altaPaciente;
    }

    public void setAltaPaciente(String altaPaciente) {
        this.altaPaciente = altaPaciente;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getExamen_radiografico() {
        return examen_radiografico;
    }

    public void setExamen_radiografico(String examen_radiografico) {
        this.examen_radiografico = examen_radiografico;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getExamen_intraoral() {
        return examen_intraoral;
    }

    public void setExamen_intraoral(String examenIntraoral) {
        this.examen_intraoral = examenIntraoral;
    }

    public String getEnfermedadActual() {
        return enfermedadActual;
    }

    public void setEnfermedadActual(String enfermedadActual) {
        this.enfermedadActual = enfermedadActual;
    }

    public String getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(String antecedentesFamiliares) {
        this.antecedentes = antecedentesFamiliares;
    }
    
    public Historia_clinica getHistoria_clinica(){
        return this;
    }
            
}
