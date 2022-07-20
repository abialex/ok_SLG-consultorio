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
import Entidades.Detalle_Presupuesto;
import Entidades.PlanTratamiento;
import Entidades.Presupuesto;
import Entidades.Tratamiento;
import com.itextpdf.io.font.FontConstants;
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
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import Pdf.style.style1;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.property.AreaBreakType;
import controller.App;
import java.awt.Label;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.itextpdf.layout.element.Image;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author yalle
 */
public class Historiaclinicapdf {

    public static void ImprimirHistoriaClinica(Persona opersona) {
        List<Detalle_Presupuesto> olistDetallePresupuesto = new ArrayList<>();
        Paciente opaciente = (Paciente) App.jpa.createQuery("select p from Paciente p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        Historia_clinica oHistoriaclinica = (Historia_clinica) App.jpa.createQuery("select p from Historia_clinica p where idpaciente=" + opaciente.getIdpaciente()).getSingleResult();
        List<Paciente_Enfermedad> listPaciente_Enfermedad = App.jpa.createQuery("select p from Paciente_Enfermedad p where idpaciente=" + opaciente.getIdpaciente()).getResultList();
        List<Paciente_Pregunta> listPaciente_PreguntaIsMujer = App.jpa.createQuery("select p from Paciente_Pregunta p where idpaciente=" + opaciente.getIdpaciente() + " and ismujer=true").getResultList();
        List<Paciente_Pregunta> listPaciente_PreguntaIsHombre = App.jpa.createQuery("select p from Paciente_Pregunta p where idpaciente=" + opaciente.getIdpaciente() + " and ismujer=false").getResultList();
        List<Pregunta> listPreguntaIsMujer = App.jpa.createQuery("select p from Pregunta p where isMujer=true ORDER BY idpregunta ASC").getResultList();
        List<Pregunta> listPreguntaIsHombre = App.jpa.createQuery("select p from Pregunta p where isMujer=false  ORDER BY idpregunta ASC").getResultList();
        List<Tratamiento> olistTratamiento = App.jpa.createQuery("select p from Tratamiento p where idpersona= " + opersona.getIdpersona() + " and flag = false order by idtratamiento ASC").setMaxResults(10).getResultList();
        List<Presupuesto> olistPresupuesto = App.jpa.createQuery("select p from Presupuesto p where idhistoria_clinica=" + oHistoriaclinica.getIdhistoria_clinica()).getResultList();
        List<PlanTratamiento> olistPlanTratamientos = App.jpa.createQuery("select p from PlanTratamiento p where idhistoria_clinica= " + oHistoriaclinica.getIdhistoria_clinica() + "  order by idplantratamiento ASC").getResultList();

        Presupuesto opresupuesto = null;
        if (!olistPresupuesto.isEmpty()) {
            opresupuesto = olistPresupuesto.get(0);
            olistDetallePresupuesto = App.jpa.createQuery("select p from Detalle_Presupuesto p where idpresupuesto= " + olistPresupuesto.get(0).getIdpresupuesto() + " order by iddetalle_presupuesto ASC").getResultList();
        }

        Period period = Period.between(opersona.getFechaNacimiento(), LocalDate.now());
        long edad = period.getYears();

        int volumen = 105;
        PdfWriter writer = null;
        try {
            writer = new PdfWriter("Pdf\\historia_clinica_" + opersona.getNombres_apellidos() + "_" + opersona.getDni() + ".pdf");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new Label(), "agregue la carpeta Pdf");
        }
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(36, 36, 36, 36);

        style1 evento = new style1(document, opresupuesto);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, evento);
        PdfFont bold = null, font = null;
        try {
            /*--------styles-------------*/
            font = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException ex) {
            Logger.getLogger(Historiaclinicapdf.class.getName()).log(Level.SEVERE, null, ex);
        }

        Style styleCell = new Style().setBorder(Border.NO_BORDER);
        Style styleTextRight = new Style().setTextAlignment(TextAlignment.RIGHT).setFontSize(10f);
        Style styleTextLeft = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(10f);
        Style styleTextCenter = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(10f);
        Style styleTextCenter8 = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(8f);
        /*---------------------Color----------*/
        Color prueba = new DeviceRgb(0, 32, 96);
        Color colorAzul = new DeviceRgb(0, 32, 96);
        Color colorSubtitulo = Color.BLACK;
        Color colorNegro = new DeviceRgb(90, 111, 152);
        Color colorBlanco = Color.WHITE;
        Color colorTituloTabla = new DeviceRgb(0, 112, 192);

        /*----------------Border--------------*/
        Border subrayado = new SolidBorder(colorNegro, 0.5f);
        Border subrayadoNo = Border.NO_BORDER;

        /*-----------------Palabras default-----------*/
        String palabra1 = "desc.";
        String palabra2 = "no presenta";

        /*----------------Palabras vacías-------------*/
        Paragraph palabraEnBlancoLimpio = new Paragraph(".").setFontColor(colorBlanco);
        Paragraph palabraEnBlanco = new Paragraph(".").setFontColor(colorBlanco).setBorderBottom(new SolidBorder(colorNegro, 0.5f));
        /*---------FIN----Palabras vacías-------------*/

 /* Contenido del documento  página 1*/
        //table raya
        Table TableRayas = new Table(new float[]{volumen * 5});
        Cell cellraya = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
        TableRayas.addCell(cellraya);
        //Cabecera
        Table CabeceraParrafo1 = new Table(new float[]{volumen * 0.5f, volumen * 0.7f});
        CabeceraParrafo1.addCell(getCell("H.C Nº:", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo1.addCell(getCell(oHistoriaclinica.getIdhistoria_clinica() + "", styleTextCenter, styleCell, subrayado));

        Table CabeceraParrafo2 = new Table(new float[]{volumen * 0.5f, volumen * 0.7f});
        CabeceraParrafo2.addCell(getCell("FECHA:", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo2.addCell(getCell(oHistoriaclinica.getFechainscripcion() + "", styleTextCenter, styleCell, subrayado));

        Image imgUp = null;
        try {
            imgUp = new Image(ImageDataFactory.create("images\\logoUp.jpg"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Historiaclinicapdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        Cell cellimagUp = new Cell().add(imgUp.setAutoScale(true)).setBorder(Border.NO_BORDER);
        /* new SolidBorder(Color.BLACK,1*/
        String Numero_historia = oHistoriaclinica.getIdhistoria_clinica() + "";
        if ((Numero_historia).length() == 1) {
            Numero_historia = "00" + Numero_historia;
        } else if ((Numero_historia).length() == 2) {
            Numero_historia = "0" + Numero_historia;
        }

        Table TableHC = new Table(new float[]{volumen * 1.2f});
        TableHC.addCell(getCell("HISTORIA CLÍNICA", styleTextCenter, styleTextCenter, subrayadoNo));
        TableHC.addCell(getCell("N° " + Numero_historia + "", styleTextCenter, styleCell, subrayado));
        //TableHC.addCell(getCell(oHistoriaclinica.getFechainscripcion() + "", styleTextCenter, styleCell, subrayado));

        Table Cabecera = new Table(new float[]{volumen * 1.15f, volumen * 2.7f, volumen * 1.15f});
        Cabecera.addCell(new Cell().add(palabraEnBlancoLimpio).addStyle(styleCell));
        Cabecera.addCell(new Cell().add(cellimagUp.setPaddingTop(0)).addStyle(styleCell));
        Cabecera.addCell(new Cell().add(TableHC).addStyle(styleCell));
        Cabecera.setMarginBottom(2);

        //Fin Cabecera
        // ANAMNESIS
        Table tableinit= new Table(new float[]{volumen * 3.5f, volumen * 0.7f,volumen * 0.8f});
        tableinit.setMarginBottom(4);
        tableinit.addCell(getCell("ANAMNESIS", styleCell, styleTextLeft, subrayadoNo).setFontColor(colorAzul).setFont(bold));     
        tableinit.addCell(getCell("Operador: ", styleCell, styleCell, subrayadoNo).setFontColor(colorNegro));
        tableinit.addCell(getCell(oHistoriaclinica.getDoctor().getPersona().getNombres_apellidos().substring(0, 9)+"...", styleCell, styleTextCenter8, subrayado));
        Paragraph parrafoTitulo = new Paragraph("HISTORIA CLÍNICA").setFontSize(14).setFont(bold).setTextAlignment(TextAlignment.CENTER);
        //Paragraph parrafoSubTitulo1 = new Paragraph("ANAMNESIS").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);

        Table table1Parrafo1 = new Table(new float[]{volumen * 0.95f, volumen * 2.55f, volumen * 0.3f, volumen * 0.6f, volumen * 0.3f, volumen * 0.3f});
        table1Parrafo1.addCell(getCell("Nombres y Apellidos:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo1.addCell(getCell(opersona.getNombres_apellidos(), styleTextCenter, styleCell, subrayado));
        table1Parrafo1.addCell(getCell("DNI:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo1.addCell(getCell(opersona.getDni(), styleTextCenter, styleCell, subrayado));
        table1Parrafo1.addCell(getCell("Edad:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo1.addCell(getCell(edad + "", styleTextCenter, styleCell, subrayado));

        Table table1Parrafo2 = new Table(new float[]{volumen * 0.5f, volumen * 1.85f, volumen * 0.45f, volumen * 0.75f, volumen * 0.55f, volumen * 0.9f});
        table1Parrafo2.addCell(getCell("Dirección:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo2.addCell(getCell(opersona.getDomicilio(), styleTextCenter, styleCell, subrayado));
        table1Parrafo2.addCell(getCell("Teléfono:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo2.addCell(getCell(opersona.getTelefono(), styleTextCenter, styleCell, subrayado));
        table1Parrafo2.addCell(getCell("Ocupación:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo2.addCell(getCell(opersona.getOcupacion(), styleTextCenter, styleCell, subrayado));

        Table table1Parrafo3 = new Table(new float[]{volumen * 1f, volumen * 4f});
        table1Parrafo3.addCell(getCell("Motivo de consulta:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo3.addCell(getCell(oHistoriaclinica.getMotivoConsulta() + "", styleTextCenter, styleCell, subrayado));

        Table table1Parrafo4 = new Table(new float[]{volumen * 0.6f, volumen * 1.85f, volumen * 0.75f, volumen * 1.8f});
        table1Parrafo4.addCell(getCell("Enf. actual:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo4.addCell(getCell(opaciente.getEnfermedadActual(), styleTextCenter, styleCell, subrayado));
        table1Parrafo4.addCell(getCell("Enf. sistémicas:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo4.addCell(getCell(opaciente.getEnfermedadSistemica(), styleTextCenter, styleCell, subrayado));

        Table table1Parrafo5 = new Table(new float[]{volumen * 1.2f, volumen * 3.8f});
        table1Parrafo5.addCell(getCell("Antecedentes personales:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo5.addCell(getCell(opaciente.getAntecedentesFamiliares(), styleTextCenter, styleCell, subrayado));

        Table table1Parrafo6 = new Table(new float[]{volumen * 0.5f, volumen * 4.5f});
        table1Parrafo6.addCell(getCell("Operador:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo6.addCell(getCell(oHistoriaclinica.getDoctor().getPersona().getNombres_apellidos(), styleTextCenter, styleCell, subrayado));

        Table tableInformacion = new Table(new float[]{volumen * 5});
        tableInformacion.addCell(new Cell().add(table1Parrafo1).addStyle(styleCell));
        tableInformacion.addCell(new Cell().add(table1Parrafo2).addStyle(styleCell));
        tableInformacion.addCell(new Cell().add(table1Parrafo3).addStyle(styleCell));
        tableInformacion.addCell(new Cell().add(table1Parrafo4).addStyle(styleCell));
        tableInformacion.addCell(new Cell().add(table1Parrafo5).addStyle(styleCell));
        tableInformacion.addCell(new Cell().add(table1Parrafo6).addStyle(styleCell));

        //table1Datos.addCell(cellimag);
        Table TableMenorDeEdadParrafo1 = new Table(new float[]{volumen * 5});
        Cell cellTableMenorDeEdadParrafo1 = new Cell().add(new Paragraph("Para pacientes menores de edad:").setFontColor(colorSubtitulo).setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo1.addCell(cellTableMenorDeEdadParrafo1);

        Table TableMenorDeEdadParrafo2 = new Table(new float[]{volumen * 0.95f, volumen * 1.25f, volumen * 0.3f, volumen * 1f, volumen * 0.43f, volumen * 1.08f});
        TableMenorDeEdadParrafo2.addCell(getCell("Nombre del tutor:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableMenorDeEdadParrafo2.addCell(getCell(opersona.getTutorNombre(), styleTextCenter, styleCell, subrayado));
        TableMenorDeEdadParrafo2.addCell(getCell("DNI:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableMenorDeEdadParrafo2.addCell(getCell(opersona.getTutorDni(), styleTextCenter, styleCell, subrayado));
        TableMenorDeEdadParrafo2.addCell(getCell("Telefono:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableMenorDeEdadParrafo2.addCell(getCell(opersona.getTutorTelefono(), styleTextCenter, styleCell, subrayado));

        Table TableMenorDeEdad = new Table(new float[]{volumen * 5});
        TableMenorDeEdad.addCell(new Cell().add(TableMenorDeEdadParrafo1).addStyle(styleCell));
        TableMenorDeEdad.addCell(new Cell().add(TableMenorDeEdadParrafo2).addStyle(styleCell));
        //TableMenorDeEdad.setMarginTop(10);

        Table TableCasoDeEmergenciaParrafo1 = new Table(new float[]{volumen * 5});
        Cell cellTableCasoDeEmergenciaParrafo1 = new Cell().add(new Paragraph("En caso de emergencia comunicarse con").setFontColor(colorSubtitulo).setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft);
        TableCasoDeEmergenciaParrafo1.addCell(cellTableCasoDeEmergenciaParrafo1);

        Table TableCasoDeEmergenciaParrafo2 = new Table(new float[]{volumen * 0.45f, volumen * 1.25f, volumen * 0.55f, volumen * 1.25f, volumen * 0.45f, volumen * 1.05f});
        TableCasoDeEmergenciaParrafo2.addCell(getCell("Nombre:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableCasoDeEmergenciaParrafo2.addCell(getCell(opaciente.getEmergenciaNombre(), styleTextCenter, styleCell, subrayado));
        TableCasoDeEmergenciaParrafo2.addCell(getCell("Parentesco:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableCasoDeEmergenciaParrafo2.addCell(getCell(opaciente.getEmergenciaParentesco(), styleTextCenter, styleCell, subrayado));
        TableCasoDeEmergenciaParrafo2.addCell(getCell("Telefono:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableCasoDeEmergenciaParrafo2.addCell(getCell(opaciente.getEmergenciaTelefono(), styleTextCenter, styleCell, subrayado));

        Table TableCasoDeEmergencia = new Table(new float[]{volumen * 5});
        TableCasoDeEmergencia.addCell(new Cell().add(TableCasoDeEmergenciaParrafo1).addStyle(styleCell));
        TableCasoDeEmergencia.addCell(new Cell().add(TableCasoDeEmergenciaParrafo2).addStyle(styleCell));

        Table TableMotivoDeConsultaParrafo1 = new Table(new float[]{volumen * 1, volumen * 4});
        TableMotivoDeConsultaParrafo1.addCell(getCell("Motivo de Consulta:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableMotivoDeConsultaParrafo1.addCell(getCell(oHistoriaclinica.getMotivoConsulta(), styleTextCenter, styleCell, subrayado));

        Table TableMotivoDeConsulta = new Table(new float[]{volumen * 5});
        TableMotivoDeConsulta.addCell(new Cell().add(TableMotivoDeConsultaParrafo1).addStyle(styleCell));
        TableMotivoDeConsulta.setMarginBottom(10);
        //Fin ANAMNESIS

        //ENFERMEDAD ACTUAL
        Paragraph parrafoSubTitulo2 = new Paragraph("II.  ENFERMEDAD ACTUAL").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);
        Table TableEnfermedadActual = new Table(new float[]{volumen * 1.3f, volumen * 3.7f});
        TableEnfermedadActual.addCell(getCell("Signo y síntomas principales:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableEnfermedadActual.addCell(getCell(opaciente.getSintomasEnfermedadActual(), styleTextCenter, styleCell, subrayado));
        TableEnfermedadActual.addCell(getCell("Tiempo de la enfermedad:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableEnfermedadActual.addCell(getCell(opaciente.getTiempoEnfermedadActual(), styleTextCenter, styleCell, subrayado));
        //Fin ENFERMEDAD ACTUAL

        //ANTECEDENTES
        //Ha presentado o presenta algunos de los siguientes síntomas
        Paragraph parrafoSubTitulo3 = new Paragraph("III.  ANTECEDENTES").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);
        Table TableAntecedentes = new Table(new float[]{volumen * 5});

        Table TableAntecedentesEnfermedades = new Table(new float[]{volumen * 1.25f, volumen * 1.25f, volumen * 1.25f, volumen * 1.25f});
        int cont = 0;
        for (Paciente_Enfermedad paciente_Enfermedad : listPaciente_Enfermedad) {
            String detalle = "";
            if (!paciente_Enfermedad.getDetalleEnfermedad().isEmpty()) {
                detalle = " : " + paciente_Enfermedad.getDetalleEnfermedad();
            }
            cont++;
            TableAntecedentesEnfermedades.addCell(getCell(cont + ": " + paciente_Enfermedad.getEnfermedad().getNombre() + detalle, styleTextLeft, styleCell, subrayadoNo));
        }

        //Otras enfermedades
        Table TableOtrasEnfermedades = new Table(new float[]{volumen * 0.95f, volumen * 4.05f});
        TableOtrasEnfermedades.addCell(getCell("Otras enfermedades:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableOtrasEnfermedades.addCell(getCell(opaciente.getOtrasEnfermedades(), styleTextCenter, styleCell, subrayado));

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
            TableAntecedentesPreguntasParrafo.addCell(getCell(contP + ": " + pregunta.getTextopregunta(), styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
            TableAntecedentesPreguntasParrafo.addCell(getCell(contexto, styleTextLeft, styleCell, subrayado));
            TableAntecedentesPreguntas.addCell(new Cell().add(TableAntecedentesPreguntasParrafo).addStyle(styleCell));
        }

        Table TableAntecedentesPreguntasMujer = new Table(new float[]{volumen * 5f});
        TableAntecedentesPreguntasMujer.addCell(new Cell().add(new Paragraph("Para pacientes de sexo femenino").setFontColor(colorSubtitulo).setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft));
        for (Pregunta pregunta : listPreguntaIsMujer) {
            contP++;
            String contexto = "NO";
            for (Paciente_Pregunta paciente_Pregunta : listPaciente_PreguntaIsMujer) {
                if (paciente_Pregunta.getPregunta() == pregunta) {
                    contexto = "SÍ " + paciente_Pregunta.getEspecificaciones();
                }

            }
            Table TableAntecedentesPreguntasMujerParrafo = new Table(new float[]{volumen * 2.4f, volumen * 2.6f});
            TableAntecedentesPreguntasMujerParrafo.addCell(getCell(contP + ": " + pregunta.getTextopregunta(), styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
            TableAntecedentesPreguntasMujerParrafo.addCell(getCell(contexto, styleTextLeft, styleCell, subrayado));
            TableAntecedentesPreguntasMujer.addCell(new Cell().add(TableAntecedentesPreguntasMujerParrafo).addStyle(styleCell));
        }

        Table TableAntecedente = new Table(new float[]{volumen * 1.1f, volumen * 3.9f});
        TableAntecedente.addCell(getCell("Antecedentes familiares: ", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableAntecedente.addCell(getCell(opaciente.getAntecedentesFamiliares(), styleTextLeft, styleCell, subrayado));

        TableAntecedentes.addCell(new Cell().add(new Paragraph("Ha presentado o presenta algunos de los siguientes síntomas").setFontColor(colorSubtitulo).setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft));
        TableAntecedentes.addCell(new Cell().add(TableAntecedentesEnfermedades).addStyle(styleCell));
        TableAntecedentes.addCell(new Cell().add(TableOtrasEnfermedades).addStyle(styleCell));
        TableAntecedentes.addCell(new Cell().add(TableAntecedentesPreguntas).addStyle(styleCell));
        TableAntecedentes.addCell(new Cell().add(TableAntecedentesPreguntasMujer).addStyle(styleCell));
        TableAntecedentes.addCell(new Cell().add(TableAntecedente).addStyle(styleCell));
        // Fin ANTECEDENTES

        //EXPLORACIÓN FÍSICA
        Paragraph parrafoSubTitulo4 = new Paragraph("IV.  EXPLORACION FÍSICA").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);

        Table TableExploracionFisicaParrafo1 = new Table(new float[]{volumen * 0.68f, volumen * 1.72f, volumen * 1.06f, volumen * 1.54f});
        TableExploracionFisicaParrafo1.addCell(getCell("Signos vitales:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableExploracionFisicaParrafo1.addCell(getCell(oHistoriaclinica.getSignosVitales(), styleTextCenter, styleCell, subrayado));
        TableExploracionFisicaParrafo1.addCell(getCell("Saturación de oxígeno:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableExploracionFisicaParrafo1.addCell(getCell(oHistoriaclinica.getSaturacionOxigeno(), styleTextCenter, styleCell, subrayado));
        //0
        Table TableExploracionFisicaParrafo2 = new Table(new float[]{volumen * 0.25f, volumen * 1f, volumen * 0.25f,
            volumen * 1f, volumen * 0.65f, volumen * 0.6f, volumen * 0.25f, volumen * 1f});
        TableExploracionFisicaParrafo2.addCell(getCell("P.A:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableExploracionFisicaParrafo2.addCell(getCell(oHistoriaclinica.getPA(), styleTextCenter, styleCell, subrayado));
        TableExploracionFisicaParrafo2.addCell(getCell("F.C:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableExploracionFisicaParrafo2.addCell(getCell(oHistoriaclinica.getFC(), styleTextCenter, styleCell, subrayado));
        TableExploracionFisicaParrafo2.addCell(getCell("Temperatura:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableExploracionFisicaParrafo2.addCell(getCell(oHistoriaclinica.getTemperatura(), styleTextCenter, styleCell, subrayado));
        TableExploracionFisicaParrafo2.addCell(getCell("F.R:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableExploracionFisicaParrafo2.addCell(getCell(oHistoriaclinica.getFR(), styleTextCenter, styleCell, subrayado));
        Table TableExploracionFisicaParrafo3 = getTableField(oHistoriaclinica.getExamenClinicoGeneral(), "Examen clínico general:", 1.09f, 3.9f, volumen, colorNegro, styleCell, styleTextLeft, palabraEnBlanco);

        Table TableExploracionFisica = new Table(new float[]{volumen * 5});
        TableExploracionFisica.addCell(new Cell().add(TableExploracionFisicaParrafo1).addStyle(styleCell));
        TableExploracionFisica.addCell(new Cell().add(TableExploracionFisicaParrafo2).addStyle(styleCell));
        TableExploracionFisica.addCell(new Cell().add(TableExploracionFisicaParrafo3).addStyle(styleCell));
        // TableExploracionFisica.addCell(new Cell().add(TableRaya).addStyle(styleCell));

        //Fin EXPLORACIÓN FÍSICA
        //DIAGNOSTICO
        Paragraph parrafoSubTitulo5 = new Paragraph("V.  DIAGNÓSTICO").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);
        Table TableDiagnosticoParrafo1 = getTableField(oHistoriaclinica.getDiagnosticoCIE10(), "Diagnóstico:", 0.6f, 4.4f, volumen, colorNegro, styleCell, styleTextLeft, palabraEnBlanco);
        Table TableDiagnostico = new Table(new float[]{volumen * 5});
        TableDiagnostico.addCell(new Cell().add(TableDiagnosticoParrafo1).addStyle(styleCell));

        //Fin DIAGNOSTICO
        //PLAN DE TRATAMIENTO - RECOMENDACIONES
        Paragraph parrafoSubTitulo6 = new Paragraph("VI. PLAN DE TRATAMIENTO - RECOMENDACIONES").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);
        Table TableTramientoRecomendaciones = getTable(oHistoriaclinica.getRecomendaciones(), volumen, palabraEnBlanco, styleCell, styleTextLeft);
        //Fin PLAN DE TRATAMIENTO - RECOMENDACIONES

        //PRONOSTICO
        Table TablePronostico = new Table(new float[]{volumen * 0.9f, volumen * 4.1f});
        TablePronostico.addCell(getCell("VII. PRONÓSTICO:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TablePronostico.addCell(getCell(oHistoriaclinica.getAltaPaciente(), styleTextCenter, styleCell, subrayado));
        TablePronostico.setMarginTop(5);
        TablePronostico.setMarginBottom(5);

        //fin PRONOSTICO
        //ALTA PACIENTE
        Table TableAlta = new Table(new float[]{volumen * 1.01f, volumen * 3.99f});
        //Fin ALTA PACIENTE
        TableAlta.addCell(getCell("VIII. ALTA PACIENTE:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableAlta.addCell(getCell(oHistoriaclinica.getAltaPaciente(), styleTextCenter, styleCell, subrayado));

        /*----Fin Contenido del documento  página 1------*/
 /*--------Contenido del documento página 2--------*/
        //ODONTOGRAMA
        Table TableOdontograma = new Table(new float[]{volumen * 0.6f, volumen * 3.8f, volumen * 0.6f});
        Image img = null;
        try {
            img = new Image(ImageDataFactory.create("images\\odontograma.png"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Historiaclinicapdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        Cell cellimag = new Cell().add(img.setAutoScale(true)).setBorder(Border.NO_BORDER);
        /* new SolidBorder(Color.BLACK,1*/
        TableOdontograma.addCell(getCell("", styleCell, styleCell, subrayadoNo));
        TableOdontograma.addCell(cellimag.addStyle(styleTextCenter));
        TableOdontograma.addCell(getCell("", styleCell, styleCell, subrayadoNo));
        Table TablePresupuesto = new Table(new float[]{volumen * 4f, volumen * 0.4f, volumen * 0.6f});
        TablePresupuesto.addCell(new Cell().add(new Paragraph("PRESUPUESTO").setFont(bold).addStyle(styleTextCenter).setFontColor(colorTituloTabla)));
        TablePresupuesto.addCell(new Cell().add(new Paragraph("C/U").setFont(bold).addStyle(styleTextCenter).setFontColor(colorTituloTabla)));
        TablePresupuesto.addCell(new Cell().add(new Paragraph("MONTO").setFont(bold).addStyle(styleTextCenter).setFontColor(colorTituloTabla)));
        float montoTotalPresupuesto = 0;
        for (Detalle_Presupuesto presupuesto : olistDetallePresupuesto) {
            TablePresupuesto.addCell(new Cell().add(new Paragraph(presupuesto.getDescripcion()).addStyle(styleTextLeft)));
            TablePresupuesto.addCell(new Cell().add(new Paragraph(presupuesto.getCantidad() + "").addStyle(styleTextCenter)));
            TablePresupuesto.addCell(new Cell().add(new Paragraph(presupuesto.getMonto() + "").addStyle(styleTextCenter)));
            montoTotalPresupuesto = montoTotalPresupuesto + presupuesto.getMonto() * presupuesto.getCantidad();
        }
        int contadorEspacioPresupuesto = 15 - olistDetallePresupuesto.size();
        for (int i = 0; i < contadorEspacioPresupuesto; i++) {
            TablePresupuesto.addCell(new Cell().add(palabraEnBlancoLimpio.addStyle(styleTextCenter)));
            TablePresupuesto.addCell(new Cell().add(new Paragraph("").addStyle(styleTextLeft)));
            TablePresupuesto.addCell(new Cell().add(new Paragraph("").addStyle(styleTextLeft)));
            //montoTotal = montoTotal + tratamiento.getMonto();
        }

        TablePresupuesto.addCell(new Cell(1, 2).add(new Paragraph("MONTO TOTAL ").addStyle(styleTextCenter).setFont(bold).setFontColor(colorTituloTabla)));
        TablePresupuesto.addCell(new Cell().add(new Paragraph(montoTotalPresupuesto + "").addStyle(styleTextCenter)));

        Paragraph parrafoRadiografico = new Paragraph("INFORME RADIOGRÁFICO").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);
        Table TableRadiografico = getTable(oHistoriaclinica.getInformeRadiografico(), volumen, palabraEnBlanco, styleCell, styleTextLeft);

        Paragraph parrafoPlantaTratamiento = new Paragraph("PLAN DE TRATAMIENTO").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);
        Table TablePlanTratamiento = new Table(new float[]{volumen * 2.5f, volumen * 2.5f});
        for (int i = 0; i < olistPlanTratamientos.size(); i++) {
            TablePlanTratamiento.addCell(getCell((i + 1) + ".- " + olistPlanTratamientos.get(i).getDescripcion(), styleCell, styleCell, subrayado).setFontColor(colorNegro).setMarginRight(15));
        }
        for (int i = olistPlanTratamientos.size(); i < 6; i++) {
            TablePlanTratamiento.addCell(getCell((i + 1) + ".-", styleCell, styleCell, subrayado).setFontColor(colorNegro).setMarginRight(15));
        }
        Table TableMontoPresupuesto = new Table(new float[]{volumen * 4.17f,volumen * 0.33f, volumen * 0.5f});
        TableMontoPresupuesto.addCell(getCell(" ", styleCell, styleCell, subrayadoNo));
        TableMontoPresupuesto.addCell(getCell("Total:", styleCell, styleCell, subrayadoNo).setFontColor(colorNegro));
        TableMontoPresupuesto.addCell(getCell( montoTotalPresupuesto+"", styleCell, styleTextCenter, subrayado));
        Table TableTratamiento = new Table(new float[]{volumen * 0.6f, volumen * 3.3f, volumen * 0.6f, volumen * 0.5f});
        TableTratamiento.setMarginTop(10);
        TableTratamiento.addCell(new Cell().add(new Paragraph("FECHA").setFont(bold).addStyle(styleTextCenter).setFontColor(colorTituloTabla)));
        TableTratamiento.addCell(new Cell().add(new Paragraph("PROCEDIMIENTO").setFont(bold).addStyle(styleTextCenter).setFontColor(colorTituloTabla)));
        TableTratamiento.addCell(new Cell().add(new Paragraph("A CTA").setFont(bold).addStyle(styleTextCenter).setFontColor(colorTituloTabla)));
        TableTratamiento.addCell(new Cell().add(new Paragraph("SALDO").setFont(bold).addStyle(styleTextCenter).setFontColor(colorTituloTabla)));
        float montoTotal = opresupuesto != null ? opresupuesto.getMonto_total() : 0;

        for (Tratamiento tratamiento : olistTratamiento) {
            montoTotal = montoTotal - tratamiento.getMonto();
            TableTratamiento.addCell(new Cell().add(new Paragraph(tratamiento.getFechaRealizada() + "").addStyle(styleTextCenter)));
            TableTratamiento.addCell(new Cell().add(new Paragraph(tratamiento.getTratamiento()).addStyle(styleTextLeft)));
            TableTratamiento.addCell(new Cell().add(new Paragraph(tratamiento.getMonto() + "").addStyle(styleTextCenter)));
            TableTratamiento.addCell(new Cell().add(new Paragraph(montoTotal + "").addStyle(styleTextCenter)));

        }
        int contadorEspacio = 25 - olistTratamiento.size();
        for (int i = 0; i < contadorEspacio; i++) {
            TableTratamiento.addCell(new Cell().add(palabraEnBlancoLimpio.addStyle(styleTextCenter)));
            TableTratamiento.addCell(new Cell().add(new Paragraph("").addStyle(styleTextLeft)));
            TableTratamiento.addCell(new Cell().add(new Paragraph("").addStyle(styleTextCenter)));
            TableTratamiento.addCell(new Cell().add(new Paragraph("").addStyle(styleTextCenter)));
            //montoTotal = montoTotal + tratamiento.getMonto();
        }

        //TableTratamiento.addCell(new Cell(1, 3).add(new Paragraph("MONTO TOTAL ").addStyle(styleTextCenter).setFont(bold).setFontColor(colorTituloTabla)));
        //TableTratamiento.addCell(new Cell().add(new Paragraph(montoTotal + "").addStyle(styleTextCenter)));

        /* Cuerpo del documentos*/
        document.add(Cabecera);
        //document.add(parrafoTitulo);
        document.add(tableinit);
        document.add(tableInformacion);
        /*        if (edad < 18) {
            document.add(TableMenorDeEdad);
        } else {
            TableCasoDeEmergencia.setMarginTop(71);
        }
        document.add(TableCasoDeEmergencia);
        document.add(TableMotivoDeConsulta);

        document.add(parrafoSubTitulo2);
        document.add(TableEnfermedadActual);

        document.add(parrafoSubTitulo3);
        document.add(TableAntecedentes);

        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(parrafoSubTitulo4);
        document.add(TableExploracionFisica);

        document.add(parrafoSubTitulo5);
        document.add(TableDiagnostico);

        document.add(parrafoSubTitulo6);
        document.add(TableTramientoRecomendaciones);
         
        document.add(TablePronostico);
        
        document.add(TableAlta);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
         */
        document.add(TableOdontograma);
        document.add(TablePresupuesto);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        document.add(parrafoPlantaTratamiento);
        document.add(TablePlanTratamiento);

        document.add(parrafoRadiografico);
        document.add(TableRadiografico);
        
        document.add(TableMontoPresupuesto);
        document.add(TableTratamiento);

        document.close();
        /*----Fin Cuerpo del documentos-----*/
    }

    public static Table getTable(String cadena, int volumen, Paragraph palabraEnBlanco, Style styleCell, Style styleTextLeft) {
        Table TablePrincipal = new Table(new float[]{volumen * 5});
        Table TableParrafo = new Table(new float[]{volumen * 5});
        Color colorSubrayado = new DeviceRgb(90, 111, 152);
        Paragraph oParagrah;
        int numCharacteres = 108;
        int iteracion = cadena.length() / numCharacteres;
        for (int i = 0; i < iteracion; i++) {
            oParagrah = new Paragraph(cadena.substring(i * numCharacteres, (i + 1) * numCharacteres));
            Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(colorSubrayado, 0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
            TableParrafo.addCell(cellParrafo1);
        }
        oParagrah = cadena.isEmpty() ? palabraEnBlanco : new Paragraph(cadena.substring(cadena.length() - cadena.length() % numCharacteres, cadena.length()));;
        Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(colorSubrayado, 0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableParrafo.addCell(cellParrafo1);
        for (int i = 0; i < 2 - iteracion; i++) {
            Cell cellraya = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
            TableParrafo.addCell(cellraya);
        }
        TablePrincipal.addCell(new Cell().add(TableParrafo).addStyle(styleCell));
        return TablePrincipal;
    }

    public static Table getTableField(String cadena, String titulo, float tamfield, float tamfield2, int volumen, Color colorNegro, Style styleCell, Style styleTextLeft, Paragraph palabraEnBlanco) {
        Table TableAtributo = new Table(new float[]{volumen * tamfield, volumen * tamfield2});
        Table TableParrafo = new Table(new float[]{volumen * 5});
        Color colorSubrayado = new DeviceRgb(90, 111, 152);
        Paragraph oParagrah;
        int subnumCharacteres = 75;
        int numCharacteres = 100;
        int iteracion = cadena.length() / numCharacteres;
        int division = cadena.length() / subnumCharacteres;
        boolean aux = division == 0;
        Cell cellDi = new Cell().add(new Paragraph(titulo).setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableAtributo.addCell(cellDi);
        Paragraph oParagra = cadena.length() == 0 ? palabraEnBlanco : new Paragraph(aux ? cadena : cadena.substring(0 * numCharacteres, (0 + 1) * subnumCharacteres));
        Cell cellParraf = new Cell().add(oParagra.setBorderBottom(new SolidBorder(colorSubrayado, 0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableAtributo.addCell(cellParraf);
        TableParrafo.addCell(new Cell().add(TableAtributo).addStyle(styleCell));
        for (int i = 1; i < iteracion; i++) {
            oParagrah = new Paragraph(cadena.substring(i * numCharacteres, (i + 1) * numCharacteres));
            Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(colorSubrayado, 0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
            TableParrafo.addCell(cellParrafo1);
        }
        oParagrah = aux ? palabraEnBlanco : new Paragraph(cadena.substring(cadena.length() - cadena.length() % numCharacteres, cadena.length()));;
        Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(colorSubrayado, 0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableParrafo.addCell(cellParrafo1);
        if (iteracion == 1 || iteracion == 0) {
            Cell cellraya2 = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
            TableParrafo.addCell(cellraya2);
        }
        return TableParrafo;
    }

    public static Cell getCell(String palabra, Style posicion, Style border, Border subrayado) {
        Color colorBlanco = Color.WHITE;
        Color colorSubrayado = new DeviceRgb(90, 111, 152);
        Paragraph palabraEnBlanco = new Paragraph(".").setFontColor(colorBlanco).setBorderBottom(new SolidBorder(colorSubrayado, 0.5f));
        Paragraph Parapalabra = palabra.isEmpty() ? palabraEnBlanco : new Paragraph(palabra);

        return new Cell().add(Parapalabra.setBorderBottom(subrayado)).addStyle(posicion).addStyle(border);
    }

}
