/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pdf;

import Entidades.Historia_clinica;
import Entidades.Paciente;
import Entidades.Paciente_Enfermedad;
import Entidades.Paciente_Pregunta;
import Entidades.Persona;
import Entidades.Pregunta;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import Pdf.style.style1;
import com.itextpdf.layout.border.SolidBorder;
import controller.App;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Font;

/**
 *
 * @author yalle
 */
public class Historiaclinicapdf {

    public static void ImprimirHistoriaClinica(Persona opersona) throws IOException {
        SimpleDateFormat oSDF = new SimpleDateFormat("yyyy / MM / dd");
        Paciente opaciente = (Paciente) App.jpa.createQuery("select p from Paciente p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        Historia_clinica oHistoriaclinica = (Historia_clinica) App.jpa.createQuery("select p from Historia_clinica p where idpaciente=" + opaciente.getIdpaciente()).getSingleResult();
        List<Paciente_Enfermedad> listPaciente_Enfermedad = App.jpa.createQuery("select p from Paciente_Enfermedad p where idpaciente=" + opaciente.getIdpaciente()).getResultList();
        List<Paciente_Pregunta> listPaciente_PreguntaIsMujer = App.jpa.createQuery("select p from Paciente_Pregunta p where idpaciente=" + opaciente.getIdpaciente() + " and ismujer=true").getResultList();
        List<Paciente_Pregunta> listPaciente_PreguntaIsHombre = App.jpa.createQuery("select p from Paciente_Pregunta p where idpaciente=" + opaciente.getIdpaciente() + " and ismujer=false").getResultList();
        List<Pregunta> listPreguntaIsMujer = App.jpa.createQuery("select p from Pregunta p where isMujer=true ORDER BY idpregunta ASC").getResultList();
        List<Pregunta> listPreguntaIsHombre = App.jpa.createQuery("select p from Pregunta p where isMujer=false  ORDER BY idpregunta ASC").getResultList();
        int volumen = 105;
        PdfWriter writer = null;
        try {
            writer = new PdfWriter("Pdf\\historia_clinica.pdf");
        } catch (FileNotFoundException e) {

        }
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(36, 36, 36, 36);

        style1 evento = new style1(document);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, evento);

        /*--------styles-------------*/
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        Style styleCell = new Style().setBorder(Border.NO_BORDER);
        Style styleTextRight = new Style().setTextAlignment(TextAlignment.RIGHT).setFontSize(10f);
        Style styleTextLeft = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(10f);
        Style styleTextCenter = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(10f);
        /* Contenido del documento  página 1*/
        //Cabecera
        Table CabeceraParrafo1 = new Table(new float[]{volumen * 4.3f, volumen * 0.7f});
        Cell cellCabeceraParrafo1 = new Cell().add(new Paragraph("H.C N°: ")).addStyle(styleTextRight).addStyle(styleCell);
        CabeceraParrafo1.addCell(cellCabeceraParrafo1);
        cellCabeceraParrafo1 = new Cell().add(new Paragraph("" + oHistoriaclinica.getIdhistoria_clinica()).setBorderBottom(new SolidBorder(1f))).addStyle(styleTextCenter).addStyle(styleCell);
        CabeceraParrafo1.addCell(cellCabeceraParrafo1);

        Table CabeceraParrafo2 = new Table(new float[]{volumen * 4.3f, volumen * 0.7f});
        Cell cellCabeceraParrafo2 = new Cell().add(new Paragraph("FECHA: ")).addStyle(styleTextRight).addStyle(styleCell);
        CabeceraParrafo2.addCell(cellCabeceraParrafo2);
        cellCabeceraParrafo2 = new Cell().add(new Paragraph(oSDF.format(oHistoriaclinica.getFechainscripcion())).setBorderBottom(new SolidBorder(1f))).addStyle(styleTextCenter).addStyle(styleCell);
        CabeceraParrafo2.addCell(cellCabeceraParrafo2);

        Table Cabecera = new Table(new float[]{volumen * 5f});
        Cabecera.addCell(new Cell().add(CabeceraParrafo1).addStyle(styleCell));
        Cabecera.addCell(new Cell().add(CabeceraParrafo2).addStyle(styleCell));
        //Fin Cabecera
        // ANAMNESIS
        Paragraph parrafoTitulo = new Paragraph("HISTORIA CLÍNICA").setFontSize(14).setFont(bold).setTextAlignment(TextAlignment.CENTER);
        Paragraph parrafoSubTitulo1 = new Paragraph("I.  ANAMNESIS").setFontSize(10).setFontColor(Color.BLUE).setFont(bold).addStyle(styleTextLeft);

        Table table1Parrafo1 = new Table(new float[]{volumen * 0.95f, volumen * 2.55f, volumen * 0.3f, volumen * 0.6f, volumen * 0.3f, volumen * 0.3f});
        Cell cell1Parrafo1 = new Cell().add(new Paragraph("Nombres y Apellidos: ")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo1.addCell(cell1Parrafo1);
        cell1Parrafo1 = new Cell().add(new Paragraph(opersona.getNombres_apellidos()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo1.addCell(cell1Parrafo1);
        cell1Parrafo1 = new Cell().add(new Paragraph("Sexo: ")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo1.addCell(cell1Parrafo1);
        cell1Parrafo1 = new Cell().add(new Paragraph(opersona.getSexo()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo1.addCell(cell1Parrafo1);
        cell1Parrafo1 = new Cell().add(new Paragraph("Edad: ")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo1.addCell(cell1Parrafo1);
        cell1Parrafo1 = new Cell().add(new Paragraph(opersona.getEdad() + "").setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo1.addCell(cell1Parrafo1);

        Table table1Parrafo2 = new Table(new float[]{volumen * 0.5f, volumen * 3.5f, volumen * 0.25f, volumen * 0.75f});
        Cell cell1Parrafo2 = new Cell().add(new Paragraph("Domicilio: ")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo2.addCell(cell1Parrafo2);
        cell1Parrafo2 = new Cell().add(new Paragraph(opersona.getDomicilio()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo2.addCell(cell1Parrafo2);
        cell1Parrafo2 = new Cell().add(new Paragraph("DNI:")).addStyle(styleTextLeft).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo2.addCell(cell1Parrafo2);
        cell1Parrafo2 = new Cell().add(new Paragraph(opersona.getDni()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo2.addCell(cell1Parrafo2);

        Table table1Parrafo3 = new Table(new float[]{volumen * 1f, volumen * 1.4f, volumen * 1f, volumen * 1.6f});
        Cell cell1Parrafo3 = new Cell().add(new Paragraph("Fecha de Nacimiento:")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo3.addCell(cell1Parrafo3);
        cell1Parrafo3 = new Cell().add(new Paragraph(oSDF.format(opersona.getFecha_nacimiento()))
                .setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo3.addCell(cell1Parrafo3);
        cell1Parrafo3 = new Cell().add(new Paragraph("Lugar de Procedencia:")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo3.addCell(cell1Parrafo3);
        cell1Parrafo3 = new Cell().add(new Paragraph(opersona.getLugar_de_procedencia()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo3.addCell(cell1Parrafo3);

        Table table1Parrafo4 = new Table(new float[]{volumen * 0.53f, volumen * 1.97f, volumen * 0.44f, volumen * 2.06f});
        Cell cell1Parrafo4 = new Cell().add(new Paragraph("Ocupación:")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo4.addCell(cell1Parrafo4);
        cell1Parrafo4 = new Cell().add(new Paragraph(opersona.getOcupacion()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo4.addCell(cell1Parrafo4);
        cell1Parrafo4 = new Cell().add(new Paragraph("Teléfono:")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo4.addCell(cell1Parrafo4);
        cell1Parrafo4 = new Cell().add(new Paragraph(opersona.getTelefono()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo4.addCell(cell1Parrafo4);

        /* Image img = null;
        try {
            img = new Image(ImageDataFactory.create("images\\imagenDiente.png"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(EventoPagina.class.getName()).log(Level.SEVERE, null, ex);
        }
        Cell cellimag=new Cell().add(img.setAutoScale(true)).setBorder(Border.NO_BORDER); /* new SolidBorder(Color.BLACK,1*/
        Table tableInformacion = new Table(new float[]{volumen * 5});
        Cell cellTable1 = new Cell().add(table1Parrafo1).addStyle(styleCell);
        tableInformacion.addCell(cellTable1);
        cellTable1 = new Cell().add(table1Parrafo2).addStyle(styleCell);
        tableInformacion.addCell(cellTable1);
        cellTable1 = new Cell().add(table1Parrafo3).addStyle(styleCell);
        tableInformacion.addCell(cellTable1);
        cellTable1 = new Cell().add(table1Parrafo4).addStyle(styleCell);
        tableInformacion.addCell(cellTable1);

        //table1Datos.addCell(cellimag);
        Table TableMenorDeEdadParrafo1 = new Table(new float[]{volumen * 5});
        Cell cellTableMenorDeEdadParrafo1 = new Cell().add(new Paragraph("Para pacientes menores de edad:").setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo1.addCell(cellTableMenorDeEdadParrafo1);

        Table TableMenorDeEdadParrafo2 = new Table(new float[]{volumen * 4, volumen * 0.5f, volumen * 0.5f});
        Cell cellTableMenorDeEdadParrafo2 = new Cell().add(new Paragraph("Nombre del Padre:")).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo2.addCell(cellTableMenorDeEdadParrafo2);
        cellTableMenorDeEdadParrafo2 = new Cell().add(new Paragraph("DNI:")).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo2.addCell(cellTableMenorDeEdadParrafo2);
        cellTableMenorDeEdadParrafo2 = new Cell().add(new Paragraph("Telf.:")).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo2.addCell(cellTableMenorDeEdadParrafo2);

        Table TableMenorDeEdadParrafo3 = new Table(new float[]{volumen * 4, volumen * 0.5f, volumen * 0.5f});
        Cell cellTableMenorDeEdadParrafo3 = new Cell().add(new Paragraph("Nombre de la Madre:")).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo3.addCell(cellTableMenorDeEdadParrafo3);
        cellTableMenorDeEdadParrafo3 = new Cell().add(new Paragraph("DNI:")).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo3.addCell(cellTableMenorDeEdadParrafo3);
        cellTableMenorDeEdadParrafo3 = new Cell().add(new Paragraph("Telf.:")).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo3.addCell(cellTableMenorDeEdadParrafo3);

        Table TableMenorDeEdad = new Table(new float[]{volumen * 5});
        Cell cellTableMenorDeEdad = new Cell().add(TableMenorDeEdadParrafo1).addStyle(styleCell);
        TableMenorDeEdad.addCell(cellTableMenorDeEdad);
        cellTableMenorDeEdad = new Cell().add(TableMenorDeEdadParrafo2).addStyle(styleCell);
        TableMenorDeEdad.addCell(cellTableMenorDeEdad);
        cellTableMenorDeEdad = new Cell().add(TableMenorDeEdadParrafo3).addStyle(styleCell);
        TableMenorDeEdad.addCell(cellTableMenorDeEdad);

        Table TableCasoDeEmergenciaParrafo1 = new Table(new float[]{volumen * 5});
        Cell cellTableCasoDeEmergenciaParrafo1 = new Cell().add(new Paragraph("En caso de emergencia comunicarse con:").setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft);
        TableCasoDeEmergenciaParrafo1.addCell(cellTableCasoDeEmergenciaParrafo1);

        Table TableCasoDeEmergenciaParrafo2 = new Table(new float[]{volumen * 3, volumen * 1.5f, volumen * 0.5f});
        Cell cellTableCasoDeEmergenciaParrafo2 = new Cell().add(new Paragraph("Nombre:")).addStyle(styleCell).addStyle(styleTextLeft);
        TableCasoDeEmergenciaParrafo2.addCell(cellTableCasoDeEmergenciaParrafo2);
        cellTableCasoDeEmergenciaParrafo2 = new Cell().add(new Paragraph("Parentesco:")).addStyle(styleCell).addStyle(styleTextLeft);
        TableCasoDeEmergenciaParrafo2.addCell(cellTableCasoDeEmergenciaParrafo2);
        cellTableCasoDeEmergenciaParrafo2 = new Cell().add(new Paragraph("Telf.:")).addStyle(styleCell).addStyle(styleTextLeft);
        TableCasoDeEmergenciaParrafo2.addCell(cellTableCasoDeEmergenciaParrafo2);

        Table TableCasoDeEmergencia = new Table(new float[]{volumen * 5});
        Cell cellTableCasoDeEmergencia = new Cell().add(TableCasoDeEmergenciaParrafo1).addStyle(styleCell);
        TableCasoDeEmergencia.addCell(cellTableCasoDeEmergencia);
        cellTableCasoDeEmergencia = new Cell().add(TableCasoDeEmergenciaParrafo2).addStyle(styleCell);
        TableCasoDeEmergencia.addCell(cellTableCasoDeEmergencia);

        Table TableMotivoDeConsultaParrafo1 = new Table(new float[]{volumen * 1, volumen * 4});
        Cell cellTableMotivoDeConsultaParrafo1 = new Cell().add(new Paragraph("Motivo de Consulta: ").setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft);
        TableMotivoDeConsultaParrafo1.addCell(cellTableMotivoDeConsultaParrafo1);
        cellTableMotivoDeConsultaParrafo1 = new Cell().add(new Paragraph(oHistoriaclinica.getMotivoConsulta()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableMotivoDeConsultaParrafo1.addCell(cellTableMotivoDeConsultaParrafo1);

        Table TableMotivoDeConsulta = new Table(new float[]{volumen * 5});
        Cell cellTableMotivoDeConsulta = new Cell().add(TableMotivoDeConsultaParrafo1).addStyle(styleCell);
        TableMotivoDeConsulta.addCell(cellTableMotivoDeConsulta);
        //Fin ANAMNESIS

        //ENFERMEDAD ACTUAL
        Paragraph parrafoSubTitulo2 = new Paragraph("II.  ENFERMEDAD ACTUAL").setFontSize(10).setFontColor(Color.BLUE).setFont(bold).addStyle(styleTextLeft);
        Table TableEnfermedadActual = new Table(new float[]{volumen * 1.3f, volumen * 3.7f});
        Cell cellTableEnfermedadActual = new Cell().add(new Paragraph("Signo y síntomas principales:")).addStyle(styleCell).addStyle(styleTextLeft);
        TableEnfermedadActual.addCell(cellTableEnfermedadActual);
        cellTableEnfermedadActual = new Cell().add(new Paragraph(opaciente.getSintomasEnfermedadActual()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableEnfermedadActual.addCell(cellTableEnfermedadActual);
        cellTableEnfermedadActual = new Cell().add(new Paragraph("Tiempo de la enfermedad:")).addStyle(styleCell).addStyle(styleTextLeft);
        TableEnfermedadActual.addCell(cellTableEnfermedadActual);
        cellTableEnfermedadActual = new Cell().add(new Paragraph(opaciente.getTiempoEnfermedadActual()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableEnfermedadActual.addCell(cellTableEnfermedadActual);
        //Fin ENFERMEDAD ACTUAL

        //ANTECEDENTES
        //Ha presentado o presenta algunos de los siguientes síntomas
        Paragraph parrafoSubTitulo3 = new Paragraph("III.  ANTECEDENTES").setFontSize(10).setFontColor(Color.BLUE).setFont(bold).addStyle(styleTextLeft);
        Table TableAntecedentes = new Table(new float[]{volumen * 5});

        Table TableAntecedentesEnfermedades = new Table(new float[]{volumen * 1.25f, volumen * 1.25f, volumen * 1.25f, volumen * 1.25f});
        int cont = 0;
        for (Paciente_Enfermedad paciente_Enfermedad : listPaciente_Enfermedad) {
            String detalle = "";
            if (!paciente_Enfermedad.getDetalleEnfermedad().isEmpty()) {
                detalle = " : " + paciente_Enfermedad.getDetalleEnfermedad();
            }
            cont++;
            TableAntecedentesEnfermedades.addCell(new Cell().add(new Paragraph(cont + ": " + paciente_Enfermedad.getEnfermedad().getNombre() + detalle)).addStyle(styleCell).addStyle(styleTextLeft));

        }

        //Otras enfermedades
        Table TableOtrasEnfermedades = new Table(new float[]{volumen * 1f, volumen * 4f});
        Cell cellTableOtrasEnfermedades = new Cell().add(new Paragraph("Otras enfermedades: ")).addStyle(styleCell).addStyle(styleTextLeft);
        TableOtrasEnfermedades.addCell(cellTableOtrasEnfermedades);
        cellTableOtrasEnfermedades = new Cell().add(new Paragraph(opaciente.getOtrasEnfermedades()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableOtrasEnfermedades.addCell(cellTableOtrasEnfermedades);

        int contP = 0;
        Table TableAntecedentesPreguntas = new Table(new float[]{volumen * 5f});
        for (Pregunta pregunta : listPreguntaIsHombre) {
            contP++;
            String contexto = "NO";
            for (Paciente_Pregunta paciente_Pregunta : listPaciente_PreguntaIsHombre) {
                if (paciente_Pregunta.getPregunta() == pregunta) {
                    contexto = "SÍ " + paciente_Pregunta.getEspecificaciones();
                }
            }
            Table TableAntecedentesPreguntasParrafo = new Table(new float[]{volumen * 2.4f, volumen * 2.6f});
            Cell cellTableAntecedentePreguntas = new Cell().add(new Paragraph(contP + ": " + pregunta.getTextopregunta())).addStyle(styleCell).addStyle(styleTextLeft);
            TableAntecedentesPreguntasParrafo.addCell(cellTableAntecedentePreguntas);
            cellTableAntecedentePreguntas = new Cell().add(new Paragraph(contexto).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
            TableAntecedentesPreguntasParrafo.addCell(cellTableAntecedentePreguntas);
            TableAntecedentesPreguntas.addCell(new Cell().add(TableAntecedentesPreguntasParrafo).addStyle(styleCell));

        }

        Table TableAntecedentesPreguntasMujer = new Table(new float[]{volumen * 5f});
        TableAntecedentesPreguntasMujer.addCell(new Cell().add(new Paragraph("Para pacientes de sexo femenino").setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft));
        for (Pregunta pregunta : listPreguntaIsMujer) {
            contP++;
            String contexto = "NO";
            for (Paciente_Pregunta paciente_Pregunta : listPaciente_PreguntaIsMujer) {
                if (paciente_Pregunta.getPregunta() == pregunta) {
                    contexto = "SÍ " + paciente_Pregunta.getEspecificaciones();
                }

            }
            Table TableAntecedentesPreguntasMujerParrafo = new Table(new float[]{volumen * 2.4f, volumen * 2.6f});
            TableAntecedentesPreguntasMujerParrafo.addCell(new Cell().add(new Paragraph(contP + ": " + pregunta.getTextopregunta())).addStyle(styleCell).addStyle(styleTextLeft));
            TableAntecedentesPreguntasMujerParrafo.addCell(new Cell().add(new Paragraph(contexto).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft));
            TableAntecedentesPreguntasMujer.addCell(new Cell().add(TableAntecedentesPreguntasMujerParrafo).addStyle(styleCell));

        }

        TableAntecedentes.addCell(new Cell().add(new Paragraph("Ha presentado o presenta algunos de los siguientes síntomas").setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft));
        TableAntecedentes.addCell(new Cell().add(TableAntecedentesEnfermedades).addStyle(styleCell));
        TableAntecedentes.addCell(new Cell().add(TableOtrasEnfermedades).addStyle(styleCell));
        TableAntecedentes.addCell(new Cell().add(TableAntecedentesPreguntas).addStyle(styleCell));
        TableAntecedentes.addCell(new Cell().add(TableAntecedentesPreguntasMujer).addStyle(styleCell));
        TableAntecedentes.addCell(new Cell().add(new Paragraph("Antecedentes familiares: " + opaciente.getAntecedentesFamiliares())).addStyle(styleCell).addStyle(styleTextLeft));
        // Fin ANTECEDENTES

        //EXPLORACIÓN FÍSICA
        Paragraph parrafoSubTitulo4 = new Paragraph("IV.  EXPLORACION FÍSICA").setFontSize(10).setFontColor(Color.BLUE).setFont(bold).addStyle(styleTextLeft);
        Table TableExploracionFisica = new Table(new float[]{volumen * 5});
        //Fin EXPLORACIÓN FÍSICA
        /*----Fin Contenido del documento  página 1------*/
 /* Cuerpo del documentos*/
        document.add(Cabecera);
        document.add(parrafoTitulo);
        document.add(parrafoSubTitulo1);
        document.add(tableInformacion);
            document.add(TableMenorDeEdad);
        document.add(TableCasoDeEmergencia);
        document.add(TableMotivoDeConsulta);

        document.add(parrafoSubTitulo2);
        document.add(TableEnfermedadActual);

        document.add(parrafoSubTitulo3);
        document.add(TableAntecedentes);

        document.add(parrafoSubTitulo4);
        document.add(TableExploracionFisica);
        document.close();
        /*----Fin Cuerpo del documentos-----*/
    }

}
