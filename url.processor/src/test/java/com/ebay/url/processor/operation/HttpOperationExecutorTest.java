package com.ebay.url.processor.operation;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.ebay.url.processor.operation.HttpOperationExecutor;
import com.ebay.url.processor.service.domain.Url;
import com.ebay.url.processor.service.url.UrlServiceException;

class HttpOperationExecutorTest {
	private HttpOperationExecutor httpOperationExecutor;
	
	CloseableHttpClient httpclient;
	
	CloseableHttpResponse httpResponse;
	
	StatusLine mockStatusLine;
	
	private AtomicInteger urlHitcount=new AtomicInteger(0);
	private AtomicInteger unsuccessfulCount=new AtomicInteger(0);
	private AtomicInteger successfulCount=new AtomicInteger(0);
	
	@BeforeEach
	void setUp() throws Exception {
		httpclient=Mockito.mock(CloseableHttpClient.class);
		httpResponse=Mockito.mock(CloseableHttpResponse.class);
		httpOperationExecutor=new HttpOperationExecutor(urlHitcount, unsuccessfulCount, successfulCount);
		httpOperationExecutor.setHttpclient(httpclient);
		mockStatusLine=Mockito.mock(StatusLine.class);
	}

	@Test
	void executeHttpGetRequestTest() {
		try {
			final Url url=new Url("http://www.test.com");
			when(httpResponse.getStatusLine()).thenReturn(mockStatusLine);
			when(mockStatusLine.getStatusCode()).thenReturn(200);		
			when(httpclient.execute(Mockito.any())).thenReturn(httpResponse);
			httpOperationExecutor.executeHttpGetRequest(url);
			assertTrue(urlHitcount.get() > 0);
			
		} catch (Exception e) {
			fail();
		} 
	}
	
	@Test
	void executeHttpGetWith500ErrorRequestTest() {
		try {
			final Url url=new Url("http://www.test.com");
			when(httpResponse.getStatusLine()).thenReturn(mockStatusLine);
			when(mockStatusLine.getStatusCode()).thenReturn(500);			
			when(httpclient.execute(Mockito.any())).thenReturn(httpResponse);
			httpOperationExecutor.executeHttpGetRequest(url);
			assertEquals(1,unsuccessfulCount.get());
			
		} catch (Exception e) {
			fail();
		} 
	}
	
	
	@Test
	void executeHttpGetRequestMoreThanThreholdTestTest() {
		try {
			urlHitcount.getAndSet(9999);
			final Url url=new Url("http://www.test.com");
			when(httpResponse.getStatusLine()).thenReturn(mockStatusLine);
			when(mockStatusLine.getStatusCode()).thenReturn(500);			
			when(httpclient.execute(Mockito.any())).thenReturn(httpResponse);
			httpOperationExecutor.executeHttpGetRequest(url);
			assertEquals(1,unsuccessfulCount.get());
			
		} catch (Exception e) {
			fail();
		} 
	}
	
	@Test
	void executeHttpGetRequestExceptionTest() {
		try {
		
						
			when(httpclient.execute(Mockito.any())).thenThrow(new IOException("Connection Error"));
			httpOperationExecutor.executeHttpGetRequest(new Url("https://www.test.com"));
			assertEquals(1,unsuccessfulCount.get());
			fail();
			
		} catch (UrlServiceException e) {
			assertNotNull(e.getMessage());
			assertEquals("java.io.IOException: Connection Error",e.getMessage());
		} 
		catch (Exception e) {
			fail();
		} 
	}

	
	@Test
	public void testDestory() {
		httpOperationExecutor.destroy();
	}
}
