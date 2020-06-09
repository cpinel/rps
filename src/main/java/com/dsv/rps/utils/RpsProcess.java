package com.dsv.rps.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dsv.rps.beans.InputBean;
import com.dsv.rps.beans.OutputBean;
import com.dsv.rps.logging.Error;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.Message;

public class RpsProcess {

	
	public String process (IMessage inputMessage) throws Exception
	{

		OutputBean ob = new OutputBean();
		InputBean ib = buildInputBeanFromXMLMessage ( inputMessage,ob);
		doCalculations(ib,ob);
		String outputXML = generateXMLOutput ( ob);
		
		return outputXML;
		
	}
	
	// reads various properties of the XML message, and populates an InputBean
	private InputBean buildInputBeanFromXMLMessage (IMessage message, OutputBean ob) throws Exception
	{
		InputBean ib = new InputBean();
		
		if (message.getLabel() != null && message.getContentType() != null && message.getContentType().contentEquals("application/xml"))
		{
			byte[] body = message.getBody();
            String xmlresult = new String(body, UTF_8);
		}
		else
		{
			ob.addError(Error.UNREADABLE_XML);
		}
		
		// Input objects
		completeInputBeanWithXMlData(ib,message);
		completeInputBeanWithTxtFileData(ib);
		
		return ib;
		
	}
	
	// complete InputBean with XML data
	private void completeInputBeanWithXMlData (InputBean ib, IMessage inpuMessage) throws Exception
	{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			System.out.println("READING ...");
			File fileXML = new File("C:\\Users\\didie\\Desktop\\test rps\\sample.xml.txt");

			System.out.println("PARSING ...");
			Document xml = builder.parse(fileXML);
			Element root = xml.getDocumentElement();
			XPathFactory xpf = XPathFactory.newInstance();
			XPath path = xpf.newXPath();
			
			String txtfile64 = path.evaluate(CW1XMLPath.TXTFILE,root);
			
			byte[] decoded = Base64.getDecoder().decode(txtfile64.getBytes("UTF-8"));
			String sdec = new String(decoded, StandardCharsets.UTF_8);
			ib.setReadableTxtFile(sdec);
			
	}
	
	// complete InputBean with XML data
	private void completeInputBeanWithTxtFileData  (InputBean ib) throws Exception
	{
		String txtFile = ib.getReadableTxtFile();
		
		// parse and complete InputBean
	}
	
	
	private void doCalculations ( InputBean ib, OutputBean ob)
	{
		// do all calculations that allow to complete the ob with data available in ib
	}
	
	private String generateXMLOutput ( OutputBean ob)
	{
	      // TODO complete message with proper structure
	      return "<RPSReply><ID>123456789</ID><code>OK</code><format>PDF></format><binaryFile>aBcDeFgHiJ</binaryFile></RPSReply>";
	}
}
