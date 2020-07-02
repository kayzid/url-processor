package com.ebay.url.processor.service.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlFileTest {
	UrlFile urlFile;

	@BeforeEach
	void setUp() throws Exception {
		urlFile=new UrlFile("Test");
	}

	@Test
	void testToString() {
		assertEquals("name:Test",urlFile.toString());
		assertEquals("Test",urlFile.getFileName());
	}

}
