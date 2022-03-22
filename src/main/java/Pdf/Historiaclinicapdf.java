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
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.property.AreaBreakType;
import controller.App;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 *
 * @author yalle
 */
public class Historiaclinicapdf {

    public static void ImprimirHistoriaClinica(Persona opersona) throws IOException {
        Paciente opaciente = (Paciente) App.jpa.createQuery("select p from Paciente p where idpersona=" + opersona.getIdpersona()).getSingleResult();
        Historia_clinica oHistoriaclinica = (Historia_clinica) App.jpa.createQuery("select p from Historia_clinica p where idpaciente=" + opaciente.getIdpaciente()).getSingleResult();
        List<Paciente_Enfermedad> listPaciente_Enfermedad = App.jpa.createQuery("select p from Paciente_Enfermedad p where idpaciente=" + opaciente.getIdpaciente()).getResultList();
        List<Paciente_Pregunta> listPaciente_PreguntaIsMujer = App.jpa.createQuery("select p from Paciente_Pregunta p where idpaciente=" + opaciente.getIdpaciente() + " and ismujer=true").getResultList();
        List<Paciente_Pregunta> listPaciente_PreguntaIsHombre = App.jpa.createQuery("select p from Paciente_Pregunta p where idpaciente=" + opaciente.getIdpaciente() + " and ismujer=false").getResultList();
        List<Pregunta> listPreguntaIsMujer = App.jpa.createQuery("select p from Pregunta p where isMujer=true ORDER BY idpregunta ASC").getResultList();
        List<Pregunta> listPreguntaIsHombre = App.jpa.createQuery("select p from Pregunta p where isMujer=false  ORDER BY idpregunta ASC").getResultList();
        List<Tratamiento> olistTratamiento = App.jpa.createQuery("select p from Tratamiento p where idpersona= " + opersona.getIdpersona() + " and flag = false order by idtratamiento DESC").setMaxResults(10).getResultList();
        Period period = Period.between(opersona.getFechaNacimiento(), LocalDate.now());
        long edad = period.getYears();

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

        /*---------------------Color----------*/
        Color prueba = new DeviceRgb(0, 0, 0);
        Color colorAzul = Color.BLUE;
        Color colorSubtitulo = Color.BLACK;
        Color colorNegro = Color.ORANGE;
        Color colorBlanco = Color.WHITE;

        /*-----------------Palabras default-----------*/
        String palabra1 = "desc.";
        String palabra2 = "no presenta";

        /*----------------Palabras vacías-------------*/
        Paragraph palabraEnBlanco = new Paragraph().setFontColor(colorBlanco);
        Paragraph nombreTutor = opersona.getTutorNombre().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(opersona.getTutorNombre());
        Paragraph dniTutor = opersona.getTutorDni().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(opersona.getTutorDni());
        Paragraph telefonoTutor = opersona.getTutorTelefono().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(opersona.getTutorTelefono());

        Paragraph emergenciaNombre = opaciente.getEmergenciaNombre().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(opaciente.getEmergenciaNombre());
        Paragraph emergenciaParentesco = opaciente.getEmergenciaParentesco().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(opaciente.getEmergenciaParentesco());
        Paragraph emergenciaTelefono = opaciente.getEmergenciaTelefono().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(opaciente.getEmergenciaTelefono());

        Paragraph MOTIVOCONSULTA = oHistoriaclinica.getMotivoConsulta().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(oHistoriaclinica.getMotivoConsulta() + "s");

        Paragraph SignosSintomas = opaciente.getSintomasEnfermedadActual().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(opaciente.getSintomasEnfermedadActual());
        Paragraph TiempoEnfermedad = opaciente.getTiempoEnfermedadActual().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(opaciente.getTiempoEnfermedadActual());

        Paragraph otrasEnfermedades = opaciente.getOtrasEnfermedades().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(opaciente.getOtrasEnfermedades());
        Paragraph antecedentesFamiliares = opaciente.getAntecedentesFamiliares().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(opaciente.getAntecedentesFamiliares());

        Paragraph signosVitales = oHistoriaclinica.getSignosVitales().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(oHistoriaclinica.getSignosVitales());
        Paragraph saturacionOxigeno = oHistoriaclinica.getSaturacionOxigeno().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(oHistoriaclinica.getSaturacionOxigeno());
        Paragraph PA = oHistoriaclinica.getPA().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(oHistoriaclinica.getPA());
        Paragraph FC = oHistoriaclinica.getFC().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(oHistoriaclinica.getFC());
        Paragraph Temperatura = oHistoriaclinica.getTemperatura().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(oHistoriaclinica.getTemperatura());
        Paragraph FR = oHistoriaclinica.getFR().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(oHistoriaclinica.getFR());
        Paragraph examenClinicoGeneral = oHistoriaclinica.getExamenClinicoGeneral().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(oHistoriaclinica.getExamenClinicoGeneral());

        Paragraph recomendaciones = oHistoriaclinica.getRecomendaciones().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(oHistoriaclinica.getRecomendaciones());

        Paragraph pronostico = oHistoriaclinica.getPronostico().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(oHistoriaclinica.getPronostico());
        Paragraph altaPaciente = oHistoriaclinica.getAltaPaciente().isEmpty() ? palabraEnBlanco.add(".") : new Paragraph(oHistoriaclinica.getAltaPaciente());

        /*---------FIN----Palabras vacías-------------*/

 /* Contenido del documento  página 1*/
        //table raya
        Table TableRaya = new Table(new float[]{volumen * 5});
        Cell cellraya = new Cell().add(new Paragraph(".").setBorderBottom(new SolidBorder(1f))).addStyle(styleTextRight).addStyle(styleCell);
        TableRaya.addCell(cellraya);
        //Cabecera
        Table CabeceraParrafo1 = new Table(new float[]{volumen * 4.3f, volumen * 0.7f});
        Cell cellCabeceraParrafo1 = new Cell().add(new Paragraph("H.C N°: ")).addStyle(styleTextRight).addStyle(styleCell);
        CabeceraParrafo1.addCell(cellCabeceraParrafo1);
        cellCabeceraParrafo1 = new Cell().add(new Paragraph("" + oHistoriaclinica.getIdhistoria_clinica()).setBorderBottom(new SolidBorder(1f))).addStyle(styleTextCenter).addStyle(styleCell);
        CabeceraParrafo1.addCell(cellCabeceraParrafo1);

        Table CabeceraParrafo2 = new Table(new float[]{volumen * 4.3f, volumen * 0.7f});
        Cell cellCabeceraParrafo2 = new Cell().add(new Paragraph("FECHA: ")).addStyle(styleTextRight).addStyle(styleCell);
        CabeceraParrafo2.addCell(cellCabeceraParrafo2);
        cellCabeceraParrafo2 = new Cell().add(new Paragraph(oHistoriaclinica.getFechainscripcion().toString()).setBorderBottom(new SolidBorder(1f))).addStyle(styleTextCenter).addStyle(styleCell);
        CabeceraParrafo2.addCell(cellCabeceraParrafo2);

        Table Cabecera = new Table(new float[]{volumen * 5f});
        Cabecera.addCell(new Cell().add(CabeceraParrafo1).addStyle(styleCell));
        Cabecera.addCell(new Cell().add(CabeceraParrafo2).addStyle(styleCell));
        //Fin Cabecera
        // ANAMNESIS
        Paragraph parrafoTitulo = new Paragraph("HISTORIA CLÍNICA").setFontSize(14).setFont(bold).setTextAlignment(TextAlignment.CENTER);
        Paragraph parrafoSubTitulo1 = new Paragraph("I.  ANAMNESIS").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);

        Table table1Parrafo1 = new Table(new float[]{volumen * 0.95f, volumen * 2.55f, volumen * 0.3f, volumen * 0.6f, volumen * 0.3f, volumen * 0.3f});
        Cell cell1Parrafo1 = new Cell().add(new Paragraph("Nombres y Apellidos: ").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo1.addCell(cell1Parrafo1);
        cell1Parrafo1 = new Cell().add(new Paragraph(opersona.getNombres_apellidos()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo1.addCell(cell1Parrafo1);
        cell1Parrafo1 = new Cell().add(new Paragraph("Sexo: ").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo1.addCell(cell1Parrafo1);
        cell1Parrafo1 = new Cell().add(new Paragraph(opersona.getSexo()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo1.addCell(cell1Parrafo1);
        cell1Parrafo1 = new Cell().add(new Paragraph("Edad: ").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo1.addCell(cell1Parrafo1);
        cell1Parrafo1 = new Cell().add(new Paragraph(edad + "").setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo1.addCell(cell1Parrafo1);

        Table table1Parrafo2 = new Table(new float[]{volumen * 0.5f, volumen * 3.5f, volumen * 0.25f, volumen * 0.75f});
        Cell cell1Parrafo2 = new Cell().add(new Paragraph("Domicilio: ").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo2.addCell(cell1Parrafo2);
        cell1Parrafo2 = new Cell().add(new Paragraph(opersona.getDomicilio()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo2.addCell(cell1Parrafo2);
        cell1Parrafo2 = new Cell().add(new Paragraph("DNI:").setFontColor(colorNegro)).addStyle(styleTextLeft).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo2.addCell(cell1Parrafo2);
        cell1Parrafo2 = new Cell().add(new Paragraph(opersona.getDni()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo2.addCell(cell1Parrafo2);

        Table table1Parrafo3 = new Table(new float[]{volumen * 1f, volumen * 1.4f, volumen * 1f, volumen * 1.6f});
        Cell cell1Parrafo3 = new Cell().add(new Paragraph("Fecha de Nacimiento:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo3.addCell(cell1Parrafo3);
        cell1Parrafo3 = new Cell().add(new Paragraph(opersona.getFechaNacimiento().toString()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo3.addCell(cell1Parrafo3);
        cell1Parrafo3 = new Cell().add(new Paragraph("Lugar de Procedencia:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo3.addCell(cell1Parrafo3);
        cell1Parrafo3 = new Cell().add(new Paragraph(opersona.getLugar_de_procedencia()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo3.addCell(cell1Parrafo3);

        Table table1Parrafo4 = new Table(new float[]{volumen * 0.53f, volumen * 1.97f, volumen * 0.44f, volumen * 2.06f});
        Cell cell1Parrafo4 = new Cell().add(new Paragraph("Ocupación:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        table1Parrafo4.addCell(cell1Parrafo4);
        cell1Parrafo4 = new Cell().add(new Paragraph(opersona.getOcupacion()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        table1Parrafo4.addCell(cell1Parrafo4);
        cell1Parrafo4 = new Cell().add(new Paragraph("Teléfono:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
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
        Cell cellTableMenorDeEdadParrafo1 = new Cell().add(new Paragraph("Para pacientes menores de edad:").setFontColor(colorSubtitulo).setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo1.addCell(cellTableMenorDeEdadParrafo1);

        Table TableMenorDeEdadParrafo2 = new Table(new float[]{volumen * 0.95f, volumen * 1.25f, volumen * 0.3f, volumen * 1f, volumen * 0.43f, volumen * 1.08f});
        Cell cellTableMenorDeEdadParrafo2 = new Cell().add(new Paragraph("Nombre del tutor:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo2.addCell(cellTableMenorDeEdadParrafo2);
        cellTableMenorDeEdadParrafo2 = new Cell().add(nombreTutor.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableMenorDeEdadParrafo2.addCell(cellTableMenorDeEdadParrafo2);
        cellTableMenorDeEdadParrafo2 = new Cell().add(new Paragraph("DNI:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo2.addCell(cellTableMenorDeEdadParrafo2);
        cellTableMenorDeEdadParrafo2 = new Cell().add(dniTutor.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableMenorDeEdadParrafo2.addCell(cellTableMenorDeEdadParrafo2);
        cellTableMenorDeEdadParrafo2 = new Cell().add(new Paragraph("Telefono.:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableMenorDeEdadParrafo2.addCell(cellTableMenorDeEdadParrafo2);
        cellTableMenorDeEdadParrafo2 = new Cell().add(telefonoTutor.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableMenorDeEdadParrafo2.addCell(cellTableMenorDeEdadParrafo2);

        Table TableMenorDeEdad = new Table(new float[]{volumen * 5});
        Cell cellTableMenorDeEdad = new Cell().add(TableMenorDeEdadParrafo1).addStyle(styleCell);
        TableMenorDeEdad.addCell(cellTableMenorDeEdad);
        cellTableMenorDeEdad = new Cell().add(TableMenorDeEdadParrafo2).addStyle(styleCell);
        TableMenorDeEdad.addCell(cellTableMenorDeEdad);
        TableMenorDeEdad.setMarginTop(10);

        Table TableCasoDeEmergenciaParrafo1 = new Table(new float[]{volumen * 5});
        Cell cellTableCasoDeEmergenciaParrafo1 = new Cell().add(new Paragraph("En caso de emergencia comunicarse con").setFontColor(colorSubtitulo).setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft);
        TableCasoDeEmergenciaParrafo1.addCell(cellTableCasoDeEmergenciaParrafo1);

        Table TableCasoDeEmergenciaParrafo2 = new Table(new float[]{volumen * 0.45f, volumen * 1.25f, volumen * 0.55f, volumen * 1.25f, volumen * 0.45f, volumen * 1.05f});
        Cell cellTableCasoDeEmergenciaParrafo2 = new Cell().add(new Paragraph("Nombre:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableCasoDeEmergenciaParrafo2.addCell(cellTableCasoDeEmergenciaParrafo2);
        cellTableCasoDeEmergenciaParrafo2 = new Cell().add(emergenciaNombre.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableCasoDeEmergenciaParrafo2.addCell(cellTableCasoDeEmergenciaParrafo2);
        cellTableCasoDeEmergenciaParrafo2 = new Cell().add(new Paragraph("Parentesco:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableCasoDeEmergenciaParrafo2.addCell(cellTableCasoDeEmergenciaParrafo2);
        cellTableCasoDeEmergenciaParrafo2 = new Cell().add(emergenciaParentesco.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableCasoDeEmergenciaParrafo2.addCell(cellTableCasoDeEmergenciaParrafo2);
        cellTableCasoDeEmergenciaParrafo2 = new Cell().add(new Paragraph("Telefono:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableCasoDeEmergenciaParrafo2.addCell(cellTableCasoDeEmergenciaParrafo2);
        cellTableCasoDeEmergenciaParrafo2 = new Cell().add(emergenciaTelefono.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableCasoDeEmergenciaParrafo2.addCell(cellTableCasoDeEmergenciaParrafo2);

        Table TableCasoDeEmergencia = new Table(new float[]{volumen * 5});
        Cell cellTableCasoDeEmergencia = new Cell().add(TableCasoDeEmergenciaParrafo1).addStyle(styleCell);
        TableCasoDeEmergencia.addCell(cellTableCasoDeEmergencia);
        cellTableCasoDeEmergencia = new Cell().add(TableCasoDeEmergenciaParrafo2).addStyle(styleCell);
        TableCasoDeEmergencia.addCell(cellTableCasoDeEmergencia);

        Table TableMotivoDeConsultaParrafo1 = new Table(new float[]{volumen * 1, volumen * 4});
        Cell cellTableMotivoDeConsultaParrafo1 = new Cell().add(new Paragraph("Motivo de Consulta: ").setFontColor(colorNegro).setFont(bold)).addStyle(styleCell).addStyle(styleTextLeft);
        TableMotivoDeConsultaParrafo1.addCell(cellTableMotivoDeConsultaParrafo1);
        cellTableMotivoDeConsultaParrafo1 = new Cell().add(MOTIVOCONSULTA.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableMotivoDeConsultaParrafo1.addCell(cellTableMotivoDeConsultaParrafo1);

        Table TableMotivoDeConsulta = new Table(new float[]{volumen * 5});
        Cell cellTableMotivoDeConsulta = new Cell().add(TableMotivoDeConsultaParrafo1).addStyle(styleCell);
        TableMotivoDeConsulta.addCell(cellTableMotivoDeConsulta);
        //Fin ANAMNESIS

        //ENFERMEDAD ACTUAL
        Paragraph parrafoSubTitulo2 = new Paragraph("II.  ENFERMEDAD ACTUAL").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);
        Table TableEnfermedadActual = new Table(new float[]{volumen * 1.3f, volumen * 3.7f});
        Cell cellTableEnfermedadActual = new Cell().add(new Paragraph("Signo y síntomas principales:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableEnfermedadActual.addCell(cellTableEnfermedadActual);
        cellTableEnfermedadActual = new Cell().add(SignosSintomas.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableEnfermedadActual.addCell(cellTableEnfermedadActual);
        cellTableEnfermedadActual = new Cell().add(new Paragraph("Tiempo de la enfermedad:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableEnfermedadActual.addCell(cellTableEnfermedadActual);
        cellTableEnfermedadActual = new Cell().add(TiempoEnfermedad.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableEnfermedadActual.addCell(cellTableEnfermedadActual);
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
            TableAntecedentesEnfermedades.addCell(new Cell().add(new Paragraph(cont + ": " + paciente_Enfermedad.getEnfermedad().getNombre() + detalle)).addStyle(styleCell).addStyle(styleTextLeft));

        }

        //Otras enfermedades
        Table TableOtrasEnfermedades = new Table(new float[]{volumen * 0.95f, volumen * 4.05f});
        Cell cellTableOtrasEnfermedades = new Cell().add(new Paragraph("Otras enfermedades: ")).addStyle(styleCell).addStyle(styleTextLeft);
        TableOtrasEnfermedades.addCell(cellTableOtrasEnfermedades);
        cellTableOtrasEnfermedades = new Cell().add(otrasEnfermedades.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
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
            Cell cellTableAntecedentePreguntas = new Cell().add(new Paragraph(contP + ": " + pregunta.getTextopregunta()).setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
            TableAntecedentesPreguntasParrafo.addCell(cellTableAntecedentePreguntas);
            cellTableAntecedentePreguntas = new Cell().add(new Paragraph(contexto).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
            TableAntecedentesPreguntasParrafo.addCell(cellTableAntecedentePreguntas);
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
            TableAntecedentesPreguntasMujerParrafo.addCell(new Cell().add(new Paragraph(contP + ": " + pregunta.getTextopregunta()).setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft));
            TableAntecedentesPreguntasMujerParrafo.addCell(new Cell().add(new Paragraph(contexto).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft));
            TableAntecedentesPreguntasMujer.addCell(new Cell().add(TableAntecedentesPreguntasMujerParrafo).addStyle(styleCell));

        }
        Table TableAntecedente = new Table(new float[]{volumen * 1.1f, volumen * 3.9f});
        Cell cellTableAntecedente = new Cell().add(new Paragraph("Antecedentes familiares: ")).addStyle(styleCell).addStyle(styleTextLeft);
        TableAntecedente.addCell(cellTableAntecedente);
        cellTableAntecedente = new Cell().add(antecedentesFamiliares.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextCenter);
        TableAntecedente.addCell(cellTableAntecedente);

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
        Cell cellExploracionFisicaParrafo1 = new Cell().add(new Paragraph("Signos vitales:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo1.addCell(cellExploracionFisicaParrafo1);
        cellExploracionFisicaParrafo1 = new Cell().add(signosVitales.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo1.addCell(cellExploracionFisicaParrafo1);
        cellExploracionFisicaParrafo1 = new Cell().add(new Paragraph("Saturación de oxígeno:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo1.addCell(cellExploracionFisicaParrafo1);
        cellExploracionFisicaParrafo1 = new Cell().add(saturacionOxigeno.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo1.addCell(cellExploracionFisicaParrafo1);
        //0
        Table TableExploracionFisicaParrafo2 = new Table(new float[]{volumen * 0.25f, volumen * 1f, volumen * 0.25f,
            volumen * 1f, volumen * 0.65f, volumen * 0.6f, volumen * 0.25f, volumen * 1f});
        Cell cellExploracionFisicaParrafo2 = new Cell().add(new Paragraph("P.A:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo2.addCell(cellExploracionFisicaParrafo2);
        cellExploracionFisicaParrafo2 = new Cell().add(PA.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo2.addCell(cellExploracionFisicaParrafo2);
        cellExploracionFisicaParrafo2 = new Cell().add(new Paragraph("F.C:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo2.addCell(cellExploracionFisicaParrafo2);
        cellExploracionFisicaParrafo2 = new Cell().add(FC.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo2.addCell(cellExploracionFisicaParrafo2);
        cellExploracionFisicaParrafo2 = new Cell().add(new Paragraph("Temperatura:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo2.addCell(cellExploracionFisicaParrafo2);
        cellExploracionFisicaParrafo2 = new Cell().add(Temperatura.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo2.addCell(cellExploracionFisicaParrafo2);
        cellExploracionFisicaParrafo2 = new Cell().add(new Paragraph("F.R:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo2.addCell(cellExploracionFisicaParrafo2);
        cellExploracionFisicaParrafo2 = new Cell().add(FR.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo2.addCell(cellExploracionFisicaParrafo2);

        Table TableExploracionFisicaParrafo3 = new Table(new float[]{volumen * 1.1f, volumen * 3.9f});
        Cell cellExploracionFisicaParrafo3 = new Cell().add(new Paragraph("Examen clínico general:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo3.addCell(cellExploracionFisicaParrafo3);
        cellExploracionFisicaParrafo3 = new Cell().add(examenClinicoGeneral.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableExploracionFisicaParrafo3.addCell(cellExploracionFisicaParrafo3);

        Table TableExploracionFisica = new Table(new float[]{volumen * 5});
        TableExploracionFisica.addCell(new Cell().add(TableExploracionFisicaParrafo1).addStyle(styleCell));
        TableExploracionFisica.addCell(new Cell().add(TableExploracionFisicaParrafo2).addStyle(styleCell));
        TableExploracionFisica.addCell(new Cell().add(TableExploracionFisicaParrafo3).addStyle(styleCell));
        TableExploracionFisica.addCell(new Cell().add(TableRaya).addStyle(styleCell));
        //Fin EXPLORACIÓN FÍSICA

        //DIAGNOSTICO
        Paragraph parrafoSubTitulo5 = new Paragraph("V.  DIAGNÓSTICO").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);

        Table TableDiagnosticoParrafo1 = new Table(new float[]{volumen * 0.6f, volumen * 4.4f});
        Cell cellDiagnosticoParrafo1 = new Cell().add(new Paragraph("Diagnóstico:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableDiagnosticoParrafo1.addCell(cellDiagnosticoParrafo1);
        cellDiagnosticoParrafo1 = new Cell().add(new Paragraph(oHistoriaclinica.getDiagnosticoCIE10()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableDiagnosticoParrafo1.addCell(cellDiagnosticoParrafo1);

        Table TableDiagnosticoParrafo2 = new Table(new float[]{volumen * 1.04f, volumen * 3.96f});
        Cell cellDiagnosticoParrafo2 = new Cell().add(new Paragraph("Diagnóstico Presuntivo:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableDiagnosticoParrafo2.addCell(cellDiagnosticoParrafo2);
        cellDiagnosticoParrafo2 = new Cell().add(new Paragraph(oHistoriaclinica.getDiagnosticoPresuntivo()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableDiagnosticoParrafo2.addCell(cellDiagnosticoParrafo2);

        Table TableDiagnosticoParrafo3 = new Table(new float[]{volumen * 1.04f, volumen * 3.96f});
        Cell cellDiagnosticoParrafo3 = new Cell().add(new Paragraph("Diagnóstico Definitivo:").setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableDiagnosticoParrafo3.addCell(cellDiagnosticoParrafo3);
        cellDiagnosticoParrafo3 = new Cell().add(new Paragraph(oHistoriaclinica.getDiagnosticoDefinitivo()).setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableDiagnosticoParrafo3.addCell(cellDiagnosticoParrafo3);

        Table TableDiagnostico = new Table(new float[]{volumen * 5});
        TableDiagnostico.addCell(new Cell().add(TableDiagnosticoParrafo1).addStyle(styleCell));
        TableDiagnostico.addCell(new Cell().add(TableRaya).addStyle(styleCell));
        TableDiagnostico.addCell(new Cell().add(TableRaya).addStyle(styleCell));
        TableDiagnostico.addCell(new Cell().add(TableDiagnosticoParrafo2).addStyle(styleCell));
        TableDiagnostico.addCell(new Cell().add(TableRaya).addStyle(styleCell));
        TableDiagnostico.addCell(new Cell().add(TableDiagnosticoParrafo3).addStyle(styleCell));
        TableDiagnostico.addCell(new Cell().add(TableRaya).addStyle(styleCell));
        //Fin DIAGNOSTICO

        //PLAN DE TRATAMIENTO - RECOMENDACIONES
        Paragraph parrafoSubTitulo6 = new Paragraph("VI. PLAN DE TRATAMIENTO - RECOMENDACIONES").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);

        Table TableTramientoRecomendacionesParrafo1 = new Table(new float[]{volumen * 5});
        Cell cellTramientoRecomendacionesParrafo1 = new Cell().add(recomendaciones.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableTramientoRecomendacionesParrafo1.addCell(cellTramientoRecomendacionesParrafo1);

        Table TableTramientoRecomendaciones = new Table(new float[]{volumen * 5});
        TableTramientoRecomendaciones.addCell(new Cell().add(TableTramientoRecomendacionesParrafo1).addStyle(styleCell));
        TableTramientoRecomendaciones.addCell(new Cell().add(TableRaya).addStyle(styleCell));
        TableTramientoRecomendaciones.addCell(new Cell().add(TableRaya).addStyle(styleCell));
        //Fin PLAN DE TRATAMIENTO - RECOMENDACIONES

        //PRONOSTICO
        Paragraph parrafoSubTitulo7 = new Paragraph("VII. PRONÓSTICO").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);

        Table TablePronosticoParrafo1 = new Table(new float[]{volumen * 5});
        Cell cellPronosticoParrafo1 = new Cell().add(pronostico.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TablePronosticoParrafo1.addCell(cellPronosticoParrafo1);

        Table TablePronostico = new Table(new float[]{volumen * 5});
        TablePronostico.addCell(new Cell().add(TablePronosticoParrafo1).addStyle(styleCell));
        //fin PRONOSTICO

        //ALTA PACIENTE
        Paragraph parrafoSubTitulo8 = new Paragraph("VIII. ALTA PACIENTE").setFontSize(10).setFontColor(colorAzul).setFont(bold).addStyle(styleTextLeft);

        Table TableAltaParrafo1 = new Table(new float[]{volumen * 5});
        Cell cellAltaParrafo1 = new Cell().add(altaPaciente.setBorderBottom(new SolidBorder(1f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableAltaParrafo1.addCell(cellAltaParrafo1);

        Table TableAlta = new Table(new float[]{volumen * 5});
        TableAlta.addCell(new Cell().add(TableAltaParrafo1).addStyle(styleCell));
        //Fin ALTA PACIENTE

        /*----Fin Contenido del documento  página 1------*/
 /*--------Contenido del documento página 2--------*/
        Table TableTratamiento = new Table(new float[]{volumen * 0.6f, volumen * 3.1f, volumen * 0.8f, volumen * 0.5f});
        TableTratamiento.addCell(new Cell().add(new Paragraph("FECHA").setFont(bold).addStyle(styleTextCenter)));
        TableTratamiento.addCell(new Cell().add(new Paragraph("TRATAMIENTO").setFont(bold).addStyle(styleTextCenter)));
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

        document.add(parrafoSubTitulo7);
        document.add(TablePronostico);

        document.add(parrafoSubTitulo8);
        document.add(TableAlta);

        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(TableTratamiento);

        document.close();
        /*----Fin Cuerpo del documentos-----*/
    }

}
