package com.dsv.rps.pdf;

import com.dsv.rps.bean.Invoice;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

public class PdfTest {

	
	public static void main(String[] args) {

		// Creating a PdfWriter
		try {
			Invoice testInvoice = Invoice.getTest();

			String dest = "d:/temp/test.pdf";

			// Creating a PdfDocument

 

		 
			// Closing the document
			PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		    Document doc = new Document(pdfDoc);

		    Table table = new Table(new float[] {50, 50 });

		    Paragraph headerParagraph = new Paragraph();
		    Text headerTitle = new Text("Title of PDF")
		            .setFontSize(20)
		            .setFontColor(new DeviceRgb(0, 128, 128));
		    Text headerDescription = new Text("Description")
		            .setFontSize(11);

		    headerParagraph.add(headerTitle);
		    headerParagraph.add(headerDescription);

		    table.addCell(new Cell().add("logo").setBorder(Border.NO_BORDER));
		    table.addCell(new Cell().add(headerParagraph).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

		    doc.add(table);
			
			
			doc.close();
			System.out.println("PDF Created");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
