package com.dsv.rps.parser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.log4j.Logger;

import com.dsv.rps.bean.ChargeLine;
import com.dsv.rps.bean.Invoice;
import com.dsv.rps.beans.EDICharge;
import com.dsv.rps.beans.OutputBean;
import com.dsv.rps.logging.Error;
import com.dsv.rps.logging.Warning;

public class TextBuilder {
	private static Logger logger = Logger.getLogger(TextBuilder.class.getName());
	public static final String TEXT_EXAMPLE = "IElEICAgICAgREVFUkUgIDIwMTUwMjEyICAxMzI1IFBSTlQgIFhQQU5QUAoxU09MRCBCWTogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIENPTU1FUkNJQUwgSU5WT0lDRSAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFBBR0UgICAgMQogICAgREVFUkUgJiBDT01QQU5ZCiAgICBXT1JMRFdJREUgTE9HSVNUSUNTIE9QRVJBVElPTlMgICAgICAgICAgICBJTlZPSUNFIE5POiAyMDAzIDM0MDEzNCBEQVRFOiAxMEZFQjE1IERFU1RJTkFUSU9OOiAyMDIwMDNQQQogICAgMzQwMCA4MFRIIFNUUkVFVCAgICAgICAgICAgICAgICAgICAgICAgICAgREVFUkUgT1JERVIgTk86IDIwMDM3NDkwMDMgICAgICAgICBEQVRFOiAwOUZFQjE1IFRZUEU6IFNOCiAgICBNT0xJTkUgICAgICAgIElMICA2MTI2NQogICAgVU5JVEVEIFNUQVRFUyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQ1VTVE9NRVIgT1JERVIgTk86CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBFWElUOiBNSUxBTiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNISVAgREFURTogMTBGRUIxNQogSU5WT0lDRSBUTzogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgU0hJUE1FTlQgSUQ6IDIwMDMgIFRFU1QyMDIwMDNQQUFUICAgRk9SV0FSREVSOiBYUEFOUFAKICAgIEpPSE4gREVFUkUgQlJBU0lMIExUREEKICAgIFJVQSBTRVJHSU8gRkVSTkFOREVTIEJPUkdFUyBTT0FSRVMsCiAgICBESVNUUklUTyBJTkRVU1RSSUFMICAgICAgICAgICAgICAgICAgICAgICBQQVlNRU5UIFRFUk1TOgogICAgMTMwNTQtNzA5IENBTVBJTkFTIC0gU1AgICAgICAgICAgICAgICAgICAgICAgRFVFIFRIRSBGSVJTVCBEQVkgT0YgVEhFIEZPVVJUSCBNT05USCBGT0xMT1dJTkcgVEhFCiAgICBCUkFaSUwgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBNT05USCBPRiBTSElQTUVOVCBPRiBHT09EUy4KICAgIENOUEo4OS42NzQuNzgyLzAwMTMtOTEKCiBTSElQIFRPOgogICAgSk9ITiBERUVSRSBCUkFTSUwgTFREQQogICAgUlVBIFNFUkdJTyBGRVJOQU5ERVMgQk9SR0VTIFNPQVJFUywKICAgIERJU1RSSVRPIElORFVTVFJJQUwKICAgIDEzMDU0LTcwOSBDQU1QSU5BUyAtIFNQICAgIEJSQVpJTAogICAgQ05QSjg5LjY3NC43ODIvMDAxMy05MSAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBTU9VTlQgSU4gVVNECgogU0hJUFBJTkcgTUFSS1M6ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEZPQiBGQUJSSUNBICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDExLjE0CgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIElOTEFORCBGUkVJR0hUICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA3LjU5CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQUlSRlJFSUdIVAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEZPUldBUkRFUiBGRUVTCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgSU5TVVJBTkNFCgoKCgoKCgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIERBUCBWSVJBQ09QQVMgQUlSUE9SVCAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDE4LjczCgoKCgoKCgoKCgogICAgICAgMSBQQUNLQUdFKFMpICAgICAgMTQuOTY4IEdST1NTIEtHICAgICAgICAgIC4xOTEgTkVUIEtHCiAgICAgICAxIFBBQ0tBR0UoUykgICAgICAzMy4wMDAgR1JPU1MgTEJTICAgICAgICAgLjQyMSBORVQgTEJTICAgICAgICAgICAgIENVQklDIEZFRVQKMVNPTEQgQlk6ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBDT01NRVJDSUFMIElOVk9JQ0UgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBQQUdFICAgIDIKICAgIERFRVJFICYgQ09NUEFOWQogICAgV09STERXSURFIExPR0lTVElDUyBPUEVSQVRJT05TICAgICAgICAgICAgSU5WT0lDRSBOTzogMjAwMyAzNDAxMzQgREFURTogMTBGRUIxNSBERVNUSU5BVElPTjogMjAyMDAzUEEKICAgIDM0MDAgODBUSCBTVFJFRVQgICAgICAgICAgICAgICAgICAgICAgICAgIERFRVJFIE9SREVSIE5POiAyMDAzNzQ5MDAzICAgICAgICAgREFURTogMDlGRUIxNSBUWVBFOiBTTgogICAgTU9MSU5FICAgICAgICBJTCAgNjEyNjUgICAgICAgICAgICAgICAgICAgRVhJVDogTUlMQU4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICBTSElQIERBVEU6IDEwRkVCMTUKICAgIFVOSVRFRCBTVEFURVMKCgogKDA1MDQgKSBHRU5FUkFMIExJQ0VOU0UgLSBOTFIKICAgICAgICAgVEhFU0UgQ09NTU9ESVRJRVMgV0VSRSBFWFBPUlRFRCBGUk9NIFRIRSBVTklURUQKICAgICAgICAgU1RBVEVTIElOIEFDQ09SREFOQ0UgV0lUSCBUSEUgRVhQT1JUIEFETUlOSVNUUkFUSU9OCiAgICAgICAgIFJFR1VMQVRJT05TLiAgRElWRVJTSU9OIENPTlRSQVJZIFRPIFUuUy4gTEFXIFBST0hJQklURUQuCgogKDA1OTEgKSBXRSBIRVJFQlkgQ0VSVElGWSBUSEFUIFRIRSBTVEFURU1FTlRTIEhFUkVJTiBBUkUgVFJVRSBBTkQKICAgICAgICAgQ09SUkVDVCBJTiBBTEwgUkVTUEVDVFMuCiAgICAgICAgIERFRVJFICYgQ09NUEFOWQoKICgwNjU4ICkgQUVTIFBPU1QgMzYyMzgyNTgwMDAgK0RPRQoKIFBBQ0tBR0UgTElTVDoKICAgICAgICAgICBDQVNFICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUEFDS0FHRSAgICAgR1JPU1MgICAgIE5FVCAgICAgICAgIERJTUVOU0lPTlMgICAgICAgIENVQklDCiAgICAgICAgICAgVFlQRSAgIFBBQ0tBR0UgREVTQ1JJUFRJT04gICAgICAgICAgIE5VTUJFUiAgICAgICBMQlMgICAgICBMQlMgICAgIExHVEggICBXRFRIICAgIEhHVCAgICBGRUVUCgogICAgICAgICAgIENUTiAgICBDQVJUT04gICAgICAgICAgICAgICAgICAgICAgICBDNTI2MzMgICAgIDMzLjAwMCAgICAgICAuNDIxICAgMjEwIFggICAxMCBYCjFTSElQUEVEIEJZOiAgICAgICAgICAgICAgICAgICAgICAgSU5WT0lDRSBOTzogMjAwMyAzNDAxMzQgREFURTogMTBGRUIxNSBERVNUSU5BVElPTjogMjAyMDAzUEEgICBQQUdFICAgIDMKICAgIERFRVJFICYgQ09NUEFOWSAgICAgICAgICAgICAgICAgICAgICBERUVSRSBPUkRFUiBOTzogMjAwMzc0OTAwMyAgREFURTogMDlGRUIxNSBUWVBFOiBTTgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIENVU1RPTUVSIE9SREVSIE5POgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNISVBNRU5UIElEOiAyMDAzICBURVNUMjAyMDAzUEFBVCAgIExJQ0VOU0UgTk86CgogICAgICAgICAgICAgICAgICAgICBDICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFAgICAgICAgICAgICAgICAgICAgICBVTklUICAgICBOICBFWFRFTkRFRCAgVgogICAgUEFSVCAgICAgUEFDS0FHRSBTICBQICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNISVAgIEsgICBPUkQgICBFWFRFTkRFRCAgICBQUklDRSAgICBFICAgUFJJQ0UgICAgQQogICBOVU1CRVIgICAgTlVNQkVSICBPICBUICAgICAgICAgREVTQ1JJUFRJT04gICAgICAgICAgIFFUWSAgIEcgICBRVFkgICAgTkVUIEtHICAgICAgVVNEICAgICBUICAgIFVTRCAgICAgVAoKIFJFNjk1ODEgICAgICBDNTI2MzMgVVMgICBUSEVSTU9TVEFUICAgICAgICAgICAgICAgICAgICAgMiAgICAgICAgMiAgICAgICAuMTkxICAgICAgNS41NyAgICAgICAgICAxMS4xNAoKIFRPVEFMUyBGT1IgT1JERVIgMjAwMzc0OTAwMyAgICAgICAgIDEgVE9UQUwgTElORSBJVEVNUyAgICAgICAgICAgICAgICAgIC4xOTEgICAgICAgICAgICAgICAgICAgIDExLjE0CgoxU0hJUFBFRCBCWTogICAgICAgICAgICAgICAgICAgICAgICAgIEVYUE9SVCBQQVJUUyBQQUNLSU5HIExJU1QgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUEFHRSAgICA0CiAgICAgICBERUVSRSAmIENPTVBBTlkKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIElOVk9JQ0UgTk86IDIwMDMgMzQwMTM0ICAxMEZFQjE1IERFU1RJTkFUSU9OOiAyMDIwMDNQQQogSU5WT0lDRSBUTzogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgRVhJVDogTUlMQU4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICBTSElQIERBVEU6IDEwRkVCMTUKICAgICAgIEpPSE4gREVFUkUgQlJBU0lMIExUREEgICAgICAgICAgICAgICAgIFNISVBNRU5UIElEOiAyMDAzICBURVNUMjAyMDAzUEFBVCAgIEZPUldBUkRFUjogWFBBTlBQCiAgICAgICBSVUEgU0VSR0lPIEZFUk5BTkRFUyBCT1JHRVMgU09BUkVTLAoKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIERFRVJFCiAgICBQQUNLQUdFICAgICAgUEFSVCAgICAgICAgU0hJUCAgICAgIFMgIFAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBPUkRFUiAgICAgQ1VTVE9NRVIgICAgICAgIEJJTgogICAgTlVNQkVSICAgICAgTlVNQkVSICAgICAgIFFUWSAgIEJORCBPICBEICAgICAgIERFU0NSSVBUSU9OICAgICAgICAgICAgTlVNQkVSICBPUkRFUiBOVU1CRVIgICAgTE9DQVRJT04KCiAgICBDNTI2MzMgICBSRTY5NTgxICAgICAgICAgICAgIDIgICAgIFVTICAgVEhFUk1PU1RBVCAgICAgICAgICAgICAgICAgICA0OTAwMyAgICAgICAgICAgICAgICAgRTM4N0EwMUIKCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQ0FTRSAgICAgICBESU1FTlNJT05TIChNTSkgICAgICBHUk9TUyAgIE5FVCAgIENVQklDCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgVFlQRSAgIExHVEggICBXRFRIICAgSEdUICAgICAgICAgICBMQlMgIExCUyAgIEZFRVQKCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQ1ROICAgICAyMTAgICAgIDEwICAgICAgICAgICAzMy4wMDAgICAgICAgLjQyMQoKCiBUT1RBTFMgRk9SIENPTU1FUkNJQUwgSU5WT0lDRSAyMDAzIDM0MDEzNCAgIExJTkUgSVRFTVMgICAgIDEgICAgICAgR1JPU1MgTEJTICAgICAgMzMuMDAwCgoKICAgICAgICAgICAgICAgICAgICAgICAgQ0FTRQogICAgICAgICAgICAgICAgIFFUWSAgICBUWVBFICAgIFBBQ0tBR0UgREVTQ1JJUFRJT04KCiAgICAgICAgICAgICAgICAgICAgMSAgICBDVE4gICAgIENBUlRPTgo=";
	public static final String TEXT_EXAMPLE2 = "UGFuYWxwaW5hIEVjdWFkb3IgUy5BLjtQYW5hbHBpbmEgRWN1YWRvciBTLkEuOzE3OTA3MzAxNjYwMDE7MDE7MDAxOzAwMTswMDAwMDAxNTc7QXYuIDYgZGUgRGljaWVtYnJlIE4zMi0zMTIgeSBKZWFuIEIuIEJvdXNzaW5nYXVsdCBFZGlmaWNpbyBUNiwgUGlzbyA1LCBPZmljaW5hIDUwMzswOC8wNS8yMDIwO0F2ZW5pZGEgRWwgSW5jYSwgeSBBdmVuaWRhIEFtYXpvbmFzIDQwNiwgRTQtMTgxOzA1OTA7U0k7MDQ7O0hBTExJQlVSVE9OIExBVElOIEFNRVJJQ0EgU1JMOzE3OTE4NTE2MzYwMzI7MzEyLjAwOzAuMDA7W0lUMzswMDAwOzAuMDA7MDAwMDswLjAwXVtJVDI7MjszMTIuMDA7MTI7MjQuMDBdMC4wMDszMzYuMDA7RE9MQVI7W1BBRzIwOzMzNi4wMDswO0RpYXNdO1tERVRDQ0w7MDAwO0lNUE9SVCBDVVNUT01TIENMRUFSQU5DRSBDSEFSR0VTOzEuMDA7MjAwLjAwOzAuMDA7MjAwLjAwOzs7REVUXVtJRDM7MC4wMDswLjAwOzAuMDA7MC4wMF1bSUQyOzI7MTI7MjAwLjAwOzI0LjAwMDBdO1tERVRMUkZMOzAwMDtSRUlNQlVSU0VNRU5UIFdBUkVIT1VTRSBGT1JLTElGVCBBTkQgT1RIRVIgRVFVSVBNRU5UOzEuMDA7MTEyLjAwOzAuMDA7MTEyLjAwOzs7REVUXVtJRDM7MC4wMDswLjAwOzAuMDA7MC4wMF1bSUQyOzAuMDA7MDA7MC4wMDswLjAwXWVtYWlsQ2xpZW50ZT1OZWxzb24uTWVuZGV6QHBhbmFscGluYS5jb207Q09ESUdPSU5URVJOT1NBUD0wMDAwMDAxNTc7Q09ESUdPSU5URVJOT1NBUENMSUVOVEU9Njc5MDY4OTI7UkVGRVJFTkNFPTtIQVdCL0JMPVNBTzc1NTg4NDE=";

	// rawlines
	private List<String> lines = new ArrayList<String>();

	private Invoice invoice = new Invoice();

	public TextBuilder() {

	}

	public TextBuilder(byte[] inputdata) {

		// constructor, decode and parse lines
		boolean success = this.parseContent(decodeFile64(inputdata));
		// header info
		this.extractHeaderInfo();
		// detect chargelines
		this.extractChargeLines();
	}

	public TextBuilder(String inputdata) {

		// this(inputdata.getBytes());

	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	// encode textfile base64
	public static byte[] encodeFile64(byte[] bytes) {

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

//decode textfile base 64
	public static String decodeFile64(byte[] bytes) {

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

	public boolean analyze(String decodefile, OutputBean ob) {

		// constructor, decode and parse lines
		boolean success = this.parseContent(decodeFile64(decodefile.getBytes()));
		if (!success) {
			ob.addError(Error.EDI_CHARGE);
			return false;
		}
		// header info
		this.extractHeaderInfo();

		// detect chargelines in text
		this.extractChargeLines();
		if (this.getInvoice().getChargeLines().size() == 0) {
			// just wwarning even if no match
			ob.addWarning(Warning.NO_CHARGES);

		}
		return true;
	}

	public String completeTxtFileWithCharges(OutputBean ob) {
		boolean match = false;
		List<EDICharge> charges = ob.getChargesToComplete();
		for (EDICharge edicharge : charges)

		{// get id from text of edicharge
			boolean success = this.updateChargeLine(edicharge.getText(), edicharge.getUpdatedValue().floatValue());
			if (success)
				match = true;

		}

		if (match == false)
			ob.addWarning(Warning.NO_CHARGES);
		// rebuild anyway
		try {
			return new String(this.rebuild());
		} catch (Exception ex) {
			ob.addError(Error.EDI_CHARGE_CURR);
		}

		return "";

	}

	// split lines
	private boolean parseContent(String decodefile) {

		try {

			String[] splitted = decodefile.split("\\r?\\n");
			for (String element : splitted) {

				lines.add(element);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	// main methods to manipulate text files

	private void extractHeaderInfo()

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

	private void extractChargeLines() {
		// SHIPPING MARKS:
		int lineIndex = 0, beginIndex = 0;
		boolean firstchargeDetected = false;
		int idx = 0;
		for (String line : lines) {

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

	public boolean updateChargeLine(String textIdentifier, float value) {

		Integer lineIndex = invoice.getChargeLinesIdx().get(textIdentifier);
		if (lineIndex == null)
			return false;
		ChargeLine airFreightCharge = invoice.getChargeLines().get(lineIndex.intValue());
		airFreightCharge.setValue(value);
		return true;

	}

	protected void reCalculateChargeLines() {

		// rebuild all chargelines, total is calculated on last charge line

		float total = 0;

		// read all except last
		for (int i = 0; i < this.getInvoice().getChargeLines().size() - 1; i++) {

			ChargeLine chargeline = this.getInvoice().getChargeLines().get(i);

			// this will reconstruct the rawline
			chargeline.reconstructRawLine();
			// sum for total on last charge line
			total += chargeline.getValue();
			int id = chargeline.getLineIndex();

			lines.set(id, chargeline.getRawLine());

		}

		// total calculated here

		ChargeLine lastchargeline = this.getInvoice().getChargeLines()
				.get(this.getInvoice().getChargeLines().size() - 1);
		lastchargeline.setValue(total);
		lastchargeline.reconstructRawLine();
		int id = lastchargeline.getLineIndex();
		// replace reconstructed line in table
		lines.set(id, lastchargeline.getRawLine());
	}

	// recalculate and reconstruct charge lines,with new amounts
	protected byte[] rebuild() {

		reCalculateChargeLines();
		StringBuilder sb = new StringBuilder();
		for (String line : this.lines) {

			sb.append(line+"\n");

		}
		byte[] res = sb.toString().getBytes();
		logger.info(sb.toString());
		return  encodeFile64(res);
	}
public static void display(String encoded)
{
	try {
		String decoded=decodeFile64(encoded.getBytes());
		String[] splitted = decoded.split("\\r?\\n");
		for (String element : splitted) {

			logger.info(element);

		}}
catch (Exception e) {
	// TODO: handle exception
} 
 

}
	public static void main(String[] args) {

		// construct from a byte[] ,extract is launched from constructor

		OutputBean ob = new OutputBean();
		ob.setOrderNumber("TESTE2EJL007C");
		ob.setBuyerId("67906544");
		ob.setInvoiceNumber("123456");
		ob.setPdf(false);
		ob.addChargeToComplete(new EDICharge("I132", "AIRFREIGHT", 100.00));
		ob.addChargeToComplete(new EDICharge("I400", "FORWARDER FEES", 500.00));
		// update one charge line

		TextBuilder txb = new TextBuilder();

		boolean parseOk = txb.analyze(TEXT_EXAMPLE, ob); // use ob.addError() if any critical error during process, and
															// return false

		if (parseOk) {
			String encoded64Result = txb.completeTxtFileWithCharges(ob);

		 display(encoded64Result);
			
			
			ob.setBinaryFile(encoded64Result);
		}
		
		
	    
		

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);

	}
}