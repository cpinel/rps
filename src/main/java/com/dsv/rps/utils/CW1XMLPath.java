package com.dsv.rps.utils;

public class CW1XMLPath {

	public static final String TXTFILE = 			"/RPS/BinaryFile";
	public static final String HAWB = 				"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/ShipmentId";
	public static final String ORDER_NUMBER = 		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderNumber";
	public static final String INVOICE_ID = 		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/References/Reference[Type='InvoiceNumber']";
	
	public static final String HAWB_GW = 			"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/GrossWeight";
	
	public static final String LINES =				"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line";
	public static final String LINE_ITEM_DESC =		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/Comments/Text/Line1";
	public static final String LINE_PART_NUMBER =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/LineNumber";
	public static final String LINE_UNIT_PRICE =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/ItemValue";
	public static final String LINE_QUANTITY =		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/OrderedQuantity";
	public static final String LINE_GROSS_WEIGHT =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/GrossWeight";
	public static final String LINE_VALUE =			"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/LineValue";
	public static final String LINE_CASE_NUMBER =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/ProductId";
	public static final String LINE_LENGTH =		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/Length";
	public static final String LINE_WIDTH =			"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/Width";
	public static final String LINE_HEIGHT =		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/Height";
	public static final String LINE_NET_WEIGHT =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/NetWeight";
	public static final String LINE_TYPE =			"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/NatureOfGoods";
	
	public static final String PARTNERS =			"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party";
	public static final String PARTNER_ROLE =		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[%%%]/@Role";
	public static final String PARTNER_NAME =		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[%%%]/Address/Name";
	public static final String PARTNER_LINE1 =		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[%%%]/Address/Line1";
	public static final String PARTNER_LINE2 =		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[%%%]/Address/Line2";
	public static final String PARTNER_POSTCODE =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[%%%]/Address/Postcode";
	public static final String PARTNER_CITY =		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[%%%]/Address/City";
	public static final String PARTNER_STATE =		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[%%%]/Address/State";		
	public static final String PARTNER_COUNTRYCODE ="/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[%%%]/Address/CountryCode";
	public static final String PARTNER_COUNTRY =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/Parties/Party[%%%]/Address/Country";
	
	
	
	public static final String EDI_CHARGE_CURR =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/References/Reference[Type='Currency Code']";
	public static final String EDI_CHARGES_SAC =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/References/Reference";
	
	public static final String PAYMENT_TERMS_REFERENCE_TYPE = 		"Payment Terms";
	
	public static final String SHIPMENT_ID = 		"REFSI";
	
	public static final String HAWB_CHARGES =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ChargeLines/ChargeLine";
	public static final String HAWB_CHARGELINE_CHARGECODE =			"ChargeCode";
	public static final String HAWB_CHARGELINE_DESCRIPTION =		"Description";
	public static final String HAWB_CHARGELINE_REVENUE_AMOUNT  =	"RevenueAmount";
	
	public static final String TRANSPORT_STAGE =			"/RPS/ShipmentInstructionMessage/ShipmentInstruction/TransportStages/Stage";
}
