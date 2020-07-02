package com.ebay.url.processor.service.file;

import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ebay.url.processor.service.domain.UrlFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * Class to process the files in the folder. 
 * This class takes in a folder and gets the names
 * of the files and their size information.
 * 
 * @author Keyur Chitnis
 *
 */
public class FileProcessor {
	private static final Logger logger = LogManager.getLogger(FileProcessor.class);
	
	private static final String DEFAULT_DIRECTORY="inputData";
	private static final String EMPTY_STRING="";
	
	
	public FileProcessor() {
	}
	
	/**
	 * Method to process files from input directory.
	 * 
	 * @param directoryName the directory name to process files from.
	 * @return FileProcessorResponse response class which holds the list of files.
	 * @throws FileProcessorException
	 */
	public FileProcessorResponse processFilesFromInputDirectory(String directoryName) throws FileProcessorException
	{	    
		try {
			boolean useDefault=useDefaultDirectory(directoryName);
			if(useDefault)
				directoryName=DEFAULT_DIRECTORY;
			
			final List<UrlFile> files=getFileMetadata(directoryName,useDefault);
		    	
		    if(files.size() == 0) {
		    	logger.error("No files in directory to process.");
		    	throw new FileProcessorException("No files to process!");
		    }
		    
		    final FileProcessorResponse response=new FileProcessorResponse(files);
		    
			return response;
		}
		catch(FileProcessorException fpe) {
			throw fpe;
		}
		catch(Exception ex) {
			logger.error("Exception in processing files : " ,ex);
			throw new FileProcessorException(ex);
		}
	    
	}
	
	/**
	 * Method to check if default directoryname
	 * should be used.
	 * 
	 * @param directoryName
	 * @return
	 */
	private boolean useDefaultDirectory(String directoryName) {
		if(directoryName == null || (EMPTY_STRING).equals(directoryName))
			return true;
		
		return false;
	}
	
	/**
	 * Method to generate file metadata.
	 * 
	 * @param inputDirectory the input directory to process. 
	 * @param useDefault use the default directory under resources.
	 * @return List<UrlFile> a collection of url file objects.
	 * @throws IOException
	 */
	private List<UrlFile> getFileMetadata(String inputDirectory, boolean useDefault) throws IOException {
		
		final List<UrlFile> filesFromDirectory=new ArrayList<>();
		final StringBuilder sb=new StringBuilder();
		if(useDefault) {
			URL inoutData=getContextClassLoader().getResource(inputDirectory);
			sb.append(inoutData.getPath().substring(1));
		}
		else {
			sb.append(inputDirectory);
		}
		
		
		try (Stream<Path> paths = Files.walk(Paths.get(sb.toString()))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach(f->{
		        	logger.debug("File Path : {} " , f.toAbsolutePath().toString());
		        	filesFromDirectory.add(new UrlFile(f.toAbsolutePath().toString()));
		        });
		} 
		
		return filesFromDirectory;
	}
	
	/**
	 * Method  to get class loader from the current thread context.
	 * @return
	 */
	private ClassLoader getContextClassLoader() {
	    return Thread.currentThread().getContextClassLoader();
	}

}
