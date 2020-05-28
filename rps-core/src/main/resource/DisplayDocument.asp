<%

'If(Session("Remote_User") = "") Then 	Response.Redirect("no_session.asp")
Set Conn = Server.CreateObject("ADODB.Connection")     'Creating connection thru ADO

 Session("Message") = ""
 %>

<HTML>
<HEAD>
	<TITLE>John Deere Invoice Printing System, Americas</TITLE>   
<script>
	this.focus(); 
</script>
</HEAD>
<body bgcolor="White" topmargin="0" bottommargin="0" leftmargin="1" rightmargin="1">

<% 	if Request("Invoice_Id") > 0 then
	 Conn.Open Session("DSN") 
		  sql = "select TotalPages = max(Page_Number)  from [jd_inv_line] (nolock)" &_
		" where invoice_id = " & Request("Invoice_Id")
		 ' Response.Write(sql)
	  Set RS = Conn.Execute(sql)
	  TotalPages = RS("TotalPages")
	  
	  For PageNumber = 1 to TotalPages
	    call DisplayDocument (Request("Invoice_Id"), PageNumber)
  	  next
	  
	  'call DisplayDocument (1)
	  'LogString = "Preview Invoice N# " & Request("InvNumberSearch")
	  sql = "insert SessionLog " &_
	  "select '" & Request.ServerVariables("REMOTE_HOST") &_
	  "', '" & Session("Remote_User") & "', " & Request("Invoice_Id") & "," &	Request("Type") & ", getdate()"  
	  'Response.Write(sql)
'	  Set LogRS =Conn.Execute(sql)
	  if request("action") = "merge" then 
	    Response.Write("<script> window.print(); window.close(); </script>")
	  end if

	  end if
 	   %>
<%= Session("Message") %>

</BODY>
</HTML>



<% sub DisplayDocument (Id, Page)
	sql = "select page_Line_Number,  txt_line" &_
		  " from jd_inv_line (nolock)" &_
		" where invoice_id = " & Id & " and Page_Number = " & Page &" order by 1"
		' Response.Write(sql)
	Set RS = Conn.Execute(sql)
	dim Lines(190), LastLine
		if  RS.Eof then
			Session("Message") = "Document N# " & Id & " not found <FORM><INPUT type='button' value='Close Window' onClick='window.close()'></FORM>"
			'Response.Redirect("default.asp")
		else
			Session("Message") = ""
		end if
		Do While Not RS.EOF 
		  	LineNumber = cint(RS("page_Line_Number"))
			Lines(LineNumber)=  RS("txt_line")
			if LineNumber > LastLine then   LastLine = LineNumber
			RS.MoveNext
		loop 
		RS.Close 
		
     if request("action") = "merge" and Page = 1 then
			' get charges and replace Invoice image numbers with COM
			sql = "select page_line_number,	Charge_Amount" &_
					  " from [jd_inv_line_text_with_com_charges] (nolock)" &_
					  " where invoice_id = " & Id 
			Set RS = Conn.Execute(sql)
			Do While Not RS.EOF 
				LineNumber = cint(RS("page_Line_Number"))
				Lines(LineNumber)=  left( Lines(LineNumber) & space(95), 95)  & right(space(10) & formatnumber(RS("Charge_Amount"), 2),10)  
				RS.MoveNext
			loop 		
			' get HAWB and display after inv total
			sql = "select [COM_HAWB] = isnull([COM_HAWB],'') " &_
					  " from [jd_inv_header] (nolock)" &_
					  " where invoice_id = " & Id 
			Set RS = Conn.Execute(sql)
			Lines(37)=   space(46) & "HAWB " & trim(RS("COM_HAWB"))  
		
	 end if

		if Page > 1 then Response.Write("<PRE  STYLE='page-break-after:always'> </PRE>") 
		
		%><pre  STYLE=font-size:11><b><% 
		For LineNumber = 1 to LastLine
			if isempty(Lines(LineNumber)) then
				Response.Write (" " & chr(13))
			else
			    Response.Write ( Lines(LineNumber) & chr(13) )
			end if
		next
Response.Write ( "</pre>")

		
end sub %>