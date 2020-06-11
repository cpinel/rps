package com.dsv.rps.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dsv.rps.beans.EDICharge;
import com.dsv.rps.beans.HAWBCharge;
import com.dsv.rps.beans.InputBean;
import com.dsv.rps.beans.OutputBean;
import com.dsv.rps.logging.Error;
import com.dsv.rps.logging.Warning;
import com.dsv.rps.resources.Constants;
import com.microsoft.azure.servicebus.IMessage;

public class RpsProcess {

	public static void main ( String [] args) throws Exception
	{
		InputBean ib = new InputBean();
		OutputBean ob = new OutputBean();
		
		FileInputStream fis = new FileInputStream(new File(Constants.MY_TEST_FILE));
		
		completeInputBeanWithXMlData(ib,fis,ob);
		
		if (ob.getErrors()!=null)
		{
			System.out.println(ob.getErrors().get(0).getText());
			return;
		}
		
		completeInputBeanWithTxtFileData(ib,ob);
		doCalculations(ib,ob);
		String outputXML = generateXMLOutput ( ob);
		
		System.out.println(outputXML);
	}
	
	
	public static String process (IMessage inputMessage) throws Exception
	{

		OutputBean ob = new OutputBean();
		
		InputBean ib = validateXML(inputMessage, ob);
		InputStream is = new ByteArrayInputStream(inputMessage.getBody());
		buildInputBeanFromXMLMessage ( ib,is,ob);
		doCalculations(ib,ob);
		String outputXML = generateXMLOutput ( ob);
		
		return outputXML;
		
	}
	
	private static InputBean validateXML(IMessage message, OutputBean ob)
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
		
		return ib;
		
	}
	// reads various properties of the XML message, and populates an InputBean
	private static InputBean buildInputBeanFromXMLMessage (InputBean ib, InputStream is, OutputBean ob) throws Exception
	{
		completeInputBeanWithXMlData(ib,is,ob);
		completeInputBeanWithTxtFileData(ib,ob);
		
		return ib;
		
	}
	
	// complete InputBean with XML data
	private static void completeInputBeanWithXMlData (InputBean ib, InputStream is,OutputBean ob)
	{
		DocumentBuilder builder = null;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException pce)
		{
			ob.addError(Error.XMLPARSER_ISSUE);
			return;
		}
		Document xml = null;
		try
		{
			xml = builder.parse(is);
		}
		catch (Exception e)
		{
			ob.addError(Error.WRONG_XML_FORMAT);
			return;
		}
			
		Element root = xml.getDocumentElement();
		XPathFactory xpf = XPathFactory.newInstance();
		XPath path = xpf.newXPath();
		
		String txtfile64 = null;
		try
		{
			txtfile64 = path.evaluate(CW1XMLPath.TXTFILE,root);
		}
		catch (XPathExpressionException e)
		{
			ob.addError(Error.NO_TXT_IN_XML);
			return;
		}
		if (StringUtils.isNull(txtfile64))
		{
			ob.addError(Error.NO_TXT_IN_XML);
			return;
		}
		
		try
		{
			byte[] decoded = Base64.getDecoder().decode(txtfile64.getBytes("UTF-8"));
			String sdec = new String(decoded, StandardCharsets.UTF_8);
			ib.setReadableTxtFile(sdec);
			
		}
		catch (UnsupportedEncodingException e)
		{
			ob.addError(Error.UNSUPPORTED_EONCODING_EXCEPTION);
			return;
		}
			
		System.out.println("INPUTBEAN updated with TXT file");
		
		ib.setHawb(assignXMLPath(ib, ob, path, root, CW1XMLPath.HAWB, Error.HAWB));
		ib.setOrderNo(assignXMLPath(ib, ob, path, root, CW1XMLPath.ORDER_NUMBER, Error.ORDER_NUMBER));
		ib.setInvoiceId(assignXMLPath(ib, ob, path, root, CW1XMLPath.INVOICE_ID, Error.INVOICEID));
		
		String shcw = assignXMLPath(ib, ob, path, root, CW1XMLPath.HAWB_GW, Error.HAWB_GW_MISSING);
		if (!StringUtils.isNull(shcw))
		{
			try
			{
				ib.setHawbCW(Double.valueOf(shcw));
			}
			catch (NumberFormatException e)
			{
				ob.addError(Error.HAWB_GW_FORMAT);
			}
		}
		
		// Invoice Gross Weight
		try {
			XPathExpression lineExpr = path.compile(CW1XMLPath.LINES);
			NodeList lineNodes = (NodeList)lineExpr.evaluate(root, XPathConstants.NODESET);
			double invoiceweight = 0;
			for (int i=1;i<=lineNodes.getLength();i++)
			{
				String lineweight = assignXMLPath(ib, ob, path, root,CW1XMLPath.LINE_GW.replace("%%%", ""+i), null);
				try
				{
					invoiceweight = invoiceweight + Double.valueOf(lineweight);
				}
				catch (NumberFormatException e)
				{
					ob.addError(Error.LINE_GW_FORMAT);
				}
			}
			ib.setInvoiceCW(invoiceweight);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			ob.addError(Error.LINE_GW);
		}
		if (ib.getHawbCW()!= null && ib.getInvoiceCW() !=null && ib.getHawbCW().doubleValue() > ib.getInvoiceCW().doubleValue())
		{
			ob.addWarning(Warning.INVOICE_WEIGHT_EXCEEDS_SHIPMENT_WEIGHT);
		}
		
		// EDI Currency
		ib.setEdiCurrency(assignXMLPath(ib, ob, path, root, CW1XMLPath.EDI_CHARGE_CURR, Error.EDI_CHARGE_CURR));
		
		
		// Invoice EDI Charges ( SAC only )
		try {
			XPathExpression lineExpr = path.compile(CW1XMLPath.EDI_CHARGES_SAC);
			NodeList  referenceNodes = (NodeList)lineExpr.evaluate(root, XPathConstants.NODESET);
			for (int i=0;i<referenceNodes.getLength();i++)
			{
				Node node = referenceNodes.item(i);
				String value = node.getTextContent();
				String code = null;
				if (node.getAttributes().getNamedItem("Type")!= null)
				{
					code = node.getAttributes().getNamedItem("Type").getTextContent();
					if (!StringUtils.isNull(code) && code.startsWith("SAC|"))
					{
						String saccode = code.substring(4); // remove SAC|
						if (value.indexOf("|")>-1)
						{
							String text = value.substring(0,value.indexOf("|"));
							try
							{
								Double cost = Double.parseDouble(value.substring(value.indexOf("|")+1));
								ib.addEdiCharge(new EDICharge(saccode,text, cost));
							}
							catch (NumberFormatException e)
							{
								ob.addError(Error.EDI_CHARGE_VALUE_WRONG_FORMAT);
							}
						}
					}
				}
				
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			ob.addError(Error.EDI_CHARGE);
		}
		
		// Freight HAWB Charges
		try {
			XPathExpression chargelineExpr = path.compile(CW1XMLPath.HAWB_CHARGES);
			NodeList  chargesNodes = (NodeList)chargelineExpr.evaluate(root, XPathConstants.NODESET);
			for (int i=0;i<chargesNodes.getLength();i++)
			{
				Node node = chargesNodes.item(i);
				String chargeCode = null;
				String chargeDescription = null;
				Double chargeAmount = null;
				
				for ( int j=0;j<node.getChildNodes().getLength();j++)
				{
					Node child = node.getChildNodes().item(j);
					if (CW1XMLPath.HAWB_CHARGELINE_CHARGECODE.equals(child.getNodeName()))
						chargeCode = child.getTextContent();
					else if (CW1XMLPath.HAWB_CHARGELINE_DESCRIPTION.equals(child.getNodeName()))
						chargeDescription = child.getTextContent();
					if (CW1XMLPath.HAWB_CHARGELINE_REVENUE_AMOUNT.equals(child.getNodeName()))
					{
						try {
							chargeAmount = Double.parseDouble(child.getTextContent());
						}
						catch (NumberFormatException e)
						{
							ob.addError(Error.HAWB_CHARGE_VALUE_WRONG_FORMAT);
						}
					}
				}
				if (chargeCode!= null && chargeDescription!=null && chargeAmount!= null)
				{
					if ("FRT".equals(chargeCode))
					{
						ib.setFreightHAWBCharge(new HAWBCharge(chargeCode, chargeDescription, chargeAmount));
					}
					else
					{
						ib.addOtherHAWBCharge(new HAWBCharge(chargeCode, chargeDescription, chargeAmount));
					}
				}
				else
				{
					ob.addWarning(Warning.HAWB_CHARGE_DETAILS_MISSING);
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			ob.addError(Error.EDI_CHARGE);
		}
		
		System.out.println(ib);
		
		System.out.println("INPUTBEAN IS COMPLETED WITH XML STUFF....");
			
	}
	
	private static String assignXMLPath(InputBean ib,OutputBean ob, XPath path, Element root, String xmlpath,Error error)
	{
		String value = null;
		try
		{
			value = path.evaluate(xmlpath,root);
		}
		catch (XPathExpressionException e)
		{
			if (error != null)
				ob.addError(error);
			return null;
		}
		if (StringUtils.isNull(value))
		{
			if (error != null)
				ob.addError(error);
			return null;
		}
		return value;
	}
	
	
	// complete InputBean with XML data
	private static void completeInputBeanWithTxtFileData  (InputBean ib, OutputBean ob) throws Exception
	{
		String txtFile = ib.getReadableTxtFile();
		
		// parse and complete InputBean
		System.out.println("INPUTBEAN TO BE COMPLETED WITH TXT STUFF....");
	}
	
	
	private static void doCalculations ( InputBean ib, OutputBean ob)
	{
		// do all calculations that allow to complete the ob with data available in ib
		
		System.out.println("DOING CALCULATIONS AND COMPLETING OUTPUTBEAN....");
	}
	
	private static String generateXMLOutput ( OutputBean ob)
	{
	      // TODO complete message with proper structure
	      return "<RPSReply><ShipmentId>SHSV0000050</ShipmentId><code>OK</code><format>TXT</format><errors/><warnings><warning>Sum of line items weight is greater than shipment gros weight</warning></warnings><binaryFile>UGFuYWxwaW5hIEVjdWFkb3IgUy5BLjtQYW5hbHBpbmEgRWN1YWRvciBTLkEuOzE3OTA3MzAxNjYwMDE7MDE7MDAxOzAwMTswMDAwMDAxNTc7QXYuIDYgZGUgRGljaWVtYnJlIE4zMi0zMTIgeSBKZWFuIEIuIEJvdXNzaW5nYXVsdCBFZGlmaWNpbyBUNiwgUGlzbyA1LCBPZmljaW5hIDUwMzswOC8wNS8yMDIwO0F2ZW5pZGEgRWwgSW5jYSwgeSBBdmVuaWRhIEFtYXpvbmFzIDQwNiwgRTQtMTgxOzA1OTA7U0k7MDQ7O0hBTExJQlVSVE9OIExBVElOIEFNRVJJQ0EgU1JMOzE3OTE4NTE2MzYwMzI7MzEyLjAwOzAuMDA7W0lUMzswMDAwOzAuMDA7MDAwMDswLjAwXVtJVDI7MjszMTIuMDA7MTI7MjQuMDBdMC4wMDszMzYuMDA7RE9MQVI7W1BBRzIwOzMzNi4wMDswO0RpYXNdO1tERVRDQ0w7MDAwO0lNUE9SVCBDVVNUT01TIENMRUFSQU5DRSBDSEFSR0VTOzEuMDA7MjAwLjAwOzAuMDA7MjAwLjAwOzs7REVUXVtJRDM7MC4wMDswLjAwOzAuMDA7MC4wMF1bSUQyOzI7MTI7MjAwLjAwOzI0LjAwMDBdO1tERVRMUkZMOzAwMDtSRUlNQlVSU0VNRU5UIFdBUkVIT1VTRSBGT1JLTElGVCBBTkQgT1RIRVIgRVFVSVBNRU5UOzEuMDA7MTEyLjAwOzAuMDA7MTEyLjAwOzs7REVUXVtJRDM7MC4wMDswLjAwOzAuMDA7MC4wMF1bSUQyOzAuMDA7MDA7MC4wMDswLjAwXWVtYWlsQ2xpZW50ZT1OZWxzb24uTWVuZGV6QHBhbmFscGluYS5jb207Q09ESUdPSU5URVJOT1NBUD0wMDAwMDAxNTc7Q09ESUdPSU5URVJOT1NBUENMSUVOVEU9Njc5MDY4OTI7UkVGRVJFTkNFPTtIQVdCL0JMPVNBTzc1NTg4NDE=</binaryFile></RPSReply>";
	}
}
