package com.ebay.url.processor.service.url;


import static com.ebay.url.processor.ProcessorConstants.POISON_PILL_INDICATOR;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ebay.url.processor.operation.HttpOperationExecutor;
import com.ebay.url.processor.service.domain.Url;

/**
 * 
 * Class for processing the URL. 
 * This class represents the Consumer 
 * in the producer-consumer problem
 * This class will process the {@link com.ebay.url.processor.service.domain.Url} from
 * the queue and use the {@link com.ebay.url.processor.operation.HttpOperationExecutor}
 * class to execute a GET operation.
 * This class uses a {@link java.util.concurrent.Callable} interface
 * to trigger multiple consumers.
 * 
 * @author Keyur Chitnis
 *
 */
public class HttpUrlService implements Callable<Boolean>,UrlService {

	private static final Logger logger = LogManager.getLogger(HttpUrlService.class);
	
	private HttpOperationExecutor executor;
	private BlockingQueue<Url> queue;
	
	public HttpUrlService(BlockingQueue<Url> queue,HttpOperationExecutor executor) {
		this.queue=queue;
		this.executor=executor;
	}


	@Override
	public Boolean call() throws Exception {
			return processUrls();
	}
	
	/**
	 * Method to process the urls by
	 * executing an Http Get operation.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean processUrls() throws Exception {
		while(true) {
			try
			{
				Url url=queue.take();
				if(url == null)
				{
					logger.warn("Null url received!Terminating operation!");
					return false;
				}
				if(validateUrl(url.getUrl()))
					return true;
					
				executor.executeHttpGetRequest(url);
				logger.debug("Processed : {}", url.getUrl());
			}
			catch(UrlServiceException upe) {
				logger.warn("Exeption in processing the url : " ,upe);
			}
			catch(InterruptedException e) {				
				logger.error("Excepion: ", e);
				Thread.currentThread().interrupt();				
			}						
		}		
	
	}
	
	/**
	 * Method to validate the url.
	 * Used in poison pill detection.
	 * 
	 * @param url
	 * @return
	 */
	private boolean validateUrl(String url) {
		if(POISON_PILL_INDICATOR.equals(url)) {
			logger.info("No more Urls to process for current thread.");
			return true;
		}
		return false;
	}

}
