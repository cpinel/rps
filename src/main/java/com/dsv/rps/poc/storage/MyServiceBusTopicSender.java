package com.dsv.rps.poc.storage;

import com.google.gson.reflect.TypeToken;
import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import com.google.gson.Gson;
import static java.nio.charset.StandardCharsets.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import org.apache.commons.cli.*;
import org.apache.commons.cli.DefaultParser;


public class MyServiceBusTopicSender {

    static final Gson GSON = new Gson();
    
    public static void main(String[] args) throws Exception, ServiceBusException {
        // TODO Auto-generated method stub

      /*  TopicClient sendClient;
        String connectionString = "Endpoint=sb://rps-service-bus.servicebus.windows.net/;SharedAccessKeyName=send-sb-key;SharedAccessKey=wQzt6kxogSppVQC6uvpC1N9ynjXRR6jJ7fS7nIhrjWs=";
        sendClient = new TopicClient(new ConnectionStringBuilder(connectionString, "topic-temp"));       
        sendMessagesAsync(sendClient).thenRunAsync(() -> sendClient.closeAsync());
        */
    }

  /*  static CompletableFuture<Void> sendMessagesAsync(TopicClient sendClient) {
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
                        new TypeToken<List<HashMap<String, String>>>() {
                        }.getType());

        List<CompletableFuture> tasks = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            final String messageId = Integer.toString(i);
            Message message = new Message(GSON.toJson(data.get(i), Map.class).getBytes(UTF_8));
            message.setContentType("application/json");
            message.setLabel("Scientist");
            message.setMessageId(messageId);
            message.setTimeToLive(Duration.ofMinutes(2));
            System.out.printf("Message sending: Id = %s\n", message.getMessageId());
            tasks.add(
                    sendClient.sendAsync(message).thenRunAsync(() -> {
                        System.out.printf("\tMessage acknowledged: Id = %s\n", message.getMessageId());
                    }));
        }
        return CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[tasks.size()]));
    }
    
    */
    
  public static String sendMessagetoTopic(String firstName, String lastName) throws Exception {
    	
    	TopicClient sendClient;
        String connectionString = "Endpoint=sb://rps-service-bus.servicebus.windows.net/;SharedAccessKeyName=send-sb-key;SharedAccessKey=wQzt6kxogSppVQC6uvpC1N9ynjXRR6jJ7fS7nIhrjWs=";
        sendClient = new TopicClient(new ConnectionStringBuilder(connectionString, "topic-temp"));       
        sendSingleMessageAsync(firstName,lastName,sendClient).thenRunAsync(() -> sendClient.closeAsync());
        return "ok";
  }

  static CompletableFuture<Void> sendSingleMessageAsync(String firstName, String lastName, TopicClient topicClient) throws Exception {

  List<HashMap<String, String>> data =
          GSON.fromJson(
                  "[" +
                  		"{'name' = '"+lastName+"', 'firstName' = '"+firstName+"'}," +
                          "]",
                  new TypeToken<List<HashMap<String, String>>>() {
                  }.getType());

  List<CompletableFuture> tasks = new ArrayList<>();
  for (int i = 0; i < data.size(); i++) {
      final String messageId = Integer.toString(i);
      
      Message message = new Message("<ShipmentInstructionMessage xmlns=\"http://esb.dsv.com/CDM/ShipmentInstructionMessage_V2\"><employee><firstname>" + firstName + "</firstname><lastname>" + lastName + "</lastname></employee></ShipmentInstructionMessage>"
    		  /*GSON.toJson(data.get(i), Map.class).getBytes(UTF_8)*/);
      message.setContentType("application/xml");
      message.setLabel("Scientist");
      message.setMessageId(messageId);
      message.setTimeToLive(Duration.ofMinutes(2));
      System.out.printf("Message sending: Id = %s\n", message.getMessageId());
      tasks.add(
    		  topicClient.sendAsync(message).thenRunAsync(() -> {
                  System.out.printf("\tMessage acknowledged: Id = %s\n", message.getMessageId());
              }));
  }
	/*  String xmlData = "<ShipmentInstructionMessage xmlns=\"http://esb.dsv.com/CDM/ShipmentInstructionMessage_V2\">" + 
	  		"<employee><firstname>" + firstName + "</firstname><lastname>" + lastName + "</lastname></employee></ShipmentInstructionMessage>";

	  List<CompletableFuture> tasks = new ArrayList<>();
	  Message message = new Message(xmlData.getBytes(UTF_8));
      message.setContentType("application/json");
      message.setLabel("Scientist");
      message.setMessageId("82");
      message.setTimeToLive(Duration.ofMinutes(2));
      System.out.printf("Message sending: Id = %s\n", message.getMessageId());
      tasks.add(
    		  topicClient.sendAsync(message).thenRunAsync(() -> {
                  System.out.printf("\tMessage acknowledged: Id = %s\n", message.getMessageId());
              }));
	  */
  return CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[tasks.size()]));
}

}