package com.ebay.url.processor.service.url;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.ebay.url.processor.operation.HttpOperationExecutor;
import com.ebay.url.processor.service.domain.Url;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

class HttpUrlServiceTest {
	private HttpUrlService httpUrlService;
	private BlockingQueue<Url> queue;
	private HttpOperationExecutor httpOperationExecutor;

	@BeforeEach
	void setUp() throws Exception {
		queue=Mockito.mock(BlockingQueue.class);
		httpOperationExecutor=Mockito.mock(HttpOperationExecutor.class);
		this.httpUrlService=new HttpUrlService(queue, httpOperationExecutor);
	}

	@Test
	void processUrlsWithPoisonPillTest() {
		try {
			when(queue.take()).thenReturn(new Url("http://127.0.0.1:8080")).thenReturn(new Url("NULL"));			
			boolean result=httpUrlService.processUrls();
			assertEquals(true,result);
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	void processUrlsWithNullUrlTest() {
		try {
			when(queue.take()).thenReturn(null);			
			boolean result=httpUrlService.processUrls();
			assertEquals(result,false);
		} catch (Exception e) {
			fail();
		}
	}
	

	@Test
	void destoryTest() {
		try {
			when(queue.take()).thenReturn(new Url("http://127.0.0.1:8080")).thenReturn(new Url("NULL"));			
			boolean result=httpUrlService.call();
			assertEquals(true,result);
		} catch (Exception e) {
			fail();
		}
	}

}
