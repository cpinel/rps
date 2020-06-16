package com.dsv.rps.pdf;

import java.io.IOException;
import java.util.List;

import org.joda.time.LocalTime;

import com.dsv.rps.bean.ChargeLine;
import com.dsv.rps.bean.Invoice;
import com.dsv.rps.bean.Item;
import com.dsv.rps.bean.Packaging;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

public class PdfBuilder {
	protected PdfFont defaultFont = null;
	protected Invoice invoice = null;

	public PdfBuilder() {

		try {
			defaultFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public PdfBuilder(Invoice invoice) {

		try {
			defaultFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Cell createCell(IBlockElement object) {

		return new Cell().add(object).setBorder(Border.NO_BORDER).setFont(defaultFont).setFontSize(9);

	}

	public Cell createCellWithNoBorder(String object) {

		return new Cell().add(object).setBorder(Border.NO_BORDER).setFont(defaultFont).setFontSize(9);

	}

	public Cell createHeaderCell(String object) {

		return new Cell().add(object).setFont(defaultFont).setFontSize(9).setBackgroundColor(Color.DARK_GRAY)
				.setFontColor(Color.WHITE);

	}

	public Cell createCell(String object) {

		return new Cell().add(object).setFont(defaultFont).setFontSize(9);

	}

	public Cell createCell(String object, TextAlignment textAlignment) {

		return new Cell().add(object).setFont(defaultFont).setFontSize(9).setTextAlignment(textAlignment);

	}

	private Table buildChargelines(List<ChargeLine> chargeLines) {
		float[] columnWidths = { 70, 15, 15 };
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setAutoLayout();
		
		table.setWidthPercent(100);
		for (ChargeLine charge : chargeLines) {
			table.addCell(new Cell().add(" ").setBorder(Border.NO_BORDER));
			table.addCell(new Cell().add(charge.getText()).setBorder(Border.NO_BORDER).setFontSize(9).setBold());
			table.addCell(new Cell().add(charge.getValue() + "").setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setFontSize(9).setBold());
			 	// text.setFixedPosition(xOffset,yOffset, 150);
		}
		return table;
	}
 

	private Table buildItemsTable(List<Item> items) {

		float[] columnWidths = { 20, 10, 10, 10, 10, 8, 8, 8, 8, 8 };
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setAutoLayout();
		table.setWidthPercent(100);
		// header row:
		table.addHeaderCell(createHeaderCell("Item Description"));
		table.addHeaderCell(createHeaderCell("Part Number"));
		table.addHeaderCell(createHeaderCell("HS Code"));
		table.addHeaderCell(createHeaderCell("Customer Order No"));
		table.addHeaderCell(createHeaderCell("Deere Order No"));
		table.addHeaderCell(createHeaderCell("Ctry of Origin"));
		table.addHeaderCell(createHeaderCell("Unit Price"));
		table.addHeaderCell(createHeaderCell("Quantity"));
		table.addHeaderCell(createHeaderCell("Weight Kg"));
		table.addHeaderCell(createHeaderCell("Value"));

		for (Item myitem : items) {
			table.addCell(createCell(myitem.getDescription()));
			table.addCell(createCell(myitem.getPartNumber()));
			table.addCell(createCell(myitem.getHsCode()));
			table.addCell(createCell(myitem.getCustomerOrderNo()));
			table.addCell(createCell(myitem.getDeereOrderNo()));
			table.addCell(createCell(myitem.getCountryOfOrigin()));

			table.addCell(createCell(myitem.getUnitPrice(), TextAlignment.RIGHT));

			table.addCell(createCell(myitem.getQuantity(), TextAlignment.RIGHT));
			table.addCell(createCell(myitem.getWeight(), TextAlignment.RIGHT));
			table.addCell(createCell(myitem.getValue(), TextAlignment.RIGHT));
		}

		return table;
	}

	private Table buildPackingTable(List<Packaging> packages) {

		float[] columnWidths = { 20, 10, 10, 10, 10, 10, 30 };
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setAutoLayout();
		table.setWidthPercent(100);
		// header row:
		table.addHeaderCell(createHeaderCell("Case Number"));
		table.addHeaderCell(createHeaderCell("Length"));
		table.addHeaderCell(createHeaderCell("Width"));
		table.addHeaderCell(createHeaderCell("Height"));
		table.addHeaderCell(createHeaderCell("Gross Weight KG"));
		table.addHeaderCell(createHeaderCell("Net Weight KG"));
		table.addHeaderCell(createHeaderCell("Type"));

		for (Packaging mypackaging : packages) {
			table.addCell(createCell(mypackaging.getCaseNumber()));
			table.addCell(createCell(mypackaging.getLength(), TextAlignment.RIGHT));
			table.addCell(createCell(mypackaging.getWidth(), TextAlignment.RIGHT));
			table.addCell(createCell(mypackaging.getHeight(), TextAlignment.RIGHT));
			table.addCell(createCell(mypackaging.getGrossWeight(), TextAlignment.RIGHT));
			table.addCell(createCell(mypackaging.getNetWeight(), TextAlignment.RIGHT));
			table.addCell(createCell(mypackaging.getType() ));
		}

		return table;
	}

	public void buildInvoiceReport(Invoice inv, String dest) {

		try (ByteArrayOutputStream ba = new ByteArrayOutputStream();
				PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
				Document doc = new Document(pdfDoc, PageSize.A4.rotate(), true);) {

			// Creating a PdfDocument

			// add handlers
			PdfPageNumberHandler myHeaderHandler = new PdfPageNumberHandler();
			pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, myHeaderHandler);
			// put a table handler
			PdfItemsHandler myItemHandler = new PdfItemsHandler(doc, inv);
			pdfDoc.addEventHandler(PdfDocumentEvent.INSERT_PAGE, myItemHandler);

			// define margin to include header
			doc.setMargins(20 + myItemHandler.getTableHeight(), 36, 36, 36);

			// Creating an Area Break
			Paragraph para1 = new Paragraph("Commercial Invoice");
			para1.setFixedPosition(50, 560, 300);
			doc.add(para1);
			// add full table

			
			Table chargelines = buildChargelines(inv.getChargeLines());
			doc.add(chargelines);
	 

			Table table = buildItemsTable(inv.getItems());
			doc.add(table);
			// break

			pdfDoc.removeEventHandler(PdfDocumentEvent.INSERT_PAGE, myItemHandler);

			// remove previous handler

			PdfPackingHandler myPackingHandler = new PdfPackingHandler(doc, inv);
			pdfDoc.addEventHandler(PdfDocumentEvent.INSERT_PAGE, myPackingHandler);
			doc.add(new AreaBreak());
			// start
			Table table2 = buildPackingTable(inv.getPackaging());
			doc.add(table2);

			// Closing the document
			doc.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public byte[] buildInvoiceReport(Invoice inv) {

		byte[] result = null;
		try (ByteArrayOutputStream ba = new ByteArrayOutputStream();
				PdfDocument pdfDoc = new PdfDocument(new PdfWriter(ba));
				Document doc = new Document(pdfDoc, PageSize.A4.rotate(), true);) {

			// Creating a PdfDocument

			// Document doc = new Document(pdfDoc, PageSize.A4);
			// put a page count handler
			PdfPageNumberHandler myHeaderHandler = new PdfPageNumberHandler();
			pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, myHeaderHandler);
			// put a table handler
			PdfItemsHandler myTableHandler = new PdfItemsHandler(doc, inv);
			pdfDoc.addEventHandler(PdfDocumentEvent.INSERT_PAGE, myTableHandler);
			// Adding an empty page
			// pdfDoc.addNewPage();

			// Creating a Document

			// define margin to include header
			doc.setMargins(20 + myTableHandler.getTableHeight(), 36, 36, 36);

			// Creating an Area Break
			Paragraph para1 = new Paragraph("Commercial Invoice");
			para1.setFixedPosition(50, 560, 300);
			doc.add(para1);
			// add full table
			Table table = buildItemsTable(inv.getItems());
			doc.add(table);

			Paragraph para2 = new Paragraph("\n\n");
			doc.add(para2);
			// start
			Table table2 = buildPackingTable(inv.getPackaging());
			doc.add(table2);

			System.out.println("PDF Created");
			result = ba.toByteArray();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	public static void main(String[] args) {

		// Creating a PdfWriter
		try {
			String dest = "c:/temp/myreport" + LocalTime.now().hashCode() + ".pdf";

			PdfBuilder pdfbuilder = new PdfBuilder();
			pdfbuilder.buildInvoiceReport(Invoice.getTest(), dest);

			// String dest2 = "c:/temp/myreport" + LocalTime.now().hashCode() + ".pdf";
			// PdfBuilder pdfbuilder2 = new PdfBuilder();
			// pdfbuilder2.buildInvoiceReport(Invoice.getTest(), dest2);

			// PdfBuilder pdfbuilder3 = new PdfBuilder();
			// byte[] testbytes = pdfbuilder3.buildInvoiceReport(Invoice.getTest());
			// System.out.println(testbytes.length);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
