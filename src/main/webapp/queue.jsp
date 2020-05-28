
<%@page import="com.dsv.rps.action.QueueListenerUtils"%>
<%@page import="com.dsv.rps.poc.servicebus.MyServiceBusQueueListener"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="org.springframework.http.converter.StringHttpMessageConverter"%>

<% if (QueueListenerUtils.startListening()) { %>
Start listening queue
<% } else %>
Listener already started

<%QueueListenerUtils.startListening();%>

LAST MESSAGE READ FROM QUEUE : <%=StringEscapeUtils.escapeHtml4(QueueListenerUtils.LAST_MESSAGE)%>


 <form action = "queue.jsp" method = "GET">
         <input type = "submit" value = "refresh" />
      </form>
      
