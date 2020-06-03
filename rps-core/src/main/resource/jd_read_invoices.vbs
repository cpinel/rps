
  'On Error Resume next
  
  Set WshNetwork = WScript.CreateObject("WScript.Network")
  ProjectFolder = "\\sqlus0comm\comm\jd_inv"
  strconn="PROVIDER=MSDASQL;DRIVER={SQL Server};SERVER=sqlus0jd-rps;DATABASE=jd-rps_2.0;IntegratedSecurity=SSPI;"


  Dim fso, tf
  Set Shell = CreateObject("WScript.Shell")
  Set fso = CreateObject("Scripting.FileSystemObject")
  Const ForReading = 1

  Set Conn = CreateObject("ADODB.Connection")
  Conn.Open strconn 
  Conn.CommandTimeout= 600
  
'	sql = 	" truncate table JD_invoice_print..jd_inv_line "
'	rs1 = Conn.Execute (sql) 
'	sql = 	" truncate table JD_invoice_print..jd_inv_header "
'	rs1 = Conn.Execute (sql) 
		
   set InFolder = fso.GetFolder("\\sqlus0comm\comm\ftproot\jd\in\")
   ResultFile	= ProjectFolder & "\failed\tmp_compress.txt"   

   For Each File In InFolder.Files
   if right(file.name,3) = "txt" then
      WScript.Echo 
      WScript.Echo "Found '" & file.path & "' " & now
      call Read_Invoices  (File)
	  
	  fso.CopyFile file.Path, ProjectFolder & "\done\", True
	  fso.DeleteFile (file.Path)
	   
	  ' File.Delete
	end if 
  next
 

function Read_Invoices (FileIn)
  Set ts = fso.OpenTextFile(FileIn, ForReading)
  dim line_count
  dim Inv_Lines(50000)
  line_count = 0

  Do While Not ts.AtEndOfStream  
	line_count = line_count + 1
	Inv_Lines(line_count) = ts.ReadLine
    'WScript.Echo line_count &  " - " & Inv_Lines (line_count)
    'WScript.Echo " right( Inv_Lines(line_count),9) " & right( Inv_Lines(line_count),9)
 
	 '   if left( Inv_Lines(line_count),6)  =" TOP**" then
    if right( Inv_Lines(line_count),9)  = "PAGE    1" and line_count > 1 then

	 WScript.Echo ("before inv save, line_count = " & line_count )  
	 
     call Save_Invoice (Inv_Lines, line_count  ) 
	  ' for the next invoice
	  Inv_Lines(1) = Inv_Lines(line_count)
      line_count = 1
	 WScript.Echo ("after inv save, reseting line_count = 1"  )  
    end if
  Loop
	ts.Close
	
    WScript.Echo ("last inv save, line_count = " & line_count )  
    call Save_Invoice (Inv_Lines, line_count  ) 
	
end function

function Save_Invoice (Inv_Lines, last_line_number)

' first element is HEADER for this invoice
' last element is HEADER for next invoice, we ignore it

	if left(Inv_Lines (1),8) = "1SOLD BY" then

		Invoice_Number  = mid(Inv_Lines (3),59,12)
		Invoice_Date    = mid(Inv_Lines (3),77,7)
		Invoice_Date    = TranslateMonth ( Invoice_Date )	
		
		Destination 	= mid(Inv_Lines (3),98,12)
		DEERE_ORDER 	= mid(Inv_Lines (4),63,12)
		ORDER_DATE  	= mid(Inv_Lines (4),88,7)
		ORDER_DATE  	= TranslateMonth ( ORDER_DATE )
		
		ORDER_TYPE  	= mid(Inv_Lines (4),102,5)

		
		Invoice_Number = replace (Invoice_Number, " ", "")
		
		WScript.Echo "2 " & Inv_Lines (2)
		WScript.Echo "3 " & Inv_Lines (3)
		WScript.Echo "4 " & Inv_Lines (4)
		WScript.Echo "----------"
		WScript.Echo "Invoice #   = " & Invoice_Number
		WScript.Echo "Inv Date    = " & Invoice_Date
		WScript.Echo "Destination = " & Destination
		WScript.Echo "DEERE ORDER = " & DEERE_ORDER
		WScript.Echo "ORDER DATE  = " & ORDER_DATE
		WScript.Echo "ORDER TYPE  = " & ORDER_TYPE


		sql = 	" delete jd_inv_header where Invoice_Number  =  '" & Invoice_Number & "'"
		rs1 = Conn.Execute (sql) 

    	sql = 	" insert jd_inv_header (  Invoice_Number, Invoice_Date_txt, Order_Number, Order_Date_txt, Order_Type, Destination ) " &_
				"select Invoice_Number  ='" & Invoice_Number & "', " &_
					"Invoice_Date  ='" & Invoice_Date & "', " &_
					"Order_Number  ='" & DEERE_ORDER & "', " &_
					"Order_Date  ='" & ORDER_DATE & "', " &_
					"Order_Type  ='" & ORDER_TYPE & "', " &_
					"Destination   ='" & Destination & "'" 

		   WScript.Echo sql

		   rs1 = Conn.Execute (sql) 
		

		sql = 	" select [Invoice_id] = max([Invoice_id]) from jd_inv_header "
		  rs1 = Conn.Execute (sql) 
		Invoice_id = rs1("Invoice_id")



		Page_Number = 1 
		Page_Line_Number = 1
		for cnt = 1 to last_line_number - 1
			' WScript.Echo cnt
			if len(Inv_Lines(cnt)) > 1 then 
			  if InStrRev(right(trim(Inv_Lines(cnt)),20), "PAGE" ) > 0 then
			    ' WScript.Echo right(trim(Inv_Lines(cnt) ),4)
			    Page_Number 		=  right(trim(Inv_Lines(cnt) ),3)
				Page_Line_Number 	= 1
			  end if
			 
			 Inv_Lines(cnt) = mid(Inv_Lines(cnt),2)
			 ' WScript.Echo  cnt &  " " & Inv_Lines(cnt) 
				sql = 	" insert jd_inv_line ( Invoice_id, page_number, page_line_number, line_number, txt_line) " &_
					"select  Invoice_id = " & Invoice_id &_
					", page_number = " & Page_Number  &_
					", page_line_number = " & Page_Line_Number  &_
					", line_number = " & cnt  &_
					", txt_line = '" & replace(Inv_Lines(cnt),"'","''") & "'"
			 '  WScript.Echo sql
			   rs1 = Conn.Execute (sql) 
			end if
			Page_Line_Number = Page_Line_Number + 1
		next
		
		sql = 	"exec enrich_invoice_after_loading " & Invoice_id 
		rs1 = Conn.Execute (sql) 

		
    end if

	end function
	
  function TranslateMonth ( dateStr )	
  
    monthString = mid(dateStr,3,3)
	If monthString = "ENE" Then 
	   monthString = "JAN"
	ElseIf monthString = "FEV"  Then
	   monthString = "FEB"
	ElseIf monthString = "ABR"  Then
	   monthString = "APR"
	ElseIf monthString = "MAI"  Then
	   monthString = "MAY"
	ElseIf monthString = "AGO"  Then
	   monthString = "AUG"
	ElseIf monthString = "SET"  Then
	   monthString = "SEP"
	ElseIf monthString = "OUT"  Then
	   monthString = "OCT"
	ElseIf monthString = "DEZ"  Then
	   monthString = "DEC"
	ElseIf monthString = "DIC"  Then
	   monthString = "DEC"
	End If

  TranslateMonth = left(dateStr,2) & monthString & right(dateStr,2)
  
  end function
  		



