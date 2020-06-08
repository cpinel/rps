<%@page import="com.dsv.rps.resources.Config"%>
<%@page import="com.dsv.rps.resources.Constants"%>
<%@page import="com.dsv.rps.action.SendAction"%>

Welcome to RPS v2.
<br/>
This is environment <b><h3><%=new Config().getConfigValue("ENVIRONMENT")%></h3></b>
<br/>
<table style="width: 100%;height: 100%">
	<tr>
		<td style="width: 50%;height: 100%" rowspan="2"><iframe id="logid" title="Logs page" height="100%" width="100%" src="logs.jsp"> </iframe></td>
		<td style="width: 50%"><iframe id="logid" title="Logs page" height="100%" width="100%" src="main.jsp"> </iframe></td>
    </tr>
    <tr>
		<td style="width: 50%"><iframe id="logid" title="Queue page" height="100%" width="100%" src="queue.jsp"> </iframe></td>
    </tr>
</table>