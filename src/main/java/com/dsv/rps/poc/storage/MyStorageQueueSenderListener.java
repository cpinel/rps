package com.dsv.rps.poc.storage;

// Include the following imports to use queue APIs.
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.queue.*;


public class MyStorageQueueSenderListener {

	// Define the connection-string with your values.
	/*public static final String storageConnectionString =
	    "DefaultEndpointsProtocol=https;AccountName=queuelist;AccountKey=wXRd3oCe1IB50jyE6+j4Ti8rwITAOlWnsVWwLaIxTMwGYK05QICmRTY2qmgMxBHY5QZz/mkYzgMOR9STjYrhuA==;EndpointSuffix=core.windows.net";
	*/
	public static final String storageConnectionString =
	"Endpoint=sb://dev-cwf-servicebus.servicebus.windows.net/;SharedAccessKeyName=dev-esb-panalpina-out-sas;SharedAccessKey=3GL6OoneZLOtCMn9fu1eTq6qX6KEeAs2MbU95JgTcIY=";
	// Retrieve storage account from connection-string.
	//String storageConnectionString =
	 //   RoleEnvironment.getConfigurationSettings().get("StorageConnectionString");

	
	public static void main ( String [] args)

	{
		
		
		// create queue ???
		/*try
		{
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		   // Create the queue client.
		   CloudQueueClient queueClient = storageAccount.createCloudQueueClient();

		   // Retrieve a reference to a queue.
		   CloudQueue queue = queueClient.getQueueReference("rpsin");

		   // Create the queue if it doesn't already exist.
		   queue.createIfNotExists();
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}*/
		
		
		// write 
		/*
		try
		{
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the queue client.
		    CloudQueueClient queueClient = storageAccount.createCloudQueueClient();

		    // Retrieve a reference to a queue.
		    CloudQueue queue = queueClient.getQueueReference("rpsin");

		    // Create the queue if it doesn't already exist.
		    queue.createIfNotExists();

		    // Create a message and add it to the queue.
		    CloudQueueMessage message = new CloudQueueMessage("Salut je t'écris un truc que tu pourras lire " + Math.random()*500);
		    queue.addMessage(message);
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
		
		*/
		// read furtif
		try
		{
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the queue client.
		    CloudQueueClient queueClient = storageAccount.createCloudQueueClient();

		    // Retrieve a reference to a queue.
		    CloudQueue queue = queueClient.getQueueReference("dev-esb-panalpina-out-test-queue");

		    // Peek at the next message.
		    //CloudQueueMessage peekedMessage = queue.retrieveMessage(); // consomme
		    CloudQueueMessage peekedMessage = queue.peekMessage(); // lit sans consommer
		    

		    // Output the message value.
		    if (peekedMessage != null)
		    {
		      System.out.println(peekedMessage.getMessageContentAsString());
		   }
		    else
		    {
			      System.out.println("y a plus rien");
		    }
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
		
	}
}
