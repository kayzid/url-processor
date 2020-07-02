package com.ebay.url.processor.service.domain;

/**
 * 
 * A pojo to hold fileName and size
 * information.  The filename will be the absolute URL
 * of the file.
 * 
 * @author Keyur Chitnis
 *
 */
public class UrlFile {

	private String fileName;
	
	
	public UrlFile(String fileName) {
		this.fileName=fileName;
	}
	
	
	public String getFileName() {
		return fileName;
	}

	
	@Override
	public String toString() {
		final StringBuilder sb=new StringBuilder();
		sb.append("name:");
		sb.append(fileName);
		return sb.toString();
	}

}
