package com.dsv.rps.poc.servicebus;


import static java.nio.charset.StandardCharsets.UTF_8;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

public class MyServiceBusQueueSender {

	
	static final Gson GSON = new Gson();
		
	  public static void main(String[] args) throws Exception, ServiceBusException {
		// Create a QueueClient instance and then asynchronously send messages.
		// Close the sender once the send operation is complete.
		
		String connectionString = "Endpoint=sb://servicebusrps.servicebus.windows.net/;SharedAccessKeyName=rps-in-queue;SharedAccessKey=0ljlOw78Yb0CSuDwU6LbSWey8xo/TIUMywXYnqyaHTI=;EntityPath=rpsin";
		String QueueName = "rpsin";
		// Create a QueueClient instance for receiving using the connection string builder
        // We set the receive mode to "PeekLock", meaning the message is delivered
        // under a lock and must be acknowledged ("completed") to be removed from the queue
        QueueClient receiveClient = new QueueClient(new ConnectionStringBuilder(connectionString,QueueName), ReceiveMode.PEEKLOCK);
        // We are using single thread executor as we are only processing one message at a time
    	ExecutorService executorService = Executors.newSingleThreadExecutor();
        //registerReceiver(receiveClient, executorService);

        // Create a QueueClient instance for sending and then asynchronously send messages.
        // Close the sender once the send operation is complete.
        QueueClient sendClient = new QueueClient(new ConnectionStringBuilder(connectionString, QueueName), ReceiveMode.PEEKLOCK);
        sendMessagesAsync(sendClient).thenRunAsync(() -> sendClient.closeAsync());

        // wait for ENTER or 10 seconds elapsing
        waitForEnter(10);

        // shut down receiver to close the receive loop
        receiveClient.close();
        executorService.shutdown();
    }
	  
	  private static void waitForEnter(int seconds) {
	        ExecutorService executor = Executors.newCachedThreadPool();
	        try {
	            executor.invokeAny(Arrays.asList(() -> {
	                System.in.read();
	                return 0;
	            }, () -> {
	                Thread.sleep(seconds * 1000);
	                return 0;
	            }));
	        } catch (Exception e) {
	            // absorb
	        }
	    }
	  
	  static CompletableFuture<Void> sendMessagesAsync(QueueClient sendClient) {
	        List<HashMap<String, String>> data =
	                GSON.fromJson(
	                        "[" +
	                                "{'name' = 'Einstein', 'firstName' = 'Albert'}," +
	                                "{'name' = 'Heisenberg', 'firstName' = 'Werner'}," +
	                                "{'name' = 'Curie', 'firstName' = 'Marie'}," +
	                                "{'name' = 'Hawking', 'firstName' = 'Steven'}," +
	                                "{'name' = 'Newton', 'firstName' = 'Isaac'}," +
	                                "{'name' = 'Bohr', 'firstName' = 'Niels'}," +
	                                "{'name' = 'Faraday', 'firstName' = 'Michael'}," +
	                                "{'name' = 'Galilei', 'firstName' = 'Galileo'}," +
	                                "{'name' = 'Kepler', 'firstName' = 'Johannes'}," +
	                                "{'name' = 'Kopernikus', 'firstName' = 'Nikolaus'}" +
	                                "]",
	                        new TypeToken<List<HashMap<String, String>>>() {}.getType());

	        List<CompletableFuture> tasks = new ArrayList<>();
	        for (int i = 0; i < data.size(); i++) {
	            final String messageId = Integer.toString(i);
	            Message message = new Message(GSON.toJson(data.get(i), Map.class).getBytes(UTF_8));
	            message.setContentType("application/json");
	            message.setLabel("Scientist");
	            message.setMessageId(messageId);
	            message.setTimeToLive(Duration.ofMinutes(2));
	            System.out.printf("\nMessage sending: Id = %s", message.getMessageId());
	            tasks.add(
	                    sendClient.sendAsync(message).thenRunAsync(() -> {
	                        System.out.printf("\n\tMessage acknowledged: Id = %s", message.getMessageId());
	                    }));
	        }
	        return CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[tasks.size()]));
	    }

	     static void registerReceiver(QueueClient queueClient, ExecutorService executorService) throws Exception {

	    	
	        // register the RegisterMessageHandler callback with executor service
	        queueClient.registerMessageHandler(new IMessageHandler() {
	                                               // callback invoked when the message handler loop has obtained a message
	                                               public CompletableFuture<Void> onMessageAsync(IMessage message) {
	                                                   // receives message is passed to callback
	                                                   if (message.getLabel() != null &&
	                                                           message.getContentType() != null &&
	                                                           message.getLabel().contentEquals("Scientist") &&
	                                                           message.getContentType().contentEquals("application/json")) {

	                                                       byte[] body = message.getBody();
	                                                       Map scientist = GSON.fromJson(new String(body, UTF_8), Map.class);

	                                                       System.out.printf(
	                                                               "\n\t\t\t\tMessage received: \n\t\t\t\t\t\tMessageId = %s, \n\t\t\t\t\t\tSequenceNumber = %s, \n\t\t\t\t\t\tEnqueuedTimeUtc = %s," +
	                                                                       "\n\t\t\t\t\t\tExpiresAtUtc = %s, \n\t\t\t\t\t\tContentType = \"%s\",  \n\t\t\t\t\t\tContent: [ firstName = %s, name = %s ]\n",
	                                                               message.getMessageId(),
	                                                               message.getSequenceNumber(),
	                                                               message.getEnqueuedTimeUtc(),
	                                                               message.getExpiresAtUtc(),
	                                                               message.getContentType(),
	                                                               scientist != null ? scientist.get("firstName") : "",
	                                                               scientist != null ? scientist.get("name") : "");
	                                                   }
	                                                   return CompletableFuture.completedFuture(null);
	                                               }

	                                               // callback invoked when the message handler has an exception to report
	                                               public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
	                                                   System.out.printf(exceptionPhase + "-" + throwable.getMessage());
	                                               }
	                                           },
	                // 1 concurrent call, messages are auto-completed, auto-renew duration
	                new MessageHandlerOptions(1, true, Duration.ofMinutes(1)),
	                executorService);

	    }

	
		
}
