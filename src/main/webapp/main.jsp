
<%@page import="com.dsv.rps.action.*"%>
<%
String fn = request.getParameter("first_name");
String ln = request.getParameter("last_name");


String res = QueueSenderUtils.sendMessagetoQueue(fn,ln );


%>

Message pushed in XML to queue, please check queue


 <form action = "queue.jsp" method = "GET">
         <input type = "submit" value = "Go to Queue monitor" />
      </form>