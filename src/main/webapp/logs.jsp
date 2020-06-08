
RPS recent log <%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<a href='logs.jsp'>Refresh</a>


<%@page import="com.dsv.rps.logging.RollingLogs"%>
<%@page import="com.dsv.rps.logging.LogItem"%>
<table border="1" style="padding: 3px;margin: 3px; height: 100%;width: 100%;overflow: scroll;" >
<tr>
	<th>#</th>
	<th>Timestamp</th>
	<th>Text</th>
</tr>

	<% for (int n=0;n<RollingLogs.getLogs().size();n++)
	{
		LogItem item = RollingLogs.getLogs().get(n);
		%>
		<tr>
			<td style="padding: 5px" width="10px"><%=n+1 %></td>
			<td style="padding: 5px" width="250px"><%=item.getTimestamp()%></td>
			<td style="padding: 5px;"><font color="<%=item.getGroup().getColor()%>"><%=StringEscapeUtils.escapeHtml4(item.getText())%></td>
		</tr>
	<% } %>
</table>