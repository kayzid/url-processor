package com.ebay.url.processor.service.url;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlServiceExceptionTest {

	UrlServiceException exception;
	
	@BeforeEach
	void setUp() throws Exception {
		exception=new UrlServiceException("test");
	}

	@Test
	void constructorTest() {
		exception=new UrlServiceException("test");
		assertEquals("test",exception.getMessage());
		exception=new UrlServiceException(new IOException("test"));
		assertEquals("java.io.IOException: test",exception.getMessage());
		
	}

}
