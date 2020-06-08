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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

public class ProcessRps {
	private static Logger logger = Logger.getLogger(ProcessRps.class.getName());
	private static String byteExample = "IElEICAgICAgREVFUkUgIDIwMTUwMjEyICAxMzI1IFBSTlQgIFhQQU5QUAoxU09MRCBCWTogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIENPTU1FUkNJQUwgSU5WT0lDRSAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFBBR0UgICAgMQogICAgREVFUkUgJiBDT01QQU5ZCiAgICBXT1JMRFdJREUgTE9HSVNUSUNTIE9QRVJBVElPTlMgICAgICAgICAgICBJTlZPSUNFIE5POiAyMDAzIDM0MDEzNCBEQVRFOiAxMEZFQjE1IERFU1RJTkFUSU9OOiAyMDIwMDNQQQogICAgMzQwMCA4MFRIIFNUUkVFVCAgICAgICAgICAgICAgICAgICAgICAgICAgREVFUkUgT1JERVIgTk86IDIwMDM3NDkwMDMgICAgICAgICBEQVRFOiAwOUZFQjE1IFRZUEU6IFNOCiAgICBNT0xJTkUgICAgICAgIElMICA2MTI2NQogICAgVU5JVEVEIFNUQVRFUyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQ1VTVE9NRVIgT1JERVIgTk86CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBFWElUOiBNSUxBTiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNISVAgREFURTogMTBGRUIxNQogSU5WT0lDRSBUTzogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgU0hJUE1FTlQgSUQ6IDIwMDMgIFRFU1QyMDIwMDNQQUFUICAgRk9SV0FSREVSOiBYUEFOUFAKICAgIEpPSE4gREVFUkUgQlJBU0lMIExUREEKICAgIFJVQSBTRVJHSU8gRkVSTkFOREVTIEJPUkdFUyBTT0FSRVMsCiAgICBESVNUUklUTyBJTkRVU1RSSUFMICAgICAgICAgICAgICAgICAgICAgICBQQVlNRU5UIFRFUk1TOgogICAgMTMwNTQtNzA5IENBTVBJTkFTIC0gU1AgICAgICAgICAgICAgICAgICAgICAgRFVFIFRIRSBGSVJTVCBEQVkgT0YgVEhFIEZPVVJUSCBNT05USCBGT0xMT1dJTkcgVEhFCiAgICBCUkFaSUwgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBNT05USCBPRiBTSElQTUVOVCBPRiBHT09EUy4KICAgIENOUEo4OS42NzQuNzgyLzAwMTMtOTEKCiBTSElQIFRPOgogICAgSk9ITiBERUVSRSBCUkFTSUwgTFREQQogICAgUlVBIFNFUkdJTyBGRVJOQU5ERVMgQk9SR0VTIFNPQVJFUywKICAgIERJU1RSSVRPIElORFVTVFJJQUwKICAgIDEzMDU0LTcwOSBDQU1QSU5BUyAtIFNQICAgIEJSQVpJTAogICAgQ05QSjg5LjY3NC43ODIvMDAxMy05MSAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBTU9VTlQgSU4gVVNECgogU0hJUFBJTkcgTUFSS1M6ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEZPQiBGQUJSSUNBICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDExLjE0CgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIElOTEFORCBGUkVJR0hUICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA3LjU5CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQUlSRlJFSUdIVAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEZPUldBUkRFUiBGRUVTCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgSU5TVVJBTkNFCgoKCgoKCgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIERBUCBWSVJBQ09QQVMgQUlSUE9SVCAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDE4LjczCgoKCgoKCgoKCgogICAgICAgMSBQQUNLQUdFKFMpICAgICAgMTQuOTY4IEdST1NTIEtHICAgICAgICAgIC4xOTEgTkVUIEtHCiAgICAgICAxIFBBQ0tBR0UoUykgICAgICAzMy4wMDAgR1JPU1MgTEJTICAgICAgICAgLjQyMSBORVQgTEJTICAgICAgICAgICAgIENVQklDIEZFRVQKMVNPTEQgQlk6ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBDT01NRVJDSUFMIElOVk9JQ0UgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBQQUdFICAgIDIKICAgIERFRVJFICYgQ09NUEFOWQogICAgV09STERXSURFIExPR0lTVElDUyBPUEVSQVRJT05TICAgICAgICAgICAgSU5WT0lDRSBOTzogMjAwMyAzNDAxMzQgREFURTogMTBGRUIxNSBERVNUSU5BVElPTjogMjAyMDAzUEEKICAgIDM0MDAgODBUSCBTVFJFRVQgICAgICAgICAgICAgICAgICAgICAgICAgIERFRVJFIE9SREVSIE5POiAyMDAzNzQ5MDAzICAgICAgICAgREFURTogMDlGRUIxNSBUWVBFOiBTTgogICAgTU9MSU5FICAgICAgICBJTCAgNjEyNjUgICAgICAgICAgICAgICAgICAgRVhJVDogTUlMQU4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICBTSElQIERBVEU6IDEwRkVCMTUKICAgIFVOSVRFRCBTVEFURVMKCgogKDA1MDQgKSBHRU5FUkFMIExJQ0VOU0UgLSBOTFIKICAgICAgICAgVEhFU0UgQ09NTU9ESVRJRVMgV0VSRSBFWFBPUlRFRCBGUk9NIFRIRSBVTklURUQKICAgICAgICAgU1RBVEVTIElOIEFDQ09SREFOQ0UgV0lUSCBUSEUgRVhQT1JUIEFETUlOSVNUUkFUSU9OCiAgICAgICAgIFJFR1VMQVRJT05TLiAgRElWRVJTSU9OIENPTlRSQVJZIFRPIFUuUy4gTEFXIFBST0hJQklURUQuCgogKDA1OTEgKSBXRSBIRVJFQlkgQ0VSVElGWSBUSEFUIFRIRSBTVEFURU1FTlRTIEhFUkVJTiBBUkUgVFJVRSBBTkQKICAgICAgICAgQ09SUkVDVCBJTiBBTEwgUkVTUEVDVFMuCiAgICAgICAgIERFRVJFICYgQ09NUEFOWQoKICgwNjU4ICkgQUVTIFBPU1QgMzYyMzgyNTgwMDAgK0RPRQoKIFBBQ0tBR0UgTElTVDoKICAgICAgICAgICBDQVNFICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUEFDS0FHRSAgICAgR1JPU1MgICAgIE5FVCAgICAgICAgIERJTUVOU0lPTlMgICAgICAgIENVQklDCiAgICAgICAgICAgVFlQRSAgIFBBQ0tBR0UgREVTQ1JJUFRJT04gICAgICAgICAgIE5VTUJFUiAgICAgICBMQlMgICAgICBMQlMgICAgIExHVEggICBXRFRIICAgIEhHVCAgICBGRUVUCgogICAgICAgICAgIENUTiAgICBDQVJUT04gICAgICAgICAgICAgICAgICAgICAgICBDNTI2MzMgICAgIDMzLjAwMCAgICAgICAuNDIxICAgMjEwIFggICAxMCBYCjFTSElQUEVEIEJZOiAgICAgICAgICAgICAgICAgICAgICAgSU5WT0lDRSBOTzogMjAwMyAzNDAxMzQgREFURTogMTBGRUIxNSBERVNUSU5BVElPTjogMjAyMDAzUEEgICBQQUdFICAgIDMKICAgIERFRVJFICYgQ09NUEFOWSAgICAgICAgICAgICAgICAgICAgICBERUVSRSBPUkRFUiBOTzogMjAwMzc0OTAwMyAgREFURTogMDlGRUIxNSBUWVBFOiBTTgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIENVU1RPTUVSIE9SREVSIE5POgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNISVBNRU5UIElEOiAyMDAzICBURVNUMjAyMDAzUEFBVCAgIExJQ0VOU0UgTk86CgogICAgICAgICAgICAgICAgICAgICBDICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFAgICAgICAgICAgICAgICAgICAgICBVTklUICAgICBOICBFWFRFTkRFRCAgVgogICAgUEFSVCAgICAgUEFDS0FHRSBTICBQICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNISVAgIEsgICBPUkQgICBFWFRFTkRFRCAgICBQUklDRSAgICBFICAgUFJJQ0UgICAgQQogICBOVU1CRVIgICAgTlVNQkVSICBPICBUICAgICAgICAgREVTQ1JJUFRJT04gICAgICAgICAgIFFUWSAgIEcgICBRVFkgICAgTkVUIEtHICAgICAgVVNEICAgICBUICAgIFVTRCAgICAgVAoKIFJFNjk1ODEgICAgICBDNTI2MzMgVVMgICBUSEVSTU9TVEFUICAgICAgICAgICAgICAgICAgICAgMiAgICAgICAgMiAgICAgICAuMTkxICAgICAgNS41NyAgICAgICAgICAxMS4xNAoKIFRPVEFMUyBGT1IgT1JERVIgMjAwMzc0OTAwMyAgICAgICAgIDEgVE9UQUwgTElORSBJVEVNUyAgICAgICAgICAgICAgICAgIC4xOTEgICAgICAgICAgICAgICAgICAgIDExLjE0CgoxU0hJUFBFRCBCWTogICAgICAgICAgICAgICAgICAgICAgICAgIEVYUE9SVCBQQVJUUyBQQUNLSU5HIExJU1QgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUEFHRSAgICA0CiAgICAgICBERUVSRSAmIENPTVBBTlkKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIElOVk9JQ0UgTk86IDIwMDMgMzQwMTM0ICAxMEZFQjE1IERFU1RJTkFUSU9OOiAyMDIwMDNQQQogSU5WT0lDRSBUTzogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgRVhJVDogTUlMQU4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICBTSElQIERBVEU6IDEwRkVCMTUKICAgICAgIEpPSE4gREVFUkUgQlJBU0lMIExUREEgICAgICAgICAgICAgICAgIFNISVBNRU5UIElEOiAyMDAzICBURVNUMjAyMDAzUEFBVCAgIEZPUldBUkRFUjogWFBBTlBQCiAgICAgICBSVUEgU0VSR0lPIEZFUk5BTkRFUyBCT1JHRVMgU09BUkVTLAoKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIERFRVJFCiAgICBQQUNLQUdFICAgICAgUEFSVCAgICAgICAgU0hJUCAgICAgIFMgIFAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBPUkRFUiAgICAgQ1VTVE9NRVIgICAgICAgIEJJTgogICAgTlVNQkVSICAgICAgTlVNQkVSICAgICAgIFFUWSAgIEJORCBPICBEICAgICAgIERFU0NSSVBUSU9OICAgICAgICAgICAgTlVNQkVSICBPUkRFUiBOVU1CRVIgICAgTE9DQVRJT04KCiAgICBDNTI2MzMgICBSRTY5NTgxICAgICAgICAgICAgIDIgICAgIFVTICAgVEhFUk1PU1RBVCAgICAgICAgICAgICAgICAgICA0OTAwMyAgICAgICAgICAgICAgICAgRTM4N0EwMUIKCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQ0FTRSAgICAgICBESU1FTlNJT05TIChNTSkgICAgICBHUk9TUyAgIE5FVCAgIENVQklDCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgVFlQRSAgIExHVEggICBXRFRIICAgSEdUICAgICAgICAgICBMQlMgIExCUyAgIEZFRVQKCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQ1ROICAgICAyMTAgICAgIDEwICAgICAgICAgICAzMy4wMDAgICAgICAgLjQyMQoKCiBUT1RBTFMgRk9SIENPTU1FUkNJQUwgSU5WT0lDRSAyMDAzIDM0MDEzNCAgIExJTkUgSVRFTVMgICAgIDEgICAgICAgR1JPU1MgTEJTICAgICAgMzMuMDAwCgoKICAgICAgICAgICAgICAgICAgICAgICAgQ0FTRQogICAgICAgICAgICAgICAgIFFUWSAgICBUWVBFICAgIFBBQ0tBR0UgREVTQ1JJUFRJT04KCiAgICAgICAgICAgICAgICAgICAgMSAgICBDVE4gICAgIENBUlRPTgo=";

	private Map<String, String> headerinfo = new HashMap<String, String>();
	private List<ChargeLine> chargelines = new ArrayList<ChargeLine>();
	private List<String> lines = new ArrayList<String>();
	private List<Integer> pageindex = new ArrayList<Integer>();

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

	public ProcessRps() {

		Properties conf = new Properties();
		InputStream inputStream = null;
		try {

			inputStream = this.getClass().getClassLoader().getResourceAsStream("rps.properties");

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
			if (lines.get(1).contains("1SOLD BY")) {
				int beginIdx = lines.get(3).indexOf("INVOICE NO:") + 11;

				headerinfo.put("Invoice_Number", lines.get(3).substring(beginIdx, beginIdx + 12).trim());
				beginIdx = lines.get(3).indexOf("DATE:") + 5;

				headerinfo.put("Invoice_Date", lines.get(3).substring(beginIdx, beginIdx + 8).trim());
				beginIdx = lines.get(3).indexOf("DESTINATION:") + 12;
				int endIdx = lines.get(3).length();
				headerinfo.put("Destination", lines.get(3).substring(beginIdx, endIdx).trim());

				beginIdx = lines.get(4).indexOf("DEERE ORDER NO:") + 15;
				headerinfo.put("Deere_order", lines.get(4).substring(beginIdx, beginIdx + 12).trim());
				beginIdx = lines.get(4).indexOf("DATE:") + 5;
				headerinfo.put("Order_date", lines.get(4).substring(beginIdx, beginIdx + 8).trim());

				for (Map.Entry<String, String> entry : headerinfo.entrySet()) {
					System.out.println(entry.getKey() + ":" + entry.getValue());
				}

			}
		} catch (Exception ex) {
		}

	}

	public void findchargeableLines() {
		// SHIPPING MARKS:
		int lineIndex = 0, pagecount = 0, beginIndex = 0, endIndex = 0;
		boolean firstchargeDetected = false;

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
				if (firstchargeDetected && line.length() > 20 && line.length() < 80) {

					ChargeLine t = new ChargeLine(line, lineIndex);
					this.getChargelines().add(t);

				} else if (firstchargeDetected && line.length() > 20 && line.length() > 80) {
					ChargeLine t = new ChargeLine(line, lineIndex);
					this.getChargelines().add(t);
				}

			}
			lineIndex++;
			if (firstchargeDetected && beginIndex > 20)
				break;
		}

	}

	public List<ChargeLine> getChargelines() {
		return chargelines;
	}

	public void setChargelines(List<ChargeLine> chargelines) {
		this.chargelines = chargelines;
	}

	public void parseContent(String decodefile) {
		int i = 0;
		String[] splitted = decodefile.split("\\r?\\n");
		for (String element : splitted) {
			System.out.println("$$$" + element);
			lines.add(element);
			if (element.contains("PAGE"))
				pageindex.add(new Integer(i));
			i++;

		}

	}

	public static void main(String[] args) {

		ProcessRps t = new ProcessRps();
		String firstmatch = t.listFilesForFolder(new File("C:\\Users\\claude.pinel\\docs\\rps"));
		// System.out.println(firstmatch);
		// String myfile= t.readFile(decodefile);

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);

		String decodefile = t.decodeFile64(ProcessRps.byteExample.getBytes());
		t.parseContent(decodefile);

		// init line

		for (Integer line : t.getPageindex()) {

			System.out.println("page index" + line);

		}

		t.extractHeaderInfo();
		try {
			// this method extracts the charges , with line index , text and eventually amount, value will be set by 0
			t.findchargeableLines();
// once we get the charges this part is reconstructing the text file with the values from the charge lines
			for (ChargeLine chargeline : t.getChargelines()) {
				chargeline.setValue(Float.valueOf(10));
				logger.info("CHARGE LINE:" + chargeline.toString());
				chargeline.reconstructRawLine();

				int id = chargeline.getLineIndex();
				List<String> mylist = t.getLines();
				mylist.set(id, chargeline.getRawLine());

			}
			for (String line : t.getLines()) {

				logger.info(line);

			}
			// String xmlfile = readFile("c:/temp/HWAYBL_I-AHR.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
