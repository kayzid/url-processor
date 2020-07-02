package com.ebay.url.processor;

import static com.ebay.url.processor.ProcessorConstants.EMTPY_STRING;
import static com.ebay.url.processor.ProcessorConstants.SPACE_STRING;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ebay.url.processor.service.UrlProcessorService;


/**
 * 
 * Entry point into the application.
 * 
 * @author Keyur Chitnis
 *
 */
public class Application {
	
	private static final Logger logger = LogManager.getLogger(Application.class);
	
	public static void main(String[] args) {
		
		String directoryName="";
		if(args.length > 0) {	
			if(args[0].equals(EMTPY_STRING) || args[0].equals(SPACE_STRING))
			{
				logger.error("Please pass in the input directory as an argument!");
				return;
			}
				
			directoryName=args[0];
		}
		else {
			logger.error("Please pass in the input directory as an argument!");
			return;
		}
		
		
		logger.info("Reading files from directory : {}",directoryName);
		
		final UrlProcessorService urlProcessorService=new UrlProcessorService();
		urlProcessorService.processFilesFromDirectory(directoryName);		
	}
	
}
