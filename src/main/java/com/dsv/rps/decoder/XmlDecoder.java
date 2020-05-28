package com.dsv.rps.decoder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class XmlDecoder {

	public static void main ( String [] args ) {
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			System.out.println("READING ...");
			File fileXML = new File("C:\\Users\\didie\\Desktop\\test rps\\sample.xml.txt");

			System.out.println("PARSING ...");
			Document xml = builder.parse(fileXML);
			Element root = xml.getDocumentElement();
			XPathFactory xpf = XPathFactory.newInstance();
			XPath path = xpf.newXPath();
			
			System.out.println("FINDING 1 ...");
			String shipmentId = path.evaluate("/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/ShipmentId", root);
			System.out.println("shipmentId : " + shipmentId);
			
			System.out.println("FINDING 2 ...");
			String desc = path.evaluate("/ShipmentInstructionMessage/ShipmentInstruction/ChargeLines/ChargeLine[ChargeCode='FRT']/Description",root);
			System.out.println("description of charge line code FRT : " + desc);
			
			String curr = path.evaluate("/ShipmentInstructionMessage/ShipmentInstruction/ChargeLines/ChargeLine[ChargeCode='FRT']/CostAmount/@Currency",root);
			System.out.println("currency of charge line code FRT : " + curr);
			
			String amt =  path.evaluate("/ShipmentInstructionMessage/ShipmentInstruction/ChargeLines/ChargeLine[ChargeCode='FRT']/CostAmount",root);
			System.out.println("amount of charge line code FRT : " + amt);
			
			String consigneecountry = path.evaluate("/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[@Role='CN']/Address/Country",root);
			System.out.println("consigneecountry : >" + consigneecountry + "<");
			
			String ffcountry = path.evaluate("/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[@Role='FF']/Address/Country",root);
			System.out.println("ffcountry : >" + ffcountry + "<");
	
			
			String consigneeaddress = path.evaluate("/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[@Role='CN']/Address",root);
			if (consigneeaddress!=null)
			{
				System.out.println("Full address text, to be splitted : " +  consigneeaddress);
				/*System.out.println("Object consigneeaddress : " + consigneeaddress.get(0) + " out of " + consigneeaddress.size());
				System.out.println("\tName : " + consigneeaddress.get(0).get("Name"));
				System.out.println("\tLine1 : " + consigneeaddress.get(0).get("Line1"));
				System.out.println("\tLine2 : " + consigneeaddress.get(0).get("Line2"));
				System.out.println("\tCity : " + consigneeaddress.get(0).get("City"));
				System.out.println("\tCountry : " + consigneeaddress.get(0).get("CountryCode") + " " + consigneeaddress.get(0).get("Country"));
				*/
			}
			
			String txtfile = path.evaluate("/ShipmentInstructionMessage/ShipmentInstruction/CustomsDeclaration/CommercialInvoices/CommercialInvoice/Comments/Text",root);
			System.out.println("txtfile *********** : " + txtfile);
			byte[] decoded = Base64.getDecoder().decode(txtfile.getBytes("UTF-8"));
			System.out.println("txt file decoded ********* :" + new String(decoded, StandardCharsets.UTF_8));
			
			/*
			
			String pgpfile = Files.readString(Paths.get("C:\\Users\\didie\\Desktop\\test rps\\pgp.txt"), StandardCharsets.UTF_8);
			System.out.println("pgpfile : " + pgpfile);
			byte[] bytes = pgpfile.getBytes("UTF-8");
			String encoded = Base64.getEncoder().encodeToString(bytes);
			byte[] decoded3 = Base64.getDecoder().decode(encoded);
			System.out.println(" pgp file décodé :" + new String(decoded3, StandardCharsets.UTF_8));
			*/
		} catch ( Exception e)
		{
			e.printStackTrace();
		}
	}
}
