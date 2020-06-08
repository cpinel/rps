package com.dsv.rps.pdf;

import java.io.IOException;
import java.util.List;

import com.dsv.rps.bean.Invoice;
import com.dsv.rps.bean.Item;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

public class PdfBuilder {
	protected static PdfFont defaultFont;
	
	static {
		
		try {
			 	defaultFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	public static Cell createCell(IBlockElement object)
	{
		
		
	return	new Cell().add(object).setBorder(Border.NO_BORDER).setFont(defaultFont).setFontSize(9);
	 	
		
	}
	
	public static Cell createCell(String object)
	{
		
		
	return	new Cell().add(object).setBorder(Border.NO_BORDER).setFont(defaultFont).setFontSize(9);
	 	
		
	}
	private static Table buildTable(List<Item> items) {

		
		
		
		float[] columnWidths = { 10, 10, 10, 30, 10,10,10 ,10};
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setAutoLayout();
		// header row:
        table.addHeaderCell("CountryOfOrigin");
        table.addHeaderCell("CustomerOrderNo");
        table.addHeaderCell("DeereOrderNo");
        table.addHeaderCell("Description");
        table.addHeaderCell("HsCode");
        table.addHeaderCell("PartNumber");
        table.addHeaderCell("Quantity");
        table.addHeaderCell("Weight");
       
		 
		for (Item myitem : items) {
			table.addCell(createCell(myitem.getCountryOfOrigin()));
			table.addCell(createCell(myitem.getCustomerOrderNo()));
			table.addCell(createCell(myitem.getDeereOrderNo()));
			table.addCell(createCell(myitem.getDescription()));
			table.addCell(createCell(myitem.getHsCode()));
			table.addCell(createCell(myitem.getPartNumber()));
			table.addCell(createCell(myitem.getQuantity()));
			table.addCell(createCell(myitem.getWeight()));
		}

		return table;
	}

	public static void main(String[] args) {

		// Creating a PdfWriter
		try {
			Invoice testInvoice = Invoice.getTest();

			String dest = "d:/temp/addingTable2.pdf";

			// Creating a PdfDocument

			PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		//	Document doc = new Document(pdfDoc, PageSize.A4);
			
			Document  doc = new Document(pdfDoc,PageSize.A4.rotate());
			// put a handler
			MyPdfHeaderTableHandler myHeaderHandler = new MyPdfHeaderTableHandler(doc,testInvoice);
			pdfDoc.addEventHandler(PdfDocumentEvent.INSERT_PAGE, myHeaderHandler);
			// Adding an empty page
			pdfDoc.addNewPage();

			// Creating a Document

			// define margin to include header
			doc.setMargins(20 + myHeaderHandler.getTableHeight(), 36, 36, 36);

			String para = "Welcome to Tutorialspoint.";
			// Creating an Area Break
			Paragraph para1 = new Paragraph(para);
			para1.setFixedPosition(10, 10, 300);
			doc.add(para1);

			Table table=buildTable(testInvoice.getItems()); 
			doc.add(table);

			// Closing the document
			doc.close();
			System.out.println("PDF Created");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
