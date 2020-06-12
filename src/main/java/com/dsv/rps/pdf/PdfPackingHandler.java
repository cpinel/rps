package com.dsv.rps.pdf;

import java.io.IOException;

import com.dsv.rps.bean.BusinessPartner;
import com.dsv.rps.bean.Invoice;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

public class PdfPackingHandler implements IEventHandler {

	protected Table headerTable;
	protected Table leftSide;
	protected Table rightSide;
	protected float tableHeight = 100;
	protected Document doc;

	protected   PdfFont defaultFont;
	protected   PdfFont boldFont;
 
	
	 
	public PdfPackingHandler(Document doc, Invoice invoice) {
		try{defaultFont = PdfFontFactory.createFont(FontConstants.HELVETICA);
		boldFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
		}
		catch (Exception ex) {ex.printStackTrace();}
		
		float[] columnWidths = { 50, 50 };
		headerTable = new Table(UnitValue.createPercentArray(columnWidths));

		float[] columnWidths2 = { 100 };
		leftSide = new Table(columnWidths2);
 
		rightSide = new Table(UnitValue.createPercentArray(columnWidths));

		headerTable.setBorder(Border.NO_BORDER);
		headerTable.setWidth(doc.getPdfDocument().getDefaultPageSize().getWidth());
		headerTable.addCell(new Cell().setBorder(Border.NO_BORDER).add(leftSide));
		headerTable.addCell(new Cell().setBorder(Border.NO_BORDER).add(rightSide));
		
		leftSide.setWidth(doc.getPdfDocument().getDefaultPageSize().getWidth()/3);
		leftSide.addCell(new Cell().setBorder(Border.NO_BORDER).add("\n\n"));
		rightSide.setWidth(doc.getPdfDocument().getDefaultPageSize().getWidth()/3);
	 	rightSide.addCell(new Cell().setBorder(Border.NO_BORDER).add("\n\n"));
		rightSide.addCell(new Cell().setBorder(Border.NO_BORDER).add("\n\n"));
		// build header structure
		this.doc = doc;
		// split int two

	// build left 
		BusinessPartner shipper = invoice.getPartners().get("SHP");
		leftSide.addCell(createCell(buildParagraph("Shipper\\Exporter",shipper)));
 		BusinessPartner invoiceto = invoice.getPartners().get("INV");
		leftSide.addCell(createCell(buildParagraph("Invoice To",invoiceto)));
 
		headerTable.setMarginLeft(30);
	 
		rightSide.addCell(createCell(buildParagraph("Date",invoice.getDate())));
		 
		String paymentTerms="";
		for (String paymentTerm :invoice.getPaymentTerms())
		{
			paymentTerms+=paymentTerm+"\n" ;
			
		}
		rightSide.addCell(new Cell(1,2).setBorder(Border.NO_BORDER).add(new Paragraph(paymentTerms).setFontSize(9).setHeight(60).setBorder(new SolidBorder(1))));
		headerTable.addCell(new Cell().setBorder(Border.NO_BORDER));
		Rectangle area = new Rectangle(0, 0, 500, 500);
		LayoutResult result = headerTable.createRendererSubTree().setParent(doc.getRenderer())
				.layout(new LayoutContext(new LayoutArea(0, area)));
		tableHeight = result.getOccupiedArea().getBBox().getHeight();
		System.out.println(result.getOccupiedArea().getBBox().getHeight());
	}
	
	public float getTableHeight() {
		return tableHeight;
	}

	public void setTableHeight(float tableHeight) {
		this.tableHeight = tableHeight;
	}

	public Cell createCell(IBlockElement object) {

		return new Cell().add(object).setBorder(Border.NO_BORDER).setFont(defaultFont).setFontSize(9);

	}
	private Paragraph buildParagraph(String title, String text) {
		if (text == null)
			text = new String(" ");
		Paragraph para1 = new Paragraph().setHeight(30).setWidth(150);
		para1.add(new Text(title).setBold()); 
		para1.add("\n");
		para1.add(text+" ");
		para1.setBorder(new SolidBorder(1));
		para1.setMargin(1);
		para1.setBackgroundColor(Color.WHITE);

		return para1;
	}

	private Paragraph buildParagraph(String title, BusinessPartner object) {
		if (object == null)
			object = new BusinessPartner();
		Paragraph para1 = new Paragraph();
		para1.add(new Text(title).setBold());
		para1.add("\n");
		StringBuilder sb = new StringBuilder();
		sb.append(object.getName()).append("\n").append(object.getAdress1a()).append("\n").append(object.getAdress2())
				.append("\n").append(object.getAdress3()).append("\n");
		para1.add(sb.toString());
		para1.setBorder(new SolidBorder(1));
		para1.setMargin(1);
		para1.setBackgroundColor(Color.WHITE);

		return para1;
	}



	@Override
	public void handleEvent(Event event) {
		PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
		PdfPage page = docEvent.getPage();
		Rectangle pageSize = page.getPageSize();

		PdfDocument pdfDoc = ((PdfDocumentEvent) event).getDocument();

		PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
// add  handler to create page number on top right, additionnally to the shipper,consignee
		try (Canvas mycanvas = new Canvas(pdfCanvas, pdfDoc, pageSize)) {

			mycanvas.add(headerTable);
			// header

			int pageNumber = docEvent.getDocument().getPageNumber(page);

			Paragraph p = new Paragraph().add("Page ").add(String.valueOf(pageNumber));
			mycanvas.showTextAligned(p, 800, 560, TextAlignment.RIGHT);

			System.out.println("dimension=" + mycanvas.getRootArea().toString());

		}

	}

	public Table getTable() {
		return headerTable;
	}

	public void setTable(Table headerTable) {
		this.headerTable = headerTable;
	}
}
