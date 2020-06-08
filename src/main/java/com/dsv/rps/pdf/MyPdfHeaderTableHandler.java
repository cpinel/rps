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
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;

public class MyPdfHeaderTableHandler implements IEventHandler {

	protected Table table;
	protected Table tableLeft;
	protected Table tableRight;
	protected float tableHeight = 100;
	protected Document doc;

	protected static PdfFont defaultFont;
	
	static {
		
		try {
			 	defaultFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public float getTableHeight() {
		return tableHeight;
	}

	public void setTableHeight(float tableHeight) {
		this.tableHeight = tableHeight;
	}
	
	public Cell createCell(IBlockElement object)
	{
		
		
	return	new Cell().add(object).setBorder(Border.NO_BORDER).setFont(defaultFont).setFontSize(9);
	 	
		
	}

// this will create header with partner details
	public MyPdfHeaderTableHandler(Document doc, Invoice invoice) {
		 
		// build header structure
		this.doc = doc;
		//split int two
		float[] columnWidths = { 50, 50 };
		table = new Table(UnitValue.createPercentArray(columnWidths));
		
		float[] columnWidths2 = { 100 };
		tableLeft = new Table(columnWidths2);
		
		table.setBorder(Border.NO_BORDER);
		table.setWidth(doc.getPdfDocument().getDefaultPageSize().getWidth());

		table.addCell(new Cell().setBorder(Border.NO_BORDER).add(tableLeft)); 
		
		
		BusinessPartner shp = invoice.getPartners().get("SHP");
		if (shp != null)

		{
			Paragraph para1 = new Paragraph();
			StringBuilder sb = new StringBuilder();
			sb.append(shp.getName()).append("\n").append(shp.getAdress1a()).append("\n").append(shp.getAdress2())
					.append("\n").append(shp.getAdress3()).append("\n").append(shp.getAdress4()).append("\n");
			para1.add(sb.toString());
			para1.setBorder(new SolidBorder(1));
			para1.setMargin(3);
			para1.setBackgroundColor(Color.WHITE);
			tableLeft.addCell(createCell(para1) );
		} else
			tableLeft.addCell("").setBorder(Border.NO_BORDER);
		  
		
		BusinessPartner cne = invoice.getPartners().get("CNE");
		if (cne != null)

		{
			Paragraph para1 = new Paragraph();
			StringBuilder sb = new StringBuilder();
			sb.append(cne.getName()).append("\n").append(cne).append("\n").append(cne.getAdress2())
					.append("\n").append(cne.getAdress3()).append("\n").append(cne.getAdress4()).append("\n");
			para1.add(sb.toString());
			para1.setBorder(new SolidBorder(1));
			para1.setMargin(3);
			para1.setBackgroundColor(Color.WHITE);
			tableLeft.addCell(createCell(para1) );
		} else
			tableLeft.addCell("").setBorder(Border.NO_BORDER);
		  
		
		table.addCell(new Cell().setBorder(Border.NO_BORDER));
		 Rectangle area = new Rectangle(0, 0, 500, 500);
		    LayoutResult result = table.createRendererSubTree().setParent(doc.getRenderer()).layout(new LayoutContext(new LayoutArea(0, area)));
		    tableHeight=    result.getOccupiedArea().getBBox().getHeight();
System.out.println(result.getOccupiedArea().getBBox().getHeight());
	}

	@Override
	public void handleEvent(Event event) {
		PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
		PdfPage page = docEvent.getPage();
		Rectangle pageSize = page.getPageSize();
		PdfDocument pdfDoc = ((PdfDocumentEvent) event).getDocument();
		 
		PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

		try (Canvas mycanvas = new Canvas(pdfCanvas, pdfDoc, pageSize)) {
			// header
			mycanvas.add(table);

		}
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
}
