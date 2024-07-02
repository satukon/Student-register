package com.satumaarit;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.awt.Desktop;

/**
 * Class to handle the generation of pdf reports.
 * @author satu
 */
public class PdfGenerator {

    /**
     * Method to generate the pdf report from the list of records of one student.
     * @param recordList a student's list of records
     * @param pdfFilePath the filepath where to generate the pdf report
     */
    public void generatePdf(List<StudentRecord> recordList, String pdfFilePath) {
        StudentRecord record = recordList.get(0);
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
            document.open();

            // Add general information to document
            document.add(new Paragraph("Our honoured student"));
            document.add(new Paragraph(" "));
            Paragraph nameparagraph = new Paragraph(record.getStudent().getFirstname() + " " + record.getStudent().getLastname());
            nameparagraph.getFont().setSize(16);
            document.add(nameparagraph);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("has completed the following courses:"));
            document.add(new Paragraph(" "));

            // Generate table with 4 columns
            PdfPTable table = new PdfPTable(4);
            float[] columnWidths = {3f, 0.5f, 1f, 0.5f};
            table.setWidths(columnWidths);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add header cells to table
            PdfPCell cell1 = new PdfPCell(new Paragraph("Course"));
            cell1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Paragraph("Credits"));
            cell2.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Paragraph("Date of evaluation"));
            cell3.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Paragraph("Grade"));
            cell4.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell4);

            // Add an empty row to table for spacing
            PdfPCell emptyCell = new PdfPCell(new Paragraph(" "));
            emptyCell.setBorder(PdfPCell.NO_BORDER);
            emptyCell.setColspan(4); // Span all 4 columns
            table.addCell(emptyCell);

            // Add data to the table
            for (StudentRecord sr : recordList) {
                PdfPCell cell = new PdfPCell(new Phrase(sr.getCourse().getName()));
                cell.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell);

                PdfPCell cell_1 = new PdfPCell(new Phrase(String.valueOf(sr.getCourse().getCredits())));
                cell_1.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell_1);

                PdfPCell cell_2 = new PdfPCell(new Phrase(sr.getEvaluationdate()));
                cell_2.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell_2);

                PdfPCell cell_3 = new PdfPCell(new Phrase(String.valueOf(sr.getGrading())));
                cell_3.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell_3);
            }
            // Add table and additional information to document
            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total number of credits completed: " + countTotalNumberOfCredits(recordList) + " credits"));
            document.add(new Paragraph("Average of grades: " + countAverageOfGrades(recordList)));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Satu's School of Awesome recognizes this effort!"));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to open the generated pdf report.
     * @param filePath filepath of the generated pdf
     */
    public void openPdf(String filePath) {
        // Check if the Desktop class is supported on the platform
        if (!Desktop.isDesktopSupported()) {
            System.err.println("The Desktop class is not supported. Couldn't open the pdf report.");
            return;
        }

        try {
            File pdfFile = new File(filePath);

            if (pdfFile.exists()) {
                // Open the PDF file
                Desktop.getDesktop().open(pdfFile);
            }

        } catch (IOException e) {
            System.err.println("Error happened while generating the pdf report: " + e);
        }
    }

    /**
     * Method to count the average of grades of a student.
     * @param recordList a student's list of records
     * @return average of grades formatted to 2 decimals
     */
    public String countAverageOfGrades(List<StudentRecord> recordList) {
        double numberOfRecords = 0;
        double sum = 0;
        double average;

        for (StudentRecord sr : recordList) {
            if (sr.getGrading() > 0) {
                numberOfRecords = numberOfRecords + 1;
            }
        }

        for (StudentRecord obj : recordList) {
            sum = sum + obj.getGrading();
        }

        average = sum / numberOfRecords;
        DecimalFormat df = new DecimalFormat("0.0");

        return df.format(average);
    }

    /**
     * Method to count the total number of course credits completed by a student.
     * @param recordList a student's list of records
     * @return the total number of credits a student has completed
     */
    public int countTotalNumberOfCredits(List<StudentRecord> recordList) {
        int totalNumberOfCredits = 0;

        for (StudentRecord sr : recordList) {
            totalNumberOfCredits = totalNumberOfCredits + sr.getCourse().getCredits();
        }

        return totalNumberOfCredits;
    }
}