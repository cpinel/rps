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


public class QueueResponseUtils {

    static final Gson GSON = new Gson();
    
    static QueueClient sendClient;
    
  public static String sendMessagetoQueue(String xmltext) throws Exception {
    	
	  sendClient = new QueueClient(new ConnectionStringBuilder(Constants.RPS_OUT_CONNECTION_STRING, Constants.RPS_OUT_QUEUE_NAME), ReceiveMode.PEEKLOCK);
      sendMessagesAsync(xmltext).thenRunAsync(() -> sendClient.closeAsync());

       return "ok";
  }
  
  static CompletableFuture<Void> sendMessagesAsync(String xmltext) {
	  
      Message message = new Message(xmltext);
      message.setContentType("application/xml");
      message.setLabel("RPS-reply");
      message.setTimeToLive(Duration.ofMinutes(2000));
      System.out.printf("Message sending: Id = %s\n", message.getMessageId());
      RollingLogs.addItem("Message sending: Id = "+message.getMessageId(),LogGroup.PROCESS);
      
      List<CompletableFuture> tasks = new ArrayList<>();
      tasks.add(
    		  sendClient.sendAsync(message).thenRunAsync(() -> {
                  System.out.printf("\tMessage acknowledged: Id = %s\n", message.getMessageId());
                  RollingLogs.addItem("Reply sent to queue: Id = "+message.getMessageId(),LogGroup.OUT_QUEUE);
              }));
      
      
      return CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[tasks.size()]));
  }
  
}