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
import com.dsv.rps.beans.Epartner;
import com.dsv.rps.beans.HAWBCharge;
import com.dsv.rps.beans.InputBean;
import com.dsv.rps.beans.LineItem;
import com.dsv.rps.beans.OutputBean;
import com.dsv.rps.beans.Partner;
import com.dsv.rps.logging.Error;
import com.dsv.rps.logging.Warning;
import com.dsv.rps.parser.TextBuilder;
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
		try
		{
			buildInputBeanFromXMLMessage ( ib,is,ob);
			doCalculations(ib,ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
				String lineweight = assignXMLPath(ib, ob, path, root,CW1XMLPath.LINE_GROSS_WEIGHT.replace("%%%", ""+i), null);
				try
				{
					invoiceweight = invoiceweight + Double.valueOf(lineweight);
				}
				catch (NumberFormatException e)
				{
					ob.addError(Error.LINE_GW_FORMAT);
				}
				
				LineItem item = new LineItem();
				item.setCaseNumber(assignXMLPath(ib, ob, path, root,CW1XMLPath.LINE_CASE_NUMBER.replace("%%%", ""+i), null));
				item.setItemDescription(assignXMLPath(ib, ob, path, root,CW1XMLPath.LINE_CASE_NUMBER.replace("%%%", ""+i), null));
				item.setPartNumber(assignDoubleXMLPath(ib, ob, path, root,CW1XMLPath.LINE_CASE_NUMBER.replace("%%%", ""+i), null));
				item.setHSCode(null);
				item.setCustomerOrderNumber(null);
				item.setDeereOrderNumber(null);
				item.setCountryOfOrigin(null);
				item.setUnitPrice(assignDoubleXMLPath(ib, ob, path, root,CW1XMLPath.LINE_UNIT_PRICE.replace("%%%", ""+i), null));
				item.setQuantity(assignDoubleXMLPath(ib, ob, path, root,CW1XMLPath.LINE_QUANTITY.replace("%%%", ""+i), null));
				item.setWeight(assignDoubleXMLPath(ib, ob, path, root,CW1XMLPath.LINE_GROSS_WEIGHT.replace("%%%", ""+i), null));
				item.setValue(assignDoubleXMLPath(ib, ob, path, root,CW1XMLPath.LINE_VALUE.replace("%%%", ""+i), null));
				item.setCaseNumber(assignXMLPath(ib, ob, path, root,CW1XMLPath.LINE_CASE_NUMBER.replace("%%%", ""+i), null));
				item.setLength(assignDoubleXMLPath(ib, ob, path, root,CW1XMLPath.LINE_LENGTH.replace("%%%", ""+i), null));
				item.setWidth(assignDoubleXMLPath(ib, ob, path, root,CW1XMLPath.LINE_WIDTH.replace("%%%", ""+i), null));
				item.setHeight(assignDoubleXMLPath(ib, ob, path, root,CW1XMLPath.LINE_HEIGHT.replace("%%%", ""+i), null));
				item.setGw(assignDoubleXMLPath(ib, ob, path, root,CW1XMLPath.LINE_GROSS_WEIGHT.replace("%%%", ""+i), null));
				item.setNetweight(assignDoubleXMLPath(ib, ob, path, root,CW1XMLPath.LINE_NET_WEIGHT.replace("%%%", ""+i), null));
				item.setType(assignXMLPath(ib, ob, path, root,CW1XMLPath.LINE_TYPE.replace("%%%", ""+i), null));
				
				
				
				ib.addLineItem(item);
				
			}
			ib.setInvoiceCW(invoiceweight);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			ob.addError(Error.LINE_GW);
		}
		if (ib.getHawbCW()!= null && ib.getInvoiceCW() !=null && ib.getHawbCW().doubleValue() < ib.getInvoiceCW().doubleValue())
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
					else if (!StringUtils.isNull(code) && code.startsWith(CW1XMLPath.PAYMENT_TERMS_REFERENCE_TYPE))
					{
						ib.addPaymentTerm(value);
					}
					else if (!StringUtils.isNull(code) && code.startsWith(CW1XMLPath.SHIPMENT_ID))
					{
						ib.setShipmentId(value);
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
		
		// Legs count
		try {
			XPathExpression transportExpr = path.compile(CW1XMLPath.TRANSPORT_STAGE);
			NodeList transportstageNodes = (NodeList)transportExpr.evaluate(root, XPathConstants.NODESET);
			
			ib.setLegCount(transportstageNodes.getLength());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			ob.addError(Error.NO_TRANSPORT_STAGE);
		}
		
		
		//Partners
		try {
			XPathExpression lineExpr = path.compile(CW1XMLPath.PARTNERS);
			NodeList partnerNodes = (NodeList)lineExpr.evaluate(root, XPathConstants.NODESET);
			for (int i=1;i<=partnerNodes.getLength();i++)
			{
				Partner partner = new Partner();
				partner.setId(assignXMLPath(ib, ob, path, root,CW1XMLPath.PARTNER_ID.replace("%%%", ""+i), null));
				partner.setRole(assignXMLPath(ib, ob, path, root,CW1XMLPath.PARTNER_ROLE.replace("%%%", ""+i), null));
				partner.setCAAD1(assignXMLPath(ib, ob, path, root,CW1XMLPath.PARTNER_NAME.replace("%%%", ""+i), null));
				partner.setCAAD1A(assignXMLPath(ib, ob, path, root,CW1XMLPath.PARTNER_LINE1.replace("%%%", ""+i), null));
				partner.setCAAD1B(assignXMLPath(ib, ob, path, root,CW1XMLPath.PARTNER_LINE2.replace("%%%", ""+i), null));
				partner.setCAAD2(assignXMLPath(ib, ob, path, root,CW1XMLPath.PARTNER_CITY.replace("%%%", ""+i), null) + " " + assignXMLPath(ib, ob, path, root,CW1XMLPath.PARTNER_POSTCODE.replace("%%%", ""+i), null));
				
				String countryLine = "";
				String country = assignXMLPath(ib, ob, path, root,CW1XMLPath.PARTNER_COUNTRY.replace("%%%", ""+i), null);
				String state = assignXMLPath(ib, ob, path, root,CW1XMLPath.PARTNER_STATE.replace("%%%", ""+i), null);
				String countryCode = assignXMLPath(ib, ob, path, root,CW1XMLPath.PARTNER_COUNTRYCODE.replace("%%%", ""+i), null);

				if (!StringUtils.isNull(state))
					countryLine = state + " - ";
				if (!StringUtils.isNull(country))
					countryLine += country;
				else if (!StringUtils.isNull(countryCode))
					countryLine += countryCode;
				partner.setCAAD4(countryLine);
				ib.addPartner(partner);
				
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			ob.addError(Error.LINE_GW);
		}
		
		
		System.out.println(ib);
		
		System.out.println("INPUTBEAN IS COMPLETED WITH XML STUFF....");
			
	}
	
	private static Double assignDoubleXMLPath(InputBean ib,OutputBean ob, XPath path, Element root, String xmlpath,Error error)
	{
		String s = assignXMLPath(ib, ob, path, root, xmlpath, error);
				
		try
		{
			return Double.parseDouble(s);
		}
		catch (Exception e)
		{
			if (error != null)
				ob.addError(error);
			return null;
		}
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
		ob.setOrderNumber(ib.getOrderNo());
		ob.setInvoiceNumber(ob.getInvoiceNumber());
		
		if (ib.getPartner(Epartner.BUYER)!=null)
		{
			ob.setBuyerId(ib.getPartner(Epartner.BUYER).getId());
		}
		else
		{
			ob.addWarning(Warning.NO_BUYER_ID);
		}
		// do all calculations that allow to complete the ob with data available in ib
		
		System.out.println("DOING CALCULATIONS AND COMPLETING OUTPUTBEAN....");
		
		EDICharge a320 = ib.getEdiCharge("A320"); // air freight
		EDICharge d140 = ib.getEdiCharge("D140"); // forwarding fees
		
		double F = 0.0;
		boolean fMissing = false;
		try
		{
			F = ib.getFreightHAWBCharge().getValue() * ib.getInvoiceCW() / ib.getHawbCW();
		}
		catch ( Exception e)
		{
			fMissing = true;
		}
		double AC = 0.0;
		boolean aCMissing = false;
		try
		{
			for (HAWBCharge hawbCharge: ib.getOtherHAWBCharges().values())
			{
				AC = AC + hawbCharge.getValue().doubleValue();
			}
			AC = AC * ib.getInvoiceCW() / ib.getHawbCW();
		}
		catch (Exception e)
		{
			aCMissing = true;
		}
		
		
		if (a320 == null)
		{
			if (d140 == null)
			{
				// do nothing
			}
			else if (d140.getValue() == null || d140.getValue().doubleValue() == 0.0)
			{
				if (fMissing)
					ob.addError(Error.NO_FREIGHT_CHARGE);
				if (aCMissing)
					ob.addError(Error.NO_OTHER_CHARGE);
				
				d140.updateValue(F + AC);
				ob.addChargeToComplete(d140);
			}
			else // d140 has a value
			{
				// do nothing, d140 keeps same value
				ob.addChargeToComplete(d140);
			}
		}
		else if (a320.getValue() == null || a320.getValue().doubleValue() == 0.0)
		{
			if (d140 == null)
			{
				if (fMissing)
					ob.addError(Error.NO_FREIGHT_CHARGE);
				if (aCMissing)
					ob.addError(Error.NO_OTHER_CHARGE);
				
				a320.updateValue(F + AC);
				ob.addChargeToComplete(a320);
			}
			else if (d140.getValue() == null || d140.getValue().doubleValue() == 0.0)
			{
				if (fMissing)
					ob.addError(Error.NO_FREIGHT_CHARGE);
				if (aCMissing)
					ob.addError(Error.NO_OTHER_CHARGE);
				
				d140.updateValue(AC);
				a320.updateValue(F);
				ob.addChargeToComplete(d140);
				ob.addChargeToComplete(a320);
			}
			else // d140 has a value
			{
				if (fMissing)
					ob.addError(Error.NO_FREIGHT_CHARGE);
				
				a320.updateValue(F);
				ob.addChargeToComplete(a320);
				ob.addChargeToComplete(d140);
			}
		}
		else // a320 has a value
		{
			ob.addChargeToComplete(a320);
			if (d140 == null)
			{
				// do nothing, a320 keeps same value
			}
			else if (d140.getValue() == null || d140.getValue().doubleValue() == 0.0)
			{
				if (aCMissing)
					ob.addError(Error.NO_OTHER_CHARGE);
				
				d140.updateValue(AC);
				ob.addChargeToComplete(d140);
			}
			else // d140 has a value
			{
				// do nothing, d140 keeps same value, a320 keeps same value
				ob.addChargeToComplete(d140);
			}
		}
		
		EDICharge i138 = ib.getEdiCharge("I138");
		if (i138 != null && i138.getValue() != null && i138.getValue().doubleValue() > 0.0)
		{
			ob.addChargeToComplete(i138);
		}

		// update other charges
		for (String chargeCode:ib.getEdiChargesKeys())
		{
			if (!StringUtils.isInList(chargeCode, "I132","I138","D980","A320","D140"))
			{
				EDICharge charge = ib.getEdiCharge(chargeCode);
				if (charge.getValue() != null && charge.getValue().doubleValue() > 0)
				{
					ob.addChargeToComplete(charge);
				}
			}
		}
		
		//insurance
		EDICharge d980 = ib.getEdiCharge("D980");
		if (d980 != null && d980.getValue() != null && d980.getValue().doubleValue() > 0.0)
		{
			ob.addChargeToComplete(d980);
		}
		else // calculate insurance cost
		{
			// according to Bruno, no need to handle this case (see COM SFT table, *1.1 and factor based on target country
		}
		
		
		
		TextBuilder txb = new TextBuilder();
		String txtEncoded64 = "UGFuYWxwaW5hIEVjdWFkb3IgUy5BLjtQYW5hbHBpbmEgRWN1YWRvciBTLkEuOzE3OTA3MzAxNjYwMDE7MDE7MDAxOzAwMTswMDAwMDAxNTc7QXYuIDYgZGUgRGljaWVtYnJlIE4zMi0zMTIgeSBKZWFuIEIuIEJvdXNzaW5nYXVsdCBFZGlmaWNpbyBUNiwgUGlzbyA1LCBPZmljaW5hIDUwMzswOC8wNS8yMDIwO0F2ZW5pZGEgRWwgSW5jYSwgeSBBdmVuaWRhIEFtYXpvbmFzIDQwNiwgRTQtMTgxOzA1OTA7U0k7MDQ7O0hBTExJQlVSVE9OIExBVElOIEFNRVJJQ0EgU1JMOzE3OTE4NTE2MzYwMzI7MzEyLjAwOzAuMDA7W0lUMzswMDAwOzAuMDA7MDAwMDswLjAwXVtJVDI7MjszMTIuMDA7MTI7MjQuMDBdMC4wMDszMzYuMDA7RE9MQVI7W1BBRzIwOzMzNi4wMDswO0RpYXNdO1tERVRDQ0w7MDAwO0lNUE9SVCBDVVNUT01TIENMRUFSQU5DRSBDSEFSR0VTOzEuMDA7MjAwLjAwOzAuMDA7MjAwLjAwOzs7REVUXVtJRDM7MC4wMDswLjAwOzAuMDA7MC4wMF1bSUQyOzI7MTI7MjAwLjAwOzI0LjAwMDBdO1tERVRMUkZMOzAwMDtSRUlNQlVSU0VNRU5UIFdBUkVIT1VTRSBGT1JLTElGVCBBTkQgT1RIRVIgRVFVSVBNRU5UOzEuMDA7MTEyLjAwOzAuMDA7MTEyLjAwOzs7REVUXVtJRDM7MC4wMDswLjAwOzAuMDA7MC4wMF1bSUQyOzAuMDA7MDA7MC4wMDswLjAwXWVtYWlsQ2xpZW50ZT1OZWxzb24uTWVuZGV6QHBhbmFscGluYS5jb207Q09ESUdPSU5URVJOT1NBUD0wMDAwMDAxNTc7Q09ESUdPSU5URVJOT1NBUENMSUVOVEU9Njc5MDY4OTI7UkVGRVJFTkNFPTtIQVdCL0JMPVNBTzc1NTg4NDE=";
		
		boolean parseOk = txb.analyze( txtEncoded64, ob ); // use ob.addError() if any critical error during process, and return false
		
		if (parseOk)
		{
			String encoded64Result = txb.completeTxtFileWithCharges ( ob.getChargesToComplete());
			
			ob.setBinaryFile(encoded64Result);
		}
		
		
		
	}
	
	public static String generateXMLOutput ( OutputBean ob)
	{
		if ( 1 < 0)
		{
			ob = new OutputBean();
			ob.setOrderNumber("TESTE2EJL007C");
			ob.setBuyerId("67906544");
			ob.setInvoiceNumber("123456");
			ob.setPdf(false);
			ob.addWarning(Warning.INVOICE_WEIGHT_EXCEEDS_SHIPMENT_WEIGHT);

			ob.setBinaryFile("UGFuYWxwaW5hIEVjdWFkb3IgUy5BLjtQYW5hbHBpbmEgRWN1YWRvciBTLkEuOzE3OTA3MzAxNjYwMDE7MDE7MDAxOzAwMTswMDAwMDAxNTc7QXYuIDYgZGUgRGljaWVtYnJlIE4zMi0zMTIgeSBKZWFuIEIuIEJvdXNzaW5nYXVsdCBFZGlmaWNpbyBUNiwgUGlzbyA1LCBPZmljaW5hIDUwMzswOC8wNS8yMDIwO0F2ZW5pZGEgRWwgSW5jYSwgeSBBdmVuaWRhIEFtYXpvbmFzIDQwNiwgRTQtMTgxOzA1OTA7U0k7MDQ7O0hBTExJQlVSVE9OIExBVElOIEFNRVJJQ0EgU1JMOzE3OTE4NTE2MzYwMzI7MzEyLjAwOzAuMDA7W0lUMzswMDAwOzAuMDA7MDAwMDswLjAwXVtJVDI7MjszMTIuMDA7MTI7MjQuMDBdMC4wMDszMzYuMDA7RE9MQVI7W1BBRzIwOzMzNi4wMDswO0RpYXNdO1tERVRDQ0w7MDAwO0lNUE9SVCBDVVNUT01TIENMRUFSQU5DRSBDSEFSR0VTOzEuMDA7MjAwLjAwOzAuMDA7MjAwLjAwOzs7REVUXVtJRDM7MC4wMDswLjAwOzAuMDA7MC4wMF1bSUQyOzI7MTI7MjAwLjAwOzI0LjAwMDBdO1tERVRMUkZMOzAwMDtSRUlNQlVSU0VNRU5UIFdBUkVIT1VTRSBGT1JLTElGVCBBTkQgT1RIRVIgRVFVSVBNRU5UOzEuMDA7MTEyLjAwOzAuMDA7MTEyLjAwOzs7REVUXVtJRDM7MC4wMDswLjAwOzAuMDA7MC4wMF1bSUQyOzAuMDA7MDA7MC4wMDswLjAwXWVtYWlsQ2xpZW50ZT1OZWxzb24uTWVuZGV6QHBhbmFscGluYS5jb207Q09ESUdPSU5URVJOT1NBUD0wMDAwMDAxNTc7Q09ESUdPSU5URVJOT1NBUENMSUVOVEU9Njc5MDY4OTI7UkVGRVJFTkNFPTtIQVdCL0JMPVNBTzc1NTg4NDE=");
		}
		StringBuffer sb = new StringBuffer();
		
		sb.append("<RPSReply>");
		if(!StringUtils.isNull(ob.getOrderNumber()))
		{
			sb.append("<OrderNumber>"+ob.getOrderNumber()+"</OrderNumber>");
		}
		if(!StringUtils.isNull(ob.getInvoiceNumber()))
		{
			sb.append("<InvoiceNumber>"+ob.getInvoiceNumber()+"</InvoiceNumber>");
		}
		if(!StringUtils.isNull(ob.getBuyerId()))
		{
			sb.append("<BuyerID>"+ob.getBuyerId()+"</BuyerID>");
		}
		
		
		if(!StringUtils.isNull(ob.getBinaryFile()))
		{
			sb.append("<Filename>"+ob.getFilename()+"</Filename>");
			sb.append("<format>" + (ob.isPdf()?"PDF":"TXT") + "</format>");
			sb.append("<code>OK</code>");
		}
		else
		{
			sb.append("<code>KO</code>");
		}
		
		if (ob.getErrors()==null || ob.getErrors().size() == 0)
		{
			sb.append("<errors/>");
		}
		else
		{
			sb.append("<errors>");
			for (Error error:ob.getErrors())
			{
				sb.append("<error>" + error.getText() + "</error>");
			}
			sb.append("</errors>");
		}
		
		if (ob.getWarnings()==null || ob.getWarnings().size() == 0)
		{
			sb.append("<warnings/>");
		}
		else
		{
			sb.append("<warnings>");
			for (Warning warning:ob.getWarnings())
			{
				sb.append("<warning>" + warning.getText() + "</warning>");
			}
			sb.append("</warnings>");
		}
		
		if (!StringUtils.isNull(ob.getBinaryFile()))
		{
			sb.append("<binaryFile>" + ob.getBinaryFile() + "</binaryFile>");
		}
		sb.append("</RPSReply>");
		
	      return sb.toString();
	}
}
