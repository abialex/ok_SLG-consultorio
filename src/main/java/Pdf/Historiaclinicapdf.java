/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pdf;

import Entidades.Persona;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yalle
 */
public class Historiaclinicapdf {

    public static  void ImprimirHistoriaClinica(Persona opersona) throws IOException {
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

        /* Contenido del documento  página 1*/
        //Cabecera
        Table Cabecera = new Table(new float[]{volumen * 4.99f});
        Cell cabeceraCell = new Cell().add(new Paragraph("H.C N°: ____________________")).addStyle(styleTextRight).addStyle(styleCell);
        Cabecera.addCell(cabeceraCell);
        cabeceraCell = new Cell().add(new Paragraph("FECHA: ____________________")).addStyle(styleTextRight).addStyle(styleCell);
        Cabecera.addCell(cabeceraCell);
        //Fin Cabecera
          Image img = null;
        try {
            img = new Image(ImageDataFactory.create(opersona.getUrlImag()));
        } catch (MalformedURLException ex) {
            Logger.getLogger(style1.class.getName()).log(Level.SEVERE, null, ex);
        }
         Cell cellImag=new Cell().add(img.setAutoScale(true)).setBorder(Border.NO_BORDER); /*new SolidBorder(Color.BLACK,1*/ 
        // ANAMNESIS
        Paragraph parrafoTitulo = new Paragraph("HISTORIA CLÍNICA").setFontSize(14).setFont(bold).setTextAlignment(TextAlignment.CENTER);
        Paragraph parrafoSubTitulo1 = new Paragraph("I.  ANAMNESIS").setFontSize(10).setFontColor(Color.BLUE).setFont(bold).addStyle(styleTextLeft);

        Table table1Parrafo1 = new Table(new float[]{volumen * 3.5f, volumen * 1f, volumen * 0.5f});
        Cell cell1Parrafo1 = new Cell().add(new Paragraph("Nombres y Apellidos: "+opersona.getNombres_apellidos())).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo1.addCell(cellImag);
        cell1Parrafo1 = new Cell().add(new Paragraph("Sexo: "+opersona.getSexo())).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo1.addCell(cell1Parrafo1);
        cell1Parrafo1 = new Cell().add(new Paragraph("Edad: "+ opersona.getEdad())).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo1.addCell(cell1Parrafo1);

        Table table1Parrafo2 = new Table(new float[]{volumen * 3.5f, volumen *1.5f});
        Cell cell1Parrafo2 = new Cell().add(new Paragraph("Domicilio: "+opersona.getDomicilio())).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo2.addCell(cell1Parrafo2);
        cell1Parrafo2 = new Cell().add(new Paragraph("DNI:")).addStyle(styleTextLeft).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo2.addCell(cell1Parrafo2);

        Table table1Parrafo3 = new Table(new float[]{volumen * 2.5f, volumen * 2.5f});
        Cell cell1Parrafo3 = new Cell().add(new Paragraph("Fecha de Nacimiento:")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo3.addCell(cell1Parrafo3);
        cell1Parrafo3 = new Cell().add(new Paragraph("Lugar de Procedencia:")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo3.addCell(cell1Parrafo3);

        Table table1Parrafo4 = new Table(new float[]{volumen * 1.66f, volumen * 1.66f, volumen * 1.66f});
        Cell cell1Parrafo4 = new Cell().add(new Paragraph("Ocupación:")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo4.addCell(cell1Parrafo4);
        cell1Parrafo4 = new Cell().add(new Paragraph("Estado Civil:")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo4.addCell(cell1Parrafo4);
        cell1Parrafo4 = new Cell().add(new Paragraph("Telf:")).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo4.addCell(cell1Parrafo4);

        /* Image img = null;
        try {
            img = new Image(ImageDataFactory.create("images\\imagenDiente.png"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(EventoPagina.class.getName()).log(Level.SEVERE, null, ex);
        }
        Cell cellimag=new Cell().add(img.setAutoScale(true)).setBorder(Border.NO_BORDER); /* new SolidBorder(Color.BLACK,1*/
        Table table1Datos = new Table(new float[]{volumen * 5});
        Cell cellTable1 = new Cell().add(table1Parrafo1).addStyle(styleCell);
        table1Datos.addCell(cellTable1);
        cellTable1 = new Cell().add(table1Parrafo2).addStyle(styleCell);
        table1Datos.addCell(cellTable1);
        cellTable1 = new Cell().add(table1Parrafo3).addStyle(styleCell);
        table1Datos.addCell(cellTable1);
        cellTable1 = new Cell().add(table1Parrafo4).addStyle(styleCell);
        table1Datos.addCell(cellTable1);
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

        Table TableMotivoDeConsultaParrafo1 = new Table(new float[]{volumen * 5});
        Cell cellTableMotivoDeConsultaParrafo1 = new Cell().add(new Paragraph("Motivo de Consulta: ").setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft);
        TableMotivoDeConsultaParrafo1.addCell(cellTableMotivoDeConsultaParrafo1);

        Table TableMotivoDeConsulta = new Table(new float[]{volumen * 5});
        Cell cellTableMotivoDeConsulta = new Cell().add(TableMotivoDeConsultaParrafo1).addStyle(styleCell);
        TableMotivoDeConsulta.addCell(cellTableMotivoDeConsulta);
        //Fin ANAMNESIS

        //ENFERMEDAD ACTUAL
        Paragraph parrafoSubTitulo2 = new Paragraph("I.  ENFERMEDAD ACTUAL").setFontSize(10).setFontColor(Color.BLUE).setFont(bold).addStyle(styleTextLeft);
        Table TableEnfermedadActual = new Table(new float[]{volumen * 5});
        Cell cellTableEnfermedadActual = new Cell().add(new Paragraph("Signo y síntomas principales")).addStyle(styleCell).addStyle(styleTextLeft);
        TableEnfermedadActual.addCell(cellTableEnfermedadActual);
        cellTableEnfermedadActual = new Cell().add(new Paragraph("Tiempo de la enfermedad")).addStyle(styleCell).addStyle(styleTextLeft);
        TableEnfermedadActual.addCell(cellTableEnfermedadActual);
        //Fin ENFERMEDAD ACTUAL

        //ENFERMEDAD ACTUAL
        //Fin ENFERMEDAD ACTUAL
        /*----Fin Contenido del documento  página 1------*/
 /* Cuerpo del documentos*/
        document.add(Cabecera);
        document.add(parrafoTitulo);
        document.add(parrafoSubTitulo1);
        document.add(table1Datos);
        document.add(TableMenorDeEdad);
        document.add(TableCasoDeEmergencia);
        document.add(TableMotivoDeConsulta);

        document.add(parrafoSubTitulo2);
        document.add(TableEnfermedadActual);

        document.close();
        /*----Fin Cuerpo del documentos-----*/
    }

}
