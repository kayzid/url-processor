package com.ebay.url.processor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ebay.url.processor.operation.HttpOperationExecutor;
import com.ebay.url.processor.service.domain.Url;
import com.ebay.url.processor.service.file.FileProcessor;
import com.ebay.url.processor.service.file.FileProcessorResponse;
import com.ebay.url.processor.service.file.GZipFileReader;
import com.ebay.url.processor.service.url.HttpUrlService;

/**
 * This class is the entry point for processing the files
 * from directory and executing HTTP
 * operations on the urls from the files.
 * 
 * 
 * @author Keyur Chitnis
 *
 */

public class UrlProcessorService {
	
	private static final Logger logger = LogManager.getLogger(UrlProcessorService.class);

	private BlockingQueue<Url> blockingQueue=new ArrayBlockingQueue<>(2000);	
	private FileProcessor fileProcessor;
	private int producerCount;	
	private int consumerThreads;
	private int poisonPillNumber;
	private AtomicInteger totalUrls;
	private AtomicInteger unsuccessfulCount;
	private AtomicInteger successCount;
	// Initialize a new executor service with a size of producers + consumers+1.
	private ExecutorService executorService;
	
	public UrlProcessorService() {
		this.fileProcessor=new FileProcessor();
		this.producerCount=4;
		this.consumerThreads=Runtime.getRuntime().availableProcessors();
		this.poisonPillNumber=consumerThreads / producerCount;
		this.totalUrls=new AtomicInteger(0);
		this.unsuccessfulCount=new AtomicInteger(0);
		this.successCount=new AtomicInteger(0);
		this.executorService=Executors.newFixedThreadPool(producerCount+consumerThreads+1);
	}


	/**
	 * Method to read the file from input directory
	 * and proess the urls by executing an Http Get operation on them
	 * 
	 * 
	 * @param inputDirectory
	 */
	public void processFilesFromDirectory(String inputDirectory) {
		try {
			final FileProcessorResponse response=fileProcessor.processFilesFromInputDirectory(inputDirectory);	
			
			int start=0;
			int totalRecords=response.getFiles().size();
			
			int loopCount=totalRecords <= producerCount? 1: totalRecords/producerCount;
			int loopStart=0;
			long startTime=System.currentTimeMillis();
			logger.debug("Start : {}",startTime);
			
			
			
			while(loopStart < loopCount) {							
				
				List<Callable<Boolean>> callableExecutor=new ArrayList<>();
				
				// Submit all producers to the executor service.
				for(int i=0;i<producerCount && start < totalRecords;i++) {
					executorService.submit(new GZipFileReader(blockingQueue, response.getFiles().get(start++),poisonPillNumber));				
				}		
				
				// Build a list of consumers.
				for(int i=0;i<consumerThreads;i++) {
					callableExecutor.add(new HttpUrlService(blockingQueue, new HttpOperationExecutor(totalUrls,unsuccessfulCount,successCount)));
				}
				
				// submit them to the executor service.
				List<Future<Boolean>> status= executorService.invokeAll(callableExecutor);														
				loopStart++;
			}
			
			long end=System.currentTimeMillis();
			logger.debug("Total Procesing time in MS : {}",(end-startTime));

			logger.info("Finished Processing. Total Urls processed: {} Unuccessful Urls: {}  Successful: {}",totalUrls.get(),unsuccessfulCount.get(),successCount.get());
		}
		catch(Exception ex) {
			logger.error("Error ",ex);
		}
		finally {
			//shutdown the pool.
			executorService.shutdownNow();	
		}
		
	}
	
	
	public void setFileProcessor(FileProcessor pr) {
		this.fileProcessor = pr;
	}



	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	
}
