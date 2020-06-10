
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
else if (push != null && "r".equals(push))
{
	 QueueResponseUtils.sendMessagetoQueue("<RPSReply><ShipmentId>SHSV0000050</ShipmentId><code>OK</code><format>TXT</format><errors/><warnings><warning>Sum of line items weight is greater than shipment gros weight</warning></warnings><binaryFile>UGFuYWxwaW5hIEVjdWFkb3IgUy5BLjtQYW5hbHBpbmEgRWN1YWRvciBTLkEuOzE3OTA3MzAxNjYwMDE7MDE7MDAxOzAwMTswMDAwMDAxNTc7QXYuIDYgZGUgRGljaWVtYnJlIE4zMi0zMTIgeSBKZWFuIEIuIEJvdXNzaW5nYXVsdCBFZGlmaWNpbyBUNiwgUGlzbyA1LCBPZmljaW5hIDUwMzswOC8wNS8yMDIwO0F2ZW5pZGEgRWwgSW5jYSwgeSBBdmVuaWRhIEFtYXpvbmFzIDQwNiwgRTQtMTgxOzA1OTA7U0k7MDQ7O0hBTExJQlVSVE9OIExBVElOIEFNRVJJQ0EgU1JMOzE3OTE4NTE2MzYwMzI7MzEyLjAwOzAuMDA7W0lUMzswMDAwOzAuMDA7MDAwMDswLjAwXVtJVDI7MjszMTIuMDA7MTI7MjQuMDBdMC4wMDszMzYuMDA7RE9MQVI7W1BBRzIwOzMzNi4wMDswO0RpYXNdO1tERVRDQ0w7MDAwO0lNUE9SVCBDVVNUT01TIENMRUFSQU5DRSBDSEFSR0VTOzEuMDA7MjAwLjAwOzAuMDA7MjAwLjAwOzs7REVUXVtJRDM7MC4wMDswLjAwOzAuMDA7MC4wMF1bSUQyOzI7MTI7MjAwLjAwOzI0LjAwMDBdO1tERVRMUkZMOzAwMDtSRUlNQlVSU0VNRU5UIFdBUkVIT1VTRSBGT1JLTElGVCBBTkQgT1RIRVIgRVFVSVBNRU5UOzEuMDA7MTEyLjAwOzAuMDA7MTEyLjAwOzs7REVUXVtJRDM7MC4wMDswLjAwOzAuMDA7MC4wMF1bSUQyOzAuMDA7MDA7MC4wMDswLjAwXWVtYWlsQ2xpZW50ZT1OZWxzb24uTWVuZGV6QHBhbmFscGluYS5jb207Q09ESUdPSU5URVJOT1NBUD0wMDAwMDAxNTc7Q09ESUdPSU5URVJOT1NBUENMSUVOVEU9Njc5MDY4OTI7UkVGRVJFTkNFPTtIQVdCL0JMPVNBTzc1NTg4NDE=</binaryFile></RPSReply>");
	 %>Reply sent to oubound queue. Check in Azure monitoring<%
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

OR

<form action = "main.jsp" method = "GET">
 		<input type = "hidden" name = "push" value="r" />
         <input type = "submit" value = "Simulate reply" />
</form>
