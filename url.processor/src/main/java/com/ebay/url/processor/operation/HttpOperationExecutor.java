package com.ebay.url.processor.operation;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ebay.url.processor.service.domain.Url;
import com.ebay.url.processor.service.url.UrlServiceException;

/**
 * The http operation executor class which is reponsible
 * for executing the http get operations. This class
 * will keep a count of urls processed, unsuccessully
 * processed urls and total successfully processed urls.
 * The counts will be incremented atomically to as 
 * multiple threads will be executing the http get requests
 * 
 * 
 * @author Keyur Chitnis
 *
 */
public class HttpOperationExecutor {
	
	private static final Logger logger = LogManager.getLogger(HttpOperationExecutor.class);

	private CloseableHttpClient httpclient;
	private AtomicInteger totalUrls;
	private AtomicInteger unsuccessfulCount;
	private AtomicInteger successCount;
	
	public HttpOperationExecutor(AtomicInteger totalUrls,AtomicInteger unsuccessfulCount ,AtomicInteger successCount) {
		this.httpclient= HttpClients.createDefault();
		this.totalUrls=totalUrls;
		this.unsuccessfulCount=unsuccessfulCount;
		this.successCount=successCount;
		
	}
	

	/**
	 * Method to execute the http get request.
	 * @param url the url to execute the get request on.
	 * @throws UrlServiceException exception thrown incase of connectivity issues.
	 */
	public void executeHttpGetRequest(Url url) throws UrlServiceException {
		
		logger.debug("Begin request.");
		
		HttpGet httpGet=new HttpGet(url.getUrl());
		try {
			CloseableHttpResponse response= httpclient.execute(httpGet);
			
			totalUrls.getAndIncrement();
			if(response.getStatusLine().getStatusCode() != 200) {
				unsuccessfulCount.getAndIncrement();
				url.setHttpStatus(response.getStatusLine().getStatusCode());
				logger.debug("Url processed: {} with response: {} ",url.getUrl(),response.getStatusLine().getStatusCode());
				
			}
			else {
				successCount.getAndIncrement();
			}
			
			url.setUrlHitCount(url.getUrlHitCount()+1);
			if(totalUrls.get() % 1000 == 0) {
				logger.info("Total Urls processed: {} Unuccessful Urls: {}  Successful: {}",totalUrls.get(),unsuccessfulCount.get(),successCount.get());
			}
			
			logger.debug("Url processed: {} with response: {} ",url.getUrl(),response.getStatusLine().getStatusCode());
			
		} catch (Exception e) {
			logger.error("Exception in executing the GET request ",e);
			throw new UrlServiceException(e);
		}
	}
	
	@PreDestroy
	public void destroy() {
		try {
			httpclient.close();
		} catch (IOException e) {
			logger.error("Exception : ",e);			
		}
	}
	
	
	
	public void setHttpclient(CloseableHttpClient httpclient) {
		this.httpclient = httpclient;
	}
}
