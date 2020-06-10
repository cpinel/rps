package com.dsv.rps.action;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.dsv.rps.logging.LogGroup;
import com.dsv.rps.logging.RollingLogs;
import com.dsv.rps.resources.Constants;
import com.google.gson.Gson;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;


public class QueueSenderUtils {

    static final Gson GSON = new Gson();
    
    static QueueClient sendClient;
    
  public static String sendMessagetoQueue(String firstName, String lastName) throws Exception {
    	
	  sendClient = new QueueClient(new ConnectionStringBuilder(Constants.RPS_IN_CONNECTION_STRING, Constants.RPS_IN_QUEUE_NAME), ReceiveMode.PEEKLOCK);
      sendMessagesAsync(firstName,lastName).thenRunAsync(() -> sendClient.closeAsync());

       return "ok";
  }
  
  public static String sendStaticFileToQueue() throws Exception {
  	
	  sendClient = new QueueClient(new ConnectionStringBuilder(Constants.RPS_IN_CONNECTION_STRING, Constants.RPS_IN_QUEUE_NAME), ReceiveMode.PEEKLOCK);
      sendMessagesAsync().thenRunAsync(() -> sendClient.closeAsync());
       return "ok";
  }
  
  public static String sendStaticFileToQueueWrong() throws Exception {
	  	
	  sendClient = new QueueClient(new ConnectionStringBuilder(Constants.RPS_IN_CONNECTION_STRING, Constants.RPS_IN_QUEUE_NAME), ReceiveMode.PEEKLOCK);
      sendMessagesAsyncWrong().thenRunAsync(() -> sendClient.closeAsync());
       return "ok";
  }

  
  static CompletableFuture<Void> sendMessagesAsync(String firstName, String lastName) {
	  
	  
      final String messageId = Integer.toString((int)(Math.random()*100));
      
      Message message = new Message("<ShipmentInstructionMessage xmlns=\"http://esb.dsv.com/CDM/ShipmentInstructionMessage_V2\"><employee><firstname>" + firstName + "</firstname><lastname>" + lastName + "</lastname></employee></ShipmentInstructionMessage>");
      message.setContentType("application/xml");
      message.setLabel("Scientist");
      message.setMessageId(messageId);
      message.setTimeToLive(Duration.ofMinutes(2));
      System.out.printf("Message sending: Id = %s\n", message.getMessageId());
      RollingLogs.addItem("Message sending: Id = "+message.getMessageId(),LogGroup.PROCESS);
      
      List<CompletableFuture> tasks = new ArrayList<>();
      tasks.add(
    		  sendClient.sendAsync(message).thenRunAsync(() -> {
                  System.out.printf("\tMessage acknowledged: Id = %s\n", message.getMessageId());
                  RollingLogs.addItem("Message sent to queue: Id = "+message.getMessageId(),LogGroup.OUT_QUEUE);
              }));
      
      
      return CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[tasks.size()]));
  }
  
 static CompletableFuture<Void> sendMessagesAsync()
 {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get("C:\\Users\\didie\\Desktop\\test rps\\sample.xml.txt"), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
	 
      Message message = new Message(contentBuilder.toString());
      message.setContentType("application/xml");
      message.setTimeToLive(Duration.ofMinutes(2));
      System.out.printf("Message sending: Id = XML ");
      RollingLogs.addItem("Message sending: Id = " + message.getMessageId(),LogGroup.PROCESS);
      
      List<CompletableFuture> tasks = new ArrayList<>();
      tasks.add(
    		  sendClient.sendAsync(message).thenRunAsync(() -> {
                  System.out.printf("\tMessage acknowledged: Id = " + message.getMessageId());
                  RollingLogs.addItem("Message sent to queue: Id = " + message.getMessageId(),LogGroup.OUT_QUEUE);
              }));
      
      
      return CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[tasks.size()]));
  }
 
 static CompletableFuture<Void> sendMessagesAsyncWrong()
 {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get("C:\\Users\\didie\\Desktop\\test rps\\sampleWrong.xml.txt"), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
	 
      Message message = new Message(contentBuilder.toString());
      message.setContentType("application/json");
      message.setTimeToLive(Duration.ofMinutes(2));
      System.out.printf("Message sending: Id = XML ");
      RollingLogs.addItem("Message sending: Id = " + message.getMessageId(),LogGroup.PROCESS);
      
      List<CompletableFuture> tasks = new ArrayList<>();
      tasks.add(
    		  sendClient.sendAsync(message).thenRunAsync(() -> {
                  System.out.printf("\tMessage acknowledged: Id = " + message.getMessageId());
                  RollingLogs.addItem("Message sent to queue: Id = " + message.getMessageId(),LogGroup.OUT_QUEUE);
              }));
      
      
      return CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[tasks.size()]));
  }
}