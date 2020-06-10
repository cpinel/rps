
<%@page import="com.dsv.rps.action.QueueListenerUtils"%>
<%@page import="com.dsv.rps.poc.servicebus.MyServiceBusQueueListener"%>
<%@page import="org.springframework.http.converter.StringHttpMessageConverter"%>

<a href='queue.jsp'>Refresh</a>
<%
String start = request.getParameter("start");

if (start!= null && start.length()>0)
{
	QueueListenerUtils.startListening();
}

%>

 <form action = "queue.jsp" method = "GET">
 		<input type = "hidden" name = "start" value="p" />
 
QUEUE STATE : 
<% if (QueueListenerUtils.isStarted()) { %>
	Queue started
<% } else { %> Stopped
 <input type = "submit" value = "Start Listening" />
<% } %>
</form>
