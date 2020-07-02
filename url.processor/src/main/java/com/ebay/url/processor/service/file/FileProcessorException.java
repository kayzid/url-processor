package com.ebay.url.processor.service.file;

/**
 * Exception class for any file processing exceptions.
 * 
 * @author Keyur Chitnis
 *
 */
public class FileProcessorException extends Exception {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = 7475267366574126245L;
	
	public FileProcessorException(String message) {
		super(message);
	}
	
	public FileProcessorException(Throwable th) {
		super(th);
	}
	

}
