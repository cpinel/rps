
<%@page import="com.dsv.rps.action.*"%>

<a href='main.jsp'>Refresh</a>
<%
String fn = request.getParameter("first_name");
String ln = request.getParameter("last_name");
String push = request.getParameter("push");

if (fn!= null && fn.length()>0)
{
	String res = QueueSenderUtils.sendMessagetoQueue(fn,ln );
	%>Message sent<%
}
else if (push != null && "p".equals(push))
{
	 QueueSenderUtils.sendStaticFileToQueue();
	 %>Message sent<%
}
else if (push != null && "w".equals(push))
{
	 QueueSenderUtils.sendStaticFileToQueueWrong();
	 %>Message sent<%
}

%>

<form action = "main.jsp" method = "GET">
         First Name: <input type = "text" name = "first_name">
         Last Name: <input type = "text" name = "last_name" />
         <input type = "submit" value = "Submit" />
      </form>
      
      
 OR 
 
<form action = "main.jsp" method = "GET">
 		<input type = "hidden" name = "push" value="p" />
         <input type = "submit" value = "Push XML file provided" />
</form>
      
OR

<form action = "main.jsp" method = "GET">
 		<input type = "hidden" name = "push" value="w" />
         <input type = "submit" value = "Push Wrong file" />
</form>
