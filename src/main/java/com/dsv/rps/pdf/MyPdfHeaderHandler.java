package com.dsv.rps.pdf;

import java.io.IOException;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

public class MyPdfHeaderHandler implements IEventHandler {

	protected String info;

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

	@Override
	public void handleEvent(Event event) {
		PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
		PdfPage page = docEvent.getPage();
		Rectangle pageSize = page.getPageSize();
		PdfDocument pdfDoc = ((PdfDocumentEvent) event).getDocument();
		PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

	 
		
	     int pageNum = docEvent.getDocument().getPageNumber(page);
	        PdfCanvas canvas = new PdfCanvas(page);
	        canvas.beginText();
	        try {
	            canvas.setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 12);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        canvas.moveText(34, 803);
	    
	        canvas.moveText(450, 0);
	        canvas.showText(String.format("Page %d  ", pageNum));
		pdfCanvas.release();
	}
}
