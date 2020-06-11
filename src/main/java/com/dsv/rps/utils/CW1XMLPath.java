package com.dsv.rps.utils;

public class CW1XMLPath {

	public static final String TXTFILE = 			"/RPS/BinaryFile";
	public static final String HAWB = 				"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/ShipmentId";
	public static final String ORDER_NUMBER = 		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderNumber";
	public static final String INVOICE_ID = 		"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/References/Reference[Type='InvoiceNumber']";
	
	public static final String HAWB_GW = 			"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ShipmentDetails/GrossWeight";
	
	public static final String LINES =				"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line";
	public static final String LINE_GW =			"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/OrderLines/Line[%%%]/GrossWeight";
	
	public static final String EDI_CHARGE_CURR =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/References/Reference[Type='Currency Code']";
	public static final String EDI_CHARGES_SAC =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/Orders/Order/References/Reference";
	
	public static final String HAWB_CHARGES =	"/RPS/ShipmentInstructionMessage/ShipmentInstruction/ChargeLines/ChargeLine";
	public static final String HAWB_CHARGELINE_CHARGECODE =			"ChargeCode";
	public static final String HAWB_CHARGELINE_DESCRIPTION =		"Description";
	public static final String HAWB_CHARGELINE_REVENUE_AMOUNT  =	"RevenueAmount";
	
	
}
