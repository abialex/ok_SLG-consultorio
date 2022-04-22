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
import javax.swing.JOptionPane;

/**
 *
 * @author yalle
 */
public class Historiaclinicapdf {

    public static void ImprimirHistoriaClinica(Persona opersona) {
        Paciente opaciente = (Paciente) App.jpa.createQuery("select p from Paciente p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        Historia_clinica oHistoriaclinica = (Historia_clinica) App.jpa.createQuery("select p from Historia_clinica p where idpaciente=" + opaciente.getIdpaciente()).getSingleResult();
        List<Paciente_Enfermedad> listPaciente_Enfermedad = App.jpa.createQuery("select p from Paciente_Enfermedad p where idpaciente=" + opaciente.getIdpaciente()).getResultList();
        List<Paciente_Pregunta> listPaciente_PreguntaIsMujer = App.jpa.createQuery("select p from Paciente_Pregunta p where idpaciente=" + opaciente.getIdpaciente() + " and ismujer=true").getResultList();
        List<Paciente_Pregunta> listPaciente_PreguntaIsHombre = App.jpa.createQuery("select p from Paciente_Pregunta p where idpaciente=" + opaciente.getIdpaciente() + " and ismujer=false").getResultList();
        List<Pregunta> listPreguntaIsMujer = App.jpa.createQuery("select p from Pregunta p where isMujer=true ORDER BY idpregunta ASC").getResultList();
        List<Pregunta> listPreguntaIsHombre = App.jpa.createQuery("select p from Pregunta p where isMujer=false  ORDER BY idpregunta ASC").getResultList();
        List<Tratamiento> olistTratamiento = App.jpa.createQuery("select p from Tratamiento p where idpersona= " + opersona.getIdpersona() + " and flag = false order by idtratamiento ASC").setMaxResults(10).getResultList();
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

        style1 evento = new style1(document);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, evento);
        PdfFont bold = null, font;
        try {
            /*--------styles-------------*/
            font = PdfFontFactory.createFont(FontConstants.HELVETICA);
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA);
        } catch (IOException ex) {
            Logger.getLogger(Historiaclinicapdf.class.getName()).log(Level.SEVERE, null, ex);
        }

        Style styleCell = new Style().setBorder(Border.NO_BORDER);
        Style styleTextRight = new Style().setTextAlignment(TextAlignment.RIGHT).setFontSize(10f);
        Style styleTextLeft = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(10f);
        Style styleTextCenter = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(10f);

        /*---------------------Color----------*/
        Color prueba = new DeviceRgb(0, 204, 204);
        Color colorAzul = new DeviceRgb(255, 178, 102);
        Color colorSubtitulo = Color.BLACK;
        Color colorNegro = new DeviceRgb(0, 204, 204);
        Color colorBlanco = Color.WHITE;

        /*----------------Border--------------*/
        Border subrayado = new SolidBorder(0.5f);
        Border subrayadoNo = Border.NO_BORDER;

        /*-----------------Palabras default-----------*/
        String palabra1 = "desc.";
        String palabra2 = "no presenta";

        /*----------------Palabras vacías-------------*/
        Paragraph palabraEnBlancoLimpio = new Paragraph(".").setFontColor(colorBlanco);
        Paragraph palabraEnBlanco = new Paragraph(".").setFontColor(colorBlanco).setBorderBottom(new SolidBorder(0.5f));
        /*---------FIN----Palabras vacías-------------*/

 /* Contenido del documento  página 1*/
        //table raya
        Table TableRayas = new Table(new float[]{volumen * 5});
        Cell cellraya = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
        TableRayas.addCell(cellraya);
        //Cabecera
        Table CabeceraParrafo1 = new Table(new float[]{volumen * 4.3f, volumen * 0.7f});
        CabeceraParrafo1.addCell(getCell("H.C Nº:", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo1.addCell(getCell(oHistoriaclinica.getIdhistoria_clinica() + "", styleTextCenter, styleCell, subrayado));

        Table CabeceraParrafo2 = new Table(new float[]{volumen * 4.3f, volumen * 0.7f});
        CabeceraParrafo2.addCell(getCell("FECHA:", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo2.addCell(getCell(oHistoriaclinica.getFechainscripcion() + "", styleTextCenter, styleCell, subrayado));

        Table Cabecera = new Table(new float[]{volumen * 5f});
        Cabecera.addCell(new Cell().add(CabeceraParrafo1).addStyle(styleCell));
        Cabecera.addCell(new Cell().add(CabeceraParrafo2).addStyle(styleCell));
        //Fin Cabecera
        // ANAMNESIS
        Paragraph parrafoTitulo = new Paragraph("HISTORIA CLÍNICA").setFontSize(14).setFont(bold).setTextAlignment(TextAlignment.CENTER);
        Paragraph parrafoSubTitulo1 = new Paragraph("I.  ANAMNESIS").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);

        Table table1Parrafo1 = new Table(new float[]{volumen * 0.95f, volumen * 2.55f, volumen * 0.3f, volumen * 0.6f, volumen * 0.3f, volumen * 0.3f});
        table1Parrafo1.addCell(getCell("Nombres y Apellidos:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo1.addCell(getCell(opersona.getNombres_apellidos(), styleTextCenter, styleCell, subrayado));
        table1Parrafo1.addCell(getCell("Sexo:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo1.addCell(getCell(opersona.getSexo(), styleTextCenter, styleCell, subrayado));
        table1Parrafo1.addCell(getCell("Edad:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo1.addCell(getCell(edad + "", styleTextCenter, styleCell, subrayado));

        Table table1Parrafo2 = new Table(new float[]{volumen * 0.5f, volumen * 3.5f, volumen * 0.25f, volumen * 0.75f});
        table1Parrafo2.addCell(getCell("Domicilio:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo2.addCell(getCell(opersona.getDomicilio(), styleTextCenter, styleCell, subrayado));
        table1Parrafo2.addCell(getCell("DNI:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo2.addCell(getCell(opersona.getDni(), styleTextCenter, styleCell, subrayado));

        Table table1Parrafo3 = new Table(new float[]{volumen * 1f, volumen * 1.4f, volumen * 1f, volumen * 1.6f});
        table1Parrafo3.addCell(getCell("Fecha de Nacimiento:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo3.addCell(getCell(opersona.getFechaNacimiento() + "", styleTextCenter, styleCell, subrayado));
        table1Parrafo3.addCell(getCell("Lugar de Procedencia:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo3.addCell(getCell(opersona.getLugar_de_procedencia(), styleTextCenter, styleCell, subrayado));

        Table table1Parrafo4 = new Table(new float[]{volumen * 0.53f, volumen * 1.97f, volumen * 0.44f, volumen * 2.06f});
        table1Parrafo4.addCell(getCell("Ocupación:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo4.addCell(getCell(opersona.getOcupacion(), styleTextCenter, styleCell, subrayado));
        table1Parrafo4.addCell(getCell("Teléfono:", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo4.addCell(getCell(opersona.getTelefono(), styleTextCenter, styleCell, subrayado));

        Table table1Parrafo5 = new Table(new float[]{volumen * 1f, volumen * 4f});
        table1Parrafo5.addCell(getCell("Doctor (Operador)", styleTextLeft, styleCell, subrayadoNo).setFontColor(colorNegro));
        table1Parrafo5.addCell(getCell(oHistoriaclinica.getDoctor().getPersona().getNombres_apellidos(), styleTextCenter, styleCell, subrayado));

        Table tableInformacion = new Table(new float[]{volumen * 5});
        tableInformacion.addCell(new Cell().add(table1Parrafo1).addStyle(styleCell));
        tableInformacion.addCell(new Cell().add(table1Parrafo2).addStyle(styleCell));
        tableInformacion.addCell(new Cell().add(table1Parrafo3).addStyle(styleCell));
        tableInformacion.addCell(new Cell().add(table1Parrafo4).addStyle(styleCell));
        tableInformacion.addCell(new Cell().add(table1Parrafo5).addStyle(styleCell));

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
        TableMenorDeEdad.setMarginTop(10);

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

        //ODONTOGRAMA
        Table TableOdontograma = new Table(new float[]{volumen * 5});
        Image img = null;
        try {
            img = new Image(ImageDataFactory.create("images\\odontograma.png"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Historiaclinicapdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        Cell cellimag = new Cell().add(img.setAutoScale(true)).setBorder(Border.NO_BORDER);
        /* new SolidBorder(Color.BLACK,1*/
        TableOdontograma.addCell(cellimag);
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
        Table TableDiagnosticoParrafo2 = getTableField(oHistoriaclinica.getDiagnosticoPresuntivo(), "Diágnostico Presuntivo:", 1.05f, 3.95f, volumen, colorNegro, styleCell, styleTextLeft, palabraEnBlanco);
        Table TableDiagnosticoParrafo3 = getTableField(oHistoriaclinica.getDiagnosticoDefinitivo(), "Diágnostico Definitivo:", 1.05f, 3.95f, volumen, colorNegro, styleCell, styleTextLeft, palabraEnBlanco);
        Table TableDiagnostico = new Table(new float[]{volumen * 5});
        TableDiagnostico.addCell(new Cell().add(TableDiagnosticoParrafo1).addStyle(styleCell));
        TableDiagnostico.addCell(new Cell().add(TableDiagnosticoParrafo2).addStyle(styleCell));
        TableDiagnostico.addCell(new Cell().add(TableDiagnosticoParrafo3).addStyle(styleCell));
        //Fin DIAGNOSTICO

        //PLAN DE TRATAMIENTO - RECOMENDACIONES
        Paragraph parrafoSubTitulo6 = new Paragraph("VI. PLAN DE TRATAMIENTO - RECOMENDACIONES").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);
        Table TableTramientoRecomendaciones = getTable(oHistoriaclinica.getRecomendaciones(), volumen, palabraEnBlanco, styleCell, styleTextLeft);
        //Fin PLAN DE TRATAMIENTO - RECOMENDACIONES

        //PRONOSTICO
        Paragraph parrafoSubTitulo7 = new Paragraph("VII. PRONÓSTICO").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);
        Table TablePronostico = getTable(oHistoriaclinica.getPronostico(), volumen, palabraEnBlanco, styleCell, styleTextLeft);
        //fin PRONOSTICO

        //ALTA PACIENTE
        Paragraph parrafoSubTitulo8 = new Paragraph("VIII. ALTA PACIENTE").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);
        Table TableAlta = getTable(oHistoriaclinica.getAltaPaciente(), volumen, palabraEnBlanco, styleCell, styleTextLeft);
        //Fin ALTA PACIENTE

        /*----Fin Contenido del documento  página 1------*/
 /*--------Contenido del documento página 2--------*/
        Table TableTratamiento = new Table(new float[]{volumen * 0.6f, volumen * 3.1f, volumen * 0.8f, volumen * 0.5f});
        TableTratamiento.addCell(new Cell().add(new Paragraph("FECHA").setFont(bold).addStyle(styleTextCenter)));
        TableTratamiento.addCell(new Cell().add(new Paragraph("PROCEDIMIENTO").setFont(bold).addStyle(styleTextCenter)));
        TableTratamiento.addCell(new Cell().add(new Paragraph("CANCELADO").setFont(bold).addStyle(styleTextCenter)));
        TableTratamiento.addCell(new Cell().add(new Paragraph("MONTO").setFont(bold).addStyle(styleTextCenter)));
        int montoTotal = 0;
        for (Tratamiento tratamiento : olistTratamiento) {
            TableTratamiento.addCell(new Cell().add(new Paragraph(tratamiento.getFechaRealizada() + "").addStyle(styleTextCenter)));
            TableTratamiento.addCell(new Cell().add(new Paragraph(tratamiento.getTratamiento()).addStyle(styleTextLeft)));
            TableTratamiento.addCell(new Cell().add(new Paragraph(tratamiento.isCancelado() ? "SI" : "NO").addStyle(styleTextCenter)));
            TableTratamiento.addCell(new Cell().add(new Paragraph(tratamiento.getMonto() + "").addStyle(styleTextCenter)));
            montoTotal = montoTotal + tratamiento.getMonto();
        }
        int contadorEspacio = 35 - olistTratamiento.size();
        for (int i = 0; i < contadorEspacio; i++) {
            TableTratamiento.addCell(new Cell().add(palabraEnBlancoLimpio.addStyle(styleTextCenter)));
            TableTratamiento.addCell(new Cell().add(new Paragraph("").addStyle(styleTextLeft)));
            TableTratamiento.addCell(new Cell().add(new Paragraph("").addStyle(styleTextCenter)));
            TableTratamiento.addCell(new Cell().add(new Paragraph("").addStyle(styleTextCenter)));
            //montoTotal = montoTotal + tratamiento.getMonto();
        }

        TableTratamiento.addCell(new Cell(1, 3).add(new Paragraph("MONTO TOTAL ").addStyle(styleTextCenter).setFont(bold)));
        TableTratamiento.addCell(new Cell().add(new Paragraph(montoTotal + "").addStyle(styleTextCenter)));

        /* Cuerpo del documentos*/
        document.add(Cabecera);
        document.add(parrafoTitulo);
        document.add(parrafoSubTitulo1);
        document.add(tableInformacion);
        if (edad < 18) {
            document.add(TableMenorDeEdad);
        } else {
            TableCasoDeEmergencia.setMarginTop(71);
        }
        document.add(TableCasoDeEmergencia);
        document.add(TableMotivoDeConsulta);

        document.add(TableOdontograma);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        document.add(parrafoSubTitulo2);
        document.add(TableEnfermedadActual);

        document.add(parrafoSubTitulo3);
        document.add(TableAntecedentes);

        //document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(parrafoSubTitulo4);
        document.add(TableExploracionFisica);

        document.add(parrafoSubTitulo5);
        document.add(TableDiagnostico);

        document.add(parrafoSubTitulo6);
        document.add(TableTramientoRecomendaciones);

        document.add(parrafoSubTitulo7);
        document.add(TablePronostico);

        document.add(parrafoSubTitulo8);
        document.add(TableAlta);

        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(TableTratamiento);

        document.close();
        /*----Fin Cuerpo del documentos-----*/
    }

    public static Table getTable(String cadena, int volumen, Paragraph palabraEnBlanco, Style styleCell, Style styleTextLeft) {
        Table TablePrincipal = new Table(new float[]{volumen * 5});
        Table TableParrafo = new Table(new float[]{volumen * 5});
        Paragraph oParagrah;
        int numCharacteres = 108;
        int iteracion = cadena.length() / numCharacteres;
        for (int i = 0; i < iteracion; i++) {
            oParagrah = new Paragraph(cadena.substring(i * numCharacteres, (i + 1) * numCharacteres));
            Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
            TableParrafo.addCell(cellParrafo1);
        }
        oParagrah = cadena.isEmpty() ? palabraEnBlanco : new Paragraph(cadena.substring(cadena.length() - cadena.length() % numCharacteres, cadena.length()));;
        Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
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
        Paragraph oParagrah;
        int subnumCharacteres = 75;
        int numCharacteres = 100;
        int iteracion = cadena.length() / numCharacteres;
        int division = cadena.length() / subnumCharacteres;
        boolean aux = division == 0;
        Cell cellDi = new Cell().add(new Paragraph(titulo).setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableAtributo.addCell(cellDi);
        Paragraph oParagra = cadena.length() == 0 ? palabraEnBlanco : new Paragraph(aux ? cadena : cadena.substring(0 * numCharacteres, (0 + 1) * subnumCharacteres));
        Cell cellParraf = new Cell().add(oParagra.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableAtributo.addCell(cellParraf);
        TableParrafo.addCell(new Cell().add(TableAtributo).addStyle(styleCell));
        for (int i = 1; i < iteracion; i++) {
            oParagrah = new Paragraph(cadena.substring(i * numCharacteres, (i + 1) * numCharacteres));
            Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
            TableParrafo.addCell(cellParrafo1);
        }
        oParagrah = aux ? palabraEnBlanco : new Paragraph(cadena.substring(cadena.length() - cadena.length() % numCharacteres, cadena.length()));;
        Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableParrafo.addCell(cellParrafo1);
        if (iteracion == 1 || iteracion == 0) {
            Cell cellraya2 = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
            TableParrafo.addCell(cellraya2);
        }
        return TableParrafo;
    }

    public static Cell getCell(String palabra, Style posicion, Style border, Border subrayado) {
        Color colorBlanco = Color.WHITE;
        Paragraph palabraEnBlanco = new Paragraph(".").setFontColor(colorBlanco).setBorderBottom(new SolidBorder(0.5f));
        Paragraph Parapalabra = palabra.isEmpty() ? palabraEnBlanco : new Paragraph(palabra);

        return new Cell().add(Parapalabra.setBorderBottom(subrayado)).addStyle(posicion).addStyle(border);
    }

}
