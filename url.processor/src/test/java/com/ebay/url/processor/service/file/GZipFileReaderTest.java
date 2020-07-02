package com.ebay.url.processor.service.file;

import static org.junit.jupiter.api.Assertions.*;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.ebay.url.processor.service.domain.Url;
import com.ebay.url.processor.service.domain.UrlFile;


class GZipFileReaderTest {
	private GZipFileReader gzipFileReader;

	private BlockingQueue<Url> queue;

	@BeforeEach
	void setUp() throws Exception {
		queue=Mockito.mock(BlockingQueue.class);
		List<UrlFile> urlFiles = getFileMetadata("inputData");
		gzipFileReader = new GZipFileReader(queue, urlFiles.get(0), 4);

	}

	@Test
	void generateUrlsFromFileTest() {
		try {

			gzipFileReader.generateUrlsFromFile();
		} catch (Exception e) {
			fail(e);
		}
	}
	
	@Test
	void generateUrlsFromFileWithExceptionTest() {
		try {
			gzipFileReader = new GZipFileReader(queue, new UrlFile("test"), 4);			
			gzipFileReader.generateUrlsFromFile();
			fail();
		}
		catch(FileProcessorException fpe) {
			assertNotNull(fpe);
			assertEquals("java.io.FileNotFoundException: test (The system cannot find the file specified)",fpe.getMessage());
		}
		catch (Exception e) {
			fail(e);
		}
	}

	private List<UrlFile> getFileMetadata(String inputDirectory) throws IOException {

		final List<UrlFile> filesFromDirectory = new ArrayList<>();
		final StringBuilder sb = new StringBuilder();
		URL inoutData = getContextClassLoader().getResource(inputDirectory);
		sb.append(inoutData.getPath().substring(1));

		try (Stream<Path> paths = Files.walk(Paths.get(sb.toString()))) {
			paths.filter(Files::isRegularFile).forEach(f -> {
				filesFromDirectory.add(new UrlFile(f.toAbsolutePath().toString()));
			});
		}

		return filesFromDirectory;
	}

	private ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
}
