package com.dsv.rps.action;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

  
  static CompletableFuture<Void> sendMessagesAsync(String firstName, String lastName) {
	  
	  
      final String messageId = Integer.toString((int)(Math.random()*100));
      
      Message message = new Message("<ShipmentInstructionMessage xmlns=\"http://esb.dsv.com/CDM/ShipmentInstructionMessage_V2\"><employee><firstname>" + firstName + "</firstname><lastname>" + lastName + "</lastname></employee></ShipmentInstructionMessage>");
      message.setContentType("application/xml");
      message.setLabel("Scientist");
      message.setMessageId(messageId);
      message.setTimeToLive(Duration.ofMinutes(2));
      System.out.printf("Message sending: Id = %s\n", message.getMessageId());
      
      List<CompletableFuture> tasks = new ArrayList<>();
      tasks.add(
    		  sendClient.sendAsync(message).thenRunAsync(() -> {
                  System.out.printf("\tMessage acknowledged: Id = %s\n", message.getMessageId());
              }));
      
      
      return CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[tasks.size()]));
  }
  
  
}