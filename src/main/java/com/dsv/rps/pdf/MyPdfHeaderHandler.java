package com.dsv.rps.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

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
		
		
		try  (Canvas mycanvas= new Canvas(pdfCanvas, pdfDoc, pageSize)) {
				// header
			mycanvas.showTextAligned("title heading", 70, pageSize.getTop() - 40, TextAlignment.CENTER,
						VerticalAlignment.MIDDLE, 0);
			mycanvas.showTextAligned("title heading", 70, pageSize.getTop() - 60, TextAlignment.CENTER,
					VerticalAlignment.MIDDLE, 0);
			mycanvas.showTextAligned("some text", pageSize.getWidth() / 2, pageSize.getTop() - 20, TextAlignment.CENTER,
						VerticalAlignment.MIDDLE, 0);
	 

	}}
}
