package com.ebay.url.processor.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.ebay.url.processor.service.domain.UrlFile;
import com.ebay.url.processor.service.file.FileProcessor;
import com.ebay.url.processor.service.file.FileProcessorException;
import com.ebay.url.processor.service.file.FileProcessorResponse;

class UrlProcessorServiceTest {
	
	private UrlProcessorService urlProcessorService;
	private ExecutorService mockExecutorService;
	private FileProcessor fileProcessorMock;

	@BeforeEach
	void setUp() throws Exception {
		this.urlProcessorService=new UrlProcessorService();
		this.fileProcessorMock=Mockito.mock(FileProcessor.class);
		this.mockExecutorService=Mockito.mock(ExecutorService.class);
		urlProcessorService.setExecutorService(mockExecutorService);
		urlProcessorService.setFileProcessor(fileProcessorMock);
	}

	@Test
	void processFilesFromDirectoryEqualsToProducerCount() {
		try {
			when(fileProcessorMock.processFilesFromInputDirectory(Mockito.anyString())).thenReturn(generateFileProcessorResponse(5));
			urlProcessorService.processFilesFromDirectory("test");
			assert(true);
			
		} catch (FileProcessorException e) {
			fail();
		}
		
		
	}
	
	
	@Test
	void processFilesFromDirectoryLessthanProducerCount() {
		try {
			when(fileProcessorMock.processFilesFromInputDirectory(Mockito.anyString())).thenReturn(generateFileProcessorResponse(1));
			urlProcessorService.processFilesFromDirectory("test");
			assert(true);
			
		} catch (FileProcessorException e) {
			fail();
		}
		
		
	}
	
	@Test
	void processFilesFromDirectoryExceptionTest() {
		try {
			when(fileProcessorMock.processFilesFromInputDirectory(Mockito.anyString())).thenReturn(generateFileProcessorResponse(1));
			when(mockExecutorService.invokeAll(Mockito.any())).thenThrow(new RuntimeException("Test!"));
			urlProcessorService.processFilesFromDirectory("test");
			
			
		} catch (FileProcessorException e) {
			fail();
		}
		catch (Exception e) {
			fail();
		}
		
		
	}
	
	
	private FileProcessorResponse generateFileProcessorResponse(int count) {
		final List<UrlFile> files=new ArrayList<>();
		while(count >= 0) {
			files.add(new UrlFile("test"));
			count--;
		}
		final FileProcessorResponse fileProcessorResponse=new FileProcessorResponse(files);
		return fileProcessorResponse;
	}
	
	

}
