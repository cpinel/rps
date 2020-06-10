package com.dsv.rps.decoder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class JsonDecoder {

	public static void main ( String [] args ) {
		
		try {
			System.out.println("READING ...");
	//		String json = Files.readString(Paths.get("C:\\Users\\didie\\Desktop\\test rps\\sample.json"), StandardCharsets.UTF_8);
			String json ="";
			System.out.println("PARSING ...");
			Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
	
			System.out.println("FINDING 1 ...");
			String shipmentId = JsonPath.read(document, "$.ShipmentInstructionMessage.ShipmentInstruction.ShipmentDetails.ShipmentId");
			System.out.println("shipmentId :" + shipmentId);
			
			System.out.println("FINDING 2 ...");
			List<String> desc = JsonPath.read(document, "$.ShipmentInstructionMessage.ShipmentInstruction.ChargeLines.ChargeLine[?(@.ChargeCode=='FRT')].Description");
			if (desc!=null)
				System.out.println("description of charge line code FRT : " + desc.get(0) + " out of " + desc.size());
			
			List<String> curr = JsonPath.read(document, "$.ShipmentInstructionMessage.ShipmentInstruction.ChargeLines.ChargeLine[?(@.ChargeCode=='FRT')].CostAmount.@Currency");
			if (curr!=null)
				System.out.println("currency of charge line code FRT : " + curr.get(0) + " out of " + curr.size());
			
			List<String> amt = JsonPath.read(document, "$.ShipmentInstructionMessage.ShipmentInstruction.ChargeLines.ChargeLine[?(@.ChargeCode=='FRT')].CostAmount.#text");
			if (amt!=null)
				System.out.println("amount of charge line code FRT : " + amt.get(0) + " out of " + amt.size());
			
			List<String> consigneecountry = JsonPath.read(document, "$.ShipmentInstructionMessage.ShipmentInstruction.ShipmentDetails.Parties.Party[?(@.@Role=='CN')].Address.Country");
			if (consigneecountry!=null)
				System.out.println("consigneecountry : " + consigneecountry.get(0) + " out of " + consigneecountry.size());		
	
			
			List<Map<String, Object>> consigneeaddress = JsonPath.read(document, "$.ShipmentInstructionMessage.ShipmentInstruction.ShipmentDetails.Parties.Party[?(@.@Role=='CN')].Address");
			if (consigneeaddress!=null)
			{
				System.out.println("Object consigneeaddress : " + consigneeaddress.get(0) + " out of " + consigneeaddress.size());
				System.out.println("\tName : " + consigneeaddress.get(0).get("Name"));
				System.out.println("\tLine1 : " + consigneeaddress.get(0).get("Line1"));
				System.out.println("\tLine2 : " + consigneeaddress.get(0).get("Line2"));
				System.out.println("\tCity : " + consigneeaddress.get(0).get("City"));
				System.out.println("\tCountry : " + consigneeaddress.get(0).get("CountryCode") + " " + consigneeaddress.get(0).get("Country"));
				
			}
			
			String txtfile = JsonPath.read(document, "$.ShipmentInstructionMessage.ShipmentInstruction.CustomsDeclaration.CommercialInvoices.CommercialInvoice.Comments.Text");
			System.out.println("txtfile : " + txtfile);
			byte[] decoded = Base64.getDecoder().decode(txtfile.getBytes("UTF-8"));
			System.out.println(" txt file décodé :" + new String(decoded, StandardCharsets.UTF_8));
			
			String pgpfile = null;
			
		//	String pgpfile = Files.readString(Paths.get("C:\\Users\\didie\\Desktop\\test rps\\pgp.txt"), StandardCharsets.UTF_8);
			System.out.println("pgpfile : " + pgpfile);
			byte[] bytes = pgpfile.getBytes("UTF-8");
			String encoded = Base64.getEncoder().encodeToString(bytes);
			byte[] decoded3 = Base64.getDecoder().decode(encoded);
			System.out.println(" pgp file décodé :" + new String(decoded3, StandardCharsets.UTF_8));
			
		} catch ( Exception e)
		{
			e.printStackTrace();
		}
	}
}
