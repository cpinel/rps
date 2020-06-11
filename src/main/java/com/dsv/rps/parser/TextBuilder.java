package com.dsv.rps.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.dsv.rps.bean.ChargeLine;
import com.dsv.rps.bean.Invoice;

public class TextBuilder {
	private static Logger logger = Logger.getLogger(TextBuilder.class.getName());
	private static String byteExample = "IElEICAgICAgREVFUkUgIDIwMTUwMjEyICAxMzI1IFBSTlQgIFhQQU5QUAoxU09MRCBCWTogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIENPTU1FUkNJQUwgSU5WT0lDRSAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFBBR0UgICAgMQogICAgREVFUkUgJiBDT01QQU5ZCiAgICBXT1JMRFdJREUgTE9HSVNUSUNTIE9QRVJBVElPTlMgICAgICAgICAgICBJTlZPSUNFIE5POiAyMDAzIDM0MDEzNCBEQVRFOiAxMEZFQjE1IERFU1RJTkFUSU9OOiAyMDIwMDNQQQogICAgMzQwMCA4MFRIIFNUUkVFVCAgICAgICAgICAgICAgICAgICAgICAgICAgREVFUkUgT1JERVIgTk86IDIwMDM3NDkwMDMgICAgICAgICBEQVRFOiAwOUZFQjE1IFRZUEU6IFNOCiAgICBNT0xJTkUgICAgICAgIElMICA2MTI2NQogICAgVU5JVEVEIFNUQVRFUyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQ1VTVE9NRVIgT1JERVIgTk86CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBFWElUOiBNSUxBTiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNISVAgREFURTogMTBGRUIxNQogSU5WT0lDRSBUTzogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgU0hJUE1FTlQgSUQ6IDIwMDMgIFRFU1QyMDIwMDNQQUFUICAgRk9SV0FSREVSOiBYUEFOUFAKICAgIEpPSE4gREVFUkUgQlJBU0lMIExUREEKICAgIFJVQSBTRVJHSU8gRkVSTkFOREVTIEJPUkdFUyBTT0FSRVMsCiAgICBESVNUUklUTyBJTkRVU1RSSUFMICAgICAgICAgICAgICAgICAgICAgICBQQVlNRU5UIFRFUk1TOgogICAgMTMwNTQtNzA5IENBTVBJTkFTIC0gU1AgICAgICAgICAgICAgICAgICAgICAgRFVFIFRIRSBGSVJTVCBEQVkgT0YgVEhFIEZPVVJUSCBNT05USCBGT0xMT1dJTkcgVEhFCiAgICBCUkFaSUwgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBNT05USCBPRiBTSElQTUVOVCBPRiBHT09EUy4KICAgIENOUEo4OS42NzQuNzgyLzAwMTMtOTEKCiBTSElQIFRPOgogICAgSk9ITiBERUVSRSBCUkFTSUwgTFREQQogICAgUlVBIFNFUkdJTyBGRVJOQU5ERVMgQk9SR0VTIFNPQVJFUywKICAgIERJU1RSSVRPIElORFVTVFJJQUwKICAgIDEzMDU0LTcwOSBDQU1QSU5BUyAtIFNQICAgIEJSQVpJTAogICAgQ05QSjg5LjY3NC43ODIvMDAxMy05MSAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBTU9VTlQgSU4gVVNECgogU0hJUFBJTkcgTUFSS1M6ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEZPQiBGQUJSSUNBICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDExLjE0CgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIElOTEFORCBGUkVJR0hUICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA3LjU5CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQUlSRlJFSUdIVAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEZPUldBUkRFUiBGRUVTCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgSU5TVVJBTkNFCgoKCgoKCgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIERBUCBWSVJBQ09QQVMgQUlSUE9SVCAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDE4LjczCgoKCgoKCgoKCgogICAgICAgMSBQQUNLQUdFKFMpICAgICAgMTQuOTY4IEdST1NTIEtHICAgICAgICAgIC4xOTEgTkVUIEtHCiAgICAgICAxIFBBQ0tBR0UoUykgICAgICAzMy4wMDAgR1JPU1MgTEJTICAgICAgICAgLjQyMSBORVQgTEJTICAgICAgICAgICAgIENVQklDIEZFRVQKMVNPTEQgQlk6ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBDT01NRVJDSUFMIElOVk9JQ0UgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBQQUdFICAgIDIKICAgIERFRVJFICYgQ09NUEFOWQogICAgV09STERXSURFIExPR0lTVElDUyBPUEVSQVRJT05TICAgICAgICAgICAgSU5WT0lDRSBOTzogMjAwMyAzNDAxMzQgREFURTogMTBGRUIxNSBERVNUSU5BVElPTjogMjAyMDAzUEEKICAgIDM0MDAgODBUSCBTVFJFRVQgICAgICAgICAgICAgICAgICAgICAgICAgIERFRVJFIE9SREVSIE5POiAyMDAzNzQ5MDAzICAgICAgICAgREFURTogMDlGRUIxNSBUWVBFOiBTTgogICAgTU9MSU5FICAgICAgICBJTCAgNjEyNjUgICAgICAgICAgICAgICAgICAgRVhJVDogTUlMQU4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICBTSElQIERBVEU6IDEwRkVCMTUKICAgIFVOSVRFRCBTVEFURVMKCgogKDA1MDQgKSBHRU5FUkFMIExJQ0VOU0UgLSBOTFIKICAgICAgICAgVEhFU0UgQ09NTU9ESVRJRVMgV0VSRSBFWFBPUlRFRCBGUk9NIFRIRSBVTklURUQKICAgICAgICAgU1RBVEVTIElOIEFDQ09SREFOQ0UgV0lUSCBUSEUgRVhQT1JUIEFETUlOSVNUUkFUSU9OCiAgICAgICAgIFJFR1VMQVRJT05TLiAgRElWRVJTSU9OIENPTlRSQVJZIFRPIFUuUy4gTEFXIFBST0hJQklURUQuCgogKDA1OTEgKSBXRSBIRVJFQlkgQ0VSVElGWSBUSEFUIFRIRSBTVEFURU1FTlRTIEhFUkVJTiBBUkUgVFJVRSBBTkQKICAgICAgICAgQ09SUkVDVCBJTiBBTEwgUkVTUEVDVFMuCiAgICAgICAgIERFRVJFICYgQ09NUEFOWQoKICgwNjU4ICkgQUVTIFBPU1QgMzYyMzgyNTgwMDAgK0RPRQoKIFBBQ0tBR0UgTElTVDoKICAgICAgICAgICBDQVNFICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUEFDS0FHRSAgICAgR1JPU1MgICAgIE5FVCAgICAgICAgIERJTUVOU0lPTlMgICAgICAgIENVQklDCiAgICAgICAgICAgVFlQRSAgIFBBQ0tBR0UgREVTQ1JJUFRJT04gICAgICAgICAgIE5VTUJFUiAgICAgICBMQlMgICAgICBMQlMgICAgIExHVEggICBXRFRIICAgIEhHVCAgICBGRUVUCgogICAgICAgICAgIENUTiAgICBDQVJUT04gICAgICAgICAgICAgICAgICAgICAgICBDNTI2MzMgICAgIDMzLjAwMCAgICAgICAuNDIxICAgMjEwIFggICAxMCBYCjFTSElQUEVEIEJZOiAgICAgICAgICAgICAgICAgICAgICAgSU5WT0lDRSBOTzogMjAwMyAzNDAxMzQgREFURTogMTBGRUIxNSBERVNUSU5BVElPTjogMjAyMDAzUEEgICBQQUdFICAgIDMKICAgIERFRVJFICYgQ09NUEFOWSAgICAgICAgICAgICAgICAgICAgICBERUVSRSBPUkRFUiBOTzogMjAwMzc0OTAwMyAgREFURTogMDlGRUIxNSBUWVBFOiBTTgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIENVU1RPTUVSIE9SREVSIE5POgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNISVBNRU5UIElEOiAyMDAzICBURVNUMjAyMDAzUEFBVCAgIExJQ0VOU0UgTk86CgogICAgICAgICAgICAgICAgICAgICBDICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFAgICAgICAgICAgICAgICAgICAgICBVTklUICAgICBOICBFWFRFTkRFRCAgVgogICAgUEFSVCAgICAgUEFDS0FHRSBTICBQICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNISVAgIEsgICBPUkQgICBFWFRFTkRFRCAgICBQUklDRSAgICBFICAgUFJJQ0UgICAgQQogICBOVU1CRVIgICAgTlVNQkVSICBPICBUICAgICAgICAgREVTQ1JJUFRJT04gICAgICAgICAgIFFUWSAgIEcgICBRVFkgICAgTkVUIEtHICAgICAgVVNEICAgICBUICAgIFVTRCAgICAgVAoKIFJFNjk1ODEgICAgICBDNTI2MzMgVVMgICBUSEVSTU9TVEFUICAgICAgICAgICAgICAgICAgICAgMiAgICAgICAgMiAgICAgICAuMTkxICAgICAgNS41NyAgICAgICAgICAxMS4xNAoKIFRPVEFMUyBGT1IgT1JERVIgMjAwMzc0OTAwMyAgICAgICAgIDEgVE9UQUwgTElORSBJVEVNUyAgICAgICAgICAgICAgICAgIC4xOTEgICAgICAgICAgICAgICAgICAgIDExLjE0CgoxU0hJUFBFRCBCWTogICAgICAgICAgICAgICAgICAgICAgICAgIEVYUE9SVCBQQVJUUyBQQUNLSU5HIExJU1QgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUEFHRSAgICA0CiAgICAgICBERUVSRSAmIENPTVBBTlkKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIElOVk9JQ0UgTk86IDIwMDMgMzQwMTM0ICAxMEZFQjE1IERFU1RJTkFUSU9OOiAyMDIwMDNQQQogSU5WT0lDRSBUTzogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgRVhJVDogTUlMQU4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICBTSElQIERBVEU6IDEwRkVCMTUKICAgICAgIEpPSE4gREVFUkUgQlJBU0lMIExUREEgICAgICAgICAgICAgICAgIFNISVBNRU5UIElEOiAyMDAzICBURVNUMjAyMDAzUEFBVCAgIEZPUldBUkRFUjogWFBBTlBQCiAgICAgICBSVUEgU0VSR0lPIEZFUk5BTkRFUyBCT1JHRVMgU09BUkVTLAoKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIERFRVJFCiAgICBQQUNLQUdFICAgICAgUEFSVCAgICAgICAgU0hJUCAgICAgIFMgIFAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBPUkRFUiAgICAgQ1VTVE9NRVIgICAgICAgIEJJTgogICAgTlVNQkVSICAgICAgTlVNQkVSICAgICAgIFFUWSAgIEJORCBPICBEICAgICAgIERFU0NSSVBUSU9OICAgICAgICAgICAgTlVNQkVSICBPUkRFUiBOVU1CRVIgICAgTE9DQVRJT04KCiAgICBDNTI2MzMgICBSRTY5NTgxICAgICAgICAgICAgIDIgICAgIFVTICAgVEhFUk1PU1RBVCAgICAgICAgICAgICAgICAgICA0OTAwMyAgICAgICAgICAgICAgICAgRTM4N0EwMUIKCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQ0FTRSAgICAgICBESU1FTlNJT05TIChNTSkgICAgICBHUk9TUyAgIE5FVCAgIENVQklDCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgVFlQRSAgIExHVEggICBXRFRIICAgSEdUICAgICAgICAgICBMQlMgIExCUyAgIEZFRVQKCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQ1ROICAgICAyMTAgICAgIDEwICAgICAgICAgICAzMy4wMDAgICAgICAgLjQyMQoKCiBUT1RBTFMgRk9SIENPTU1FUkNJQUwgSU5WT0lDRSAyMDAzIDM0MDEzNCAgIExJTkUgSVRFTVMgICAgIDEgICAgICAgR1JPU1MgTEJTICAgICAgMzMuMDAwCgoKICAgICAgICAgICAgICAgICAgICAgICAgQ0FTRQogICAgICAgICAgICAgICAgIFFUWSAgICBUWVBFICAgIFBBQ0tBR0UgREVTQ1JJUFRJT04KCiAgICAgICAgICAgICAgICAgICAgMSAgICBDVE4gICAgIENBUlRPTgo=";

	private List<String> lines = new ArrayList<String>();
	private List<Integer> pageindex = new ArrayList<Integer>();

	private Invoice invoice = new Invoice();

	public List<Integer> getPageindex() {
		return pageindex;
	}

	public void setPageindex(List<Integer> pageindex) {
		this.pageindex = pageindex;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public TextBuilder() {

		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("rps.properties");) {

			// conf.load(inputStream);

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				// inputStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public String listFilesForFolder(final File folder) {

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				if (fileEntry.getAbsolutePath().contains(".txt"))
					return fileEntry.getAbsolutePath();

			}
		}
		return "";

	}

	private String readFile(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String sCurrentLine;
			int i = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				contentBuilder.append(sCurrentLine).append("\n");
				lines.add(sCurrentLine);
				if (sCurrentLine.contains("PAGE"))
					pageindex.add(new Integer(i));
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException ex) {
				}

		}
		return contentBuilder.toString();
	}

	private byte[] encodeFile64(byte[] bytes) {

		try {
			byte[] bytesEncoded;
			// Encode data on your side using BASE64
			bytesEncoded = Base64.getEncoder().encode(bytes);
			System.out.println("encoded value is " + new String(bytesEncoded));

			return bytesEncoded;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	private String decodeFile64(byte[] bytes) {

		try {

			// Decode data on other side, by processing encoded data
			byte[] valueDecoded = Base64.getDecoder().decode(bytes);
			logger.info("Decoded value is " + new String(valueDecoded));
			return new String(valueDecoded);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	private static Document getDocument(String fileName) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(fileName);
		return doc;
	}

	private static List<String> evaluateXPath(Document document, String xpathExpression) throws Exception {
		// Create XPathFactory object
		XPathFactory xpathFactory = XPathFactory.newInstance();

		// Create XPath object
		XPath xpath = xpathFactory.newXPath();

		List<String> values = new ArrayList<String>();
		try {
			// Create XPathExpression object
			XPathExpression expr = xpath.compile(xpathExpression);

			// Evaluate expression result on XML document
			NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++) {
				values.add(nodes.item(i).getNodeValue());
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return values;
	}

	public void extractHeaderInfo()

	{
		try {
			int endIdx = -1;
			// this is how to extract fixed informations base on the first line, SOLD BY,
			// identify tags
			if (lines.get(1).contains("SOLD BY")) {
				// get invoice no

				// line 3
				int beginIdx = lines.get(3).indexOf("INVOICE NO:") + 11;
				endIdx = lines.get(3).indexOf("DATE:");
				if (beginIdx >= 0)
					invoice.setInvoiceNo(lines.get(3).substring(beginIdx, endIdx).trim());
				// get date
				beginIdx = lines.get(3).indexOf("DATE:") + 5;
				endIdx = lines.get(3).indexOf("DESTINATION:");
				if (beginIdx >= 0)
					invoice.setDate(lines.get(3).substring(beginIdx, endIdx).trim());
				// get destination code
				beginIdx = lines.get(3).indexOf("DESTINATION:") + 12;
				endIdx = lines.get(3).length();
				if (beginIdx >= 0)
					invoice.setDestinationCode(lines.get(3).substring(beginIdx, endIdx).trim());

				// line 4

				// get DeerOrder No
				beginIdx = lines.get(4).indexOf("DEERE ORDER NO:") + 15;
				endIdx = lines.get(4).indexOf("DATE:");
				if (beginIdx >= 0)
					invoice.setDeereOrderNo(lines.get(4).substring(beginIdx, endIdx).trim());
				// line 7

				// get Exit
				beginIdx = lines.get(7).indexOf("EXIT:") + 5;
				endIdx = lines.get(7).indexOf("SHIP DATE:");
				if (beginIdx >= 0)
					invoice.setShipDate(lines.get(7).substring(beginIdx, endIdx).trim());
				// get shipmentDate
				beginIdx = lines.get(7).indexOf("SHIP DATE:") + 10;
				endIdx = lines.get(7).length();
				if (beginIdx >= 0)
					invoice.setShipDate(lines.get(7).substring(beginIdx, endIdx).trim());
				// line 8

				// get shipmentId
				beginIdx = lines.get(8).indexOf("SHIPMENT ID:") + 12;
				endIdx = lines.get(8).indexOf("FORWARDER:");
				if (beginIdx >= 0)
					invoice.setShipmentId(lines.get(8).substring(beginIdx, endIdx).trim());

			}
		} catch (Exception ex) {
		}
		System.out.println("[INVOICE]" + invoice.toString());
	}

	public void extractChargeLines() {
		// SHIPPING MARKS:
		int lineIndex = 0, beginIndex = 0;
		boolean firstchargeDetected = false;
		int idx = 0;
		for (String line : getLines()) {

			if (line.contains("SHIPPING MARKS:")) {
				{
					firstchargeDetected = true;
				}

			}
			if (firstchargeDetected) {
				beginIndex++;
			}
			if (line.trim().length() > 0) {
				if (firstchargeDetected) {

					ChargeLine t = new ChargeLine(line, lineIndex);
					this.getInvoice().getChargeLines().add(t);
					this.getInvoice().getChargeLinesIdx().put(t.getText(), idx);

					idx++;
				}

			}
			lineIndex++;
			if (firstchargeDetected && beginIndex > 20)
				break;
		}

	}

	public void parseContent(String decodefile) {
		int i = 0;
		String[] splitted = decodefile.split("\\r?\\n");
		for (String element : splitted) {

			lines.add(element);
			if (element.contains("PAGE"))
				pageindex.add(new Integer(i));
			i++;

		}

	}

	public void reCalculateAndReConstructChargeLines() {
		
		
		float total = 0;
 
		
		// read all except last
		for (int i = 0; i < this.getInvoice().getChargeLines().size() - 1; i++) {

			ChargeLine chargeline = this.getInvoice().getChargeLines().get(i);

			// this will reconstruct the rawline
			chargeline.reconstructRawLine();
			// sum for total on last charge line
			total += chargeline.getValue();
			int id = chargeline.getLineIndex();
			List<String> mylist = this.getLines();
			mylist.set(id, chargeline.getRawLine());

		}

		ChargeLine lastchargeline = this.getInvoice().getChargeLines().get(this.getInvoice().getChargeLines().size() - 1);
		lastchargeline.setValue(total);
		lastchargeline.reconstructRawLine();
		int id = lastchargeline.getLineIndex();
		List<String> mylist = this.getLines();
		mylist.set(id, lastchargeline.getRawLine());
	}

	public static void main(String[] args) {

		TextBuilder textBuilder = new TextBuilder();
	 

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);

		String decodefile = textBuilder.decodeFile64(TextBuilder.byteExample.getBytes());
		textBuilder.parseContent(decodefile);

		// init line

		for (Integer line : textBuilder.getPageindex()) {

			System.out.println("page index" + line);

		}

		textBuilder.extractHeaderInfo();
		try {
			// this method extracts the charges , with line index , text and eventually
			// amount, value will be set by 0
			textBuilder.extractChargeLines();
			// once we get the charges this part is reconstructing the text file with the
			// values from the charge lines

			/// lookup for AIRFREIGHT and inject test

			Integer lineIndex = textBuilder.getInvoice().getChargeLinesIdx().get("AIRFREIGHT");
			ChargeLine airFreightCharge = textBuilder.getInvoice().getChargeLines().get(lineIndex.intValue());
			airFreightCharge.setValue(new Float(34));

			logger.info("ENTER AIRFREIGHT:" + lineIndex);
			/// lookup for FORWARDER FEES and inject test
			Integer lineIndex2 = textBuilder.getInvoice().getChargeLinesIdx().get("FORWARDER FEES");
			ChargeLine forwarderCharge = textBuilder.getInvoice().getChargeLines().get(lineIndex2.intValue());
			forwarderCharge.setValue(new Float(77));

			logger.info("ENTER FORWARDER FEES:" + lineIndex2);

			textBuilder.reCalculateAndReConstructChargeLines();

			for (String line : textBuilder.getLines()) {

				logger.info(line);

			}

			logger.info(textBuilder.getInvoice().toString());
			// String xmlfile = readFile("c:/temp/HWAYBL_I-AHR.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
