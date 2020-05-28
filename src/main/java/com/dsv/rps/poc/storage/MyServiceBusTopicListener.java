package com.dsv.rps.poc.storage;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

public class MyServiceBusTopicListener {

	public static String LAST_MESSAGE;
	
    //static final Gson GSON = new Gson();
    
    public static void startListening() throws Exception, ServiceBusException
    {
        SubscriptionClient subscription1Client = new SubscriptionClient(new ConnectionStringBuilder(
        		"Endpoint=sb://rps-service-bus.servicebus.windows.net/;SharedAccessKeyName=listen-sb-key;SharedAccessKey=iq9s9P7FdGLmqh0f18MEGqE3BKg7+1qaMb2cwZDW9gk=", "topic-temp/subscriptions/temp-sub"), ReceiveMode.PEEKLOCK);
               registerMessageHandlerOnClient(subscription1Client);
    }
    
    public static void main(String[] args) throws Exception, ServiceBusException {
    
    	// DSV Azure queue
    	//SubscriptionClient subscription1Client = new SubscriptionClient(new ConnectionStringBuilder(
    	//		"Endpoint=sb://dev-cwf-servicebus.servicebus.windows.net/;SharedAccessKeyName=dev-esb-panalpina-out-sas;SharedAccessKey=3GL6OoneZLOtCMn9fu1eTq6qX6KEeAs2MbU95JgTcIY=;EntityPath=dev-esb-panalpina-out-test-queue"), ReceiveMode.PEEKLOCK);
    		
    	// mon topic
    	SubscriptionClient subscription1Client = new SubscriptionClient(new ConnectionStringBuilder(
        		"Endpoint=sb://rps-service-bus.servicebus.windows.net/;SharedAccessKeyName=listen-sb-key;SharedAccessKey=iq9s9P7FdGLmqh0f18MEGqE3BKg7+1qaMb2cwZDW9gk=", "topic-temp/subscriptions/temp-sub"), ReceiveMode.PEEKLOCK);
               registerMessageHandlerOnClient(subscription1Client);
    	
//Mon Azure Topic        SubscriptionClient subscription1Client = new SubscriptionClient(new ConnectionStringBuilder("Endpoint=sb://rps-service-bus.servicebus.windows.net/;SharedAccessKeyName=listen-sb-key;SharedAccessKey=iq9s9P7FdGLmqh0f18MEGqE3BKg7+1qaMb2cwZDW9gk=", "topic-temp/subscriptions/temp-sub"), ReceiveMode.PEEKLOCK);
 //       SubscriptionClient subscription3Client = new SubscriptionClient(new ConnectionStringBuilder(connectionString, "BasicTopic/subscriptions/Subscription3"), ReceiveMode.PEEKLOCK);        

        registerMessageHandlerOnClient(subscription1Client);
        //registerMessageHandlerOnClient(subscription2Client);
        //registerMessageHandlerOnClient(subscription3Client);
    }
    
    static void registerMessageHandlerOnClient(SubscriptionClient receiveClient) throws Exception {

        // register the RegisterMessageHandler callback
        IMessageHandler messageHandler = new IMessageHandler() {
            // callback invoked when the message handler loop has obtained a message
            public CompletableFuture<Void> onMessageAsync(IMessage message) {
                // receives message is passed to callback
                if (message.getLabel() != null &&
                        message.getContentType() != null &&
                        message.getLabel().contentEquals("Scientist") &&
                        message.getContentType().contentEquals("application/xml")) {

                    byte[] body = message.getBody();
                   // Map scientist = GSON.fromJson(new String(body, UTF_8), Map.class);
                    String result = new String(body, UTF_8);
                    System.out.println("result" + result);
                    
                    LAST_MESSAGE = result;
                    /*System.out.printf(
                            "\n\t\t\t\t%s Message received: \n\t\t\t\t\t\tMessageId = %s, \n\t\t\t\t\t\tSequenceNumber = %s, \n\t\t\t\t\t\tEnqueuedTimeUtc = %s," +
                                    "\n\t\t\t\t\t\tExpiresAtUtc = %s, \n\t\t\t\t\t\tContentType = \"%s\",  \n\t\t\t\t\t\tContent: [ firstName = %s, name = %s ]\n",
                            receiveClient.getEntityPath(),
                            message.getMessageId(),
                            message.getSequenceNumber(),
                            message.getEnqueuedTimeUtc(),
                            message.getExpiresAtUtc(),
                            message.getContentType(),
                            scientist != null ? scientist.get("firstName") : "",
                            scientist != null ? scientist.get("name") : "");*/
                }
                
                /*if (message.getLabel() != null &&
                message.getContentType() != null &&
                message.getContentType().contentEquals("application/xml")) {
		
		            byte[] body = message.getBody();
		
		            System.out.printf(
		                    "\n\t\t\t\t%s Message received: \n\t\t\t\t\t\tMessageId = %s, \n\t\t\t\t\t\tSequenceNumber = %s, \n\t\t\t\t\t\tEnqueuedTimeUtc = %s," +
		                            "\n\t\t\t\t\t\tExpiresAtUtc = %s, \n\t\t\t\t\t\tContentType = \"%s\",  \n\t\t\t\t\t\tContent: [ %s ]\n",
		                    receiveClient.getEntityPath(),
		                    message.getMessageId(),
		                    message.getSequenceNumber(),
		                    message.getEnqueuedTimeUtc(),
		                    message.getExpiresAtUtc(),
		                    message.getContentType(),
		                    new String(body, UTF_8));
		
		        }
		        */
                
                return receiveClient.completeAsync(message.getLockToken());
            }
            
            public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
                System.out.printf(exceptionPhase + "-" + throwable.getMessage());
            }
        };

 
        receiveClient.registerMessageHandler(
                    messageHandler,
                    // callback invoked when the message handler has an exception to report
                // 1 concurrent call, messages aren't auto-completed, auto-renew duration
                new MessageHandlerOptions(1, false, Duration.ofMinutes(1)));

    }
}
