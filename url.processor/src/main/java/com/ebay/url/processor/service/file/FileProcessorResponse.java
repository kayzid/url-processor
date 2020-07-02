package com.ebay.url.processor.service.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ebay.url.processor.service.domain.UrlFile;

/**
 * A pojo for file processor
 * response which holds a list of files.
 * 
 * @author Keyur Chitnis
 *
 */
public class FileProcessorResponse {
	
	private List<UrlFile> files=new ArrayList<>();
	
	
	public List<UrlFile> getFiles() {
		return files;
	}

	public FileProcessorResponse(Collection<UrlFile> files) {
		this.files.addAll(files);		
	}

}
