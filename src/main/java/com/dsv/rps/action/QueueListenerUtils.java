package com.dsv.rps.action;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dsv.rps.logging.Error;
import com.dsv.rps.logging.LogGroup;
import com.dsv.rps.logging.RollingLogs;
import com.dsv.rps.resources.Constants;
import com.dsv.rps.utils.RpsProcess;
import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

public class QueueListenerUtils {

	private static QueueClient queueClient = null;
	private static ExecutorService executorService = null;
	
	private static boolean started = false;
	
    public static boolean startListening() throws Exception, ServiceBusException
    {
    	if (!started)
    	{
    		System.out.println("start listening");
    		queueClient = new QueueClient(new ConnectionStringBuilder(Constants.RPS_IN_CONNECTION_STRING,Constants.RPS_IN_QUEUE_NAME), ReceiveMode.PEEKLOCK);
    		executorService = Executors.newSingleThreadExecutor();
    		registerReceiver();
    		started = true;
    		return true;
    	}
    	return false;
    }
    
    public static void stopListening() throws ServiceBusException
    {
    	queueClient.close();
         executorService.shutdown();
    }
    
    static void registerReceiver() throws Exception {
    	
    	 // register the RegisterMessageHandler callback with executor service
        queueClient.registerMessageHandler(new IMessageHandler()
        {
	           // callback invoked when the message handler loop has obtained a message
	           public CompletableFuture<Void> onMessageAsync(IMessage message)
	           {
	        	   System.out.println("*********************************************");
	        	   System.out.println("\tnew message " + message.getMessageId());
	        	   System.out.println(message.getContentType() + " " + message.getCorrelationId() + " " + message.getLabel() + " " + message.getExpiresAtUtc() + " " + message.getTimeToLive());
	        	   RollingLogs.addItem("Incoming new message: Id = "+message.getMessageId(),LogGroup.IN_QUEUE);
	               // receives message is passed to callback
	               if (message.getLabel() != null &&
	                       message.getContentType() != null &&
	                       message.getLabel().contentEquals("Scientist") &&
	                       message.getContentType().indexOf("application/xml")>-1) {
	
	            	   byte[] body = message.getBody();
	                   // Map scientist = GSON.fromJson(new String(body, UTF_8), Map.class);
	                    String result = new String(body, UTF_8);
	                    System.out.println("result " + result);
	                    RollingLogs.addItem("Content of message: Id = "+message.getMessageId() + " : " + result,LogGroup.PROCESS);
	                    
	               }
	               else if (message.getContentType().indexOf("application/xml")>-1)
	               {
	            	   byte[] body = message.getBody();
	                   // Map scientist = GSON.fromJson(new String(body, UTF_8), Map.class);
	                    String result = new String(body, UTF_8);
	                    System.out.println("result " + (result.length()>100?(result.substring(0,100) + "..."):result));
	                    RollingLogs.addItem("Now processing message id = "+message.getMessageId() + " : " + (result.length()>100?(result.substring(0,100) + "..."):result),LogGroup.PROCESS);
	                    RpsProcess rpsProcess = new RpsProcess();
	                    
	                    try
	                    {
	                    	QueueResponseUtils.sendMessagetoQueue(rpsProcess.process(message));
	                    }
	                    catch (Exception e)
	                    {
	                    	e.printStackTrace();
	                    	RollingLogs.addItem("Could not send message back to queue id " + message.getMessageId() + " : " + e.getMessage(),LogGroup.ERROR);
	                    }
	                    
	                    
	               }
	               else
	               {
	            	   RollingLogs.addItem(Error.UNREADABLE_XML.getText() + " : Id = "+message.getMessageId() ,LogGroup.ERROR);
	                   
	               }
	               return CompletableFuture.completedFuture(null);
	           }
		
               // callback invoked when the message handler has an exception to report
               public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase)
               {
                   System.out.printf(exceptionPhase + "-" + throwable.getMessage());
               }
           },
            // 1 concurrent call, messages are auto-completed, auto-renew duration
            new MessageHandlerOptions(1, true, Duration.ofMinutes(1)),executorService);

    }
    
    public static boolean isStarted()
    {
    	return started;
    }
}
