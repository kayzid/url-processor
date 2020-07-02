package com.ebay.url.processor.service.file;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileProcessorTest {
	private FileProcessor fileProcessor;

	@BeforeEach
	void setUp() throws Exception {
		fileProcessor=new FileProcessor();
	}

	@Test
	void processFilesFromInputDirectoryUseDefaultTest() {
		try {
			final FileProcessorResponse fpr=fileProcessor.processFilesFromInputDirectory("");
			assertTrue(fpr.getFiles().size() >0 );
		} catch (FileProcessorException e) {
			fail();
		}
		
	}
	
	@Test
	void processFilesFromInputDirectoryNonExistnatDirectoryTest() {
		try {
			final FileProcessorResponse fpr=fileProcessor.processFilesFromInputDirectory("inputData");
			fail();
		} catch (FileProcessorException e) {
			assertNotNull(e);
			assertEquals("java.nio.file.NoSuchFileException: inputData",e.getMessage());			
		}
		
	}

}
