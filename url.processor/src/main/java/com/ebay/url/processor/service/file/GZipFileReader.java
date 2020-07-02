package com.ebay.url.processor.service.file;


import static com.ebay.url.processor.ProcessorConstants.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;
import java.util.zip.GZIPInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ebay.url.processor.ProcessorConstants;
import com.ebay.url.processor.service.domain.Url;
import com.ebay.url.processor.service.domain.UrlFile;

/**
 * 
 * GZip File reader class which will read 
 * each zip file from the directory and add
 * each url to the BlockingQueue so it can be
 * worked on by multiple threads.
 * 
 * This is similar to a multithreaded 
 * producer - consumer problem. The producer 
 * in this case is the GZipFileReader class which
 * is producing the urls which are consumed by 
 * the UrlOperation class.
 * 
 * @author Keyur Chitnis
 *
 */
public class GZipFileReader implements Runnable,FileReader {
	private static final Logger logger = LogManager.getLogger(GZipFileReader.class);
	
	private UrlFile file;
	private BlockingQueue<Url> blockingQueue;
	private int numberOfConsumers;
	
	
	public GZipFileReader(BlockingQueue<Url> blockingQueue,UrlFile file,int numberOfConsumers) {
		this.blockingQueue=blockingQueue;
		this.file=file;
		this.numberOfConsumers=numberOfConsumers;
	}
	
	
	@Override
	public void run() {
		try {
			generateUrlsFromFile();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Method to read the urls from a file.
	 * This method uses NIO FileChannel to open 
	 * the file in read only mode and read the contents
	 * of the gz file.
	 * The urls are then added to the queue.
	 * 
	 * @throws Exception
	 */
	public void generateUrlsFromFile() throws FileProcessorException
	{
				
		try(
				RandomAccessFile raf=new RandomAccessFile(file.getFileName(), "r");
				FileChannel fc=raf.getChannel();
				GZIPInputStream gzis =
	    		new GZIPInputStream(Channels.newInputStream(fc));
	    		InputStreamReader out =
	            new InputStreamReader(gzis,UTF_8);
				BufferedReader buffered = new BufferedReader(out)){
			
			String resource;
			
			while((resource = buffered.readLine()) != null) {
				blockingQueue.put(new Url(resource));
				logger.debug("Url: {}",resource);
			}
			
			for(int num=0;num<numberOfConsumers;num++) {
				blockingQueue.put(new Url(ProcessorConstants.POISON_PILL_INDICATOR));	
			}
				
	    }
		catch(IOException ex){
		       logger.error("Exception in reading a file! " , ex);
		       throw new FileProcessorException(ex);
		}
		catch(Exception ex){	       
	       logger.error("Something went wrong " , ex);
	       throw new FileProcessorException(ex);
	    }
			
		
	}
	
	
	

}
