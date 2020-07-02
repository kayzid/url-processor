package com.ebay.url.processor.service.domain;

/**
 * 
 * The Pojo which holds
 * information about the url
 * which has to be processed.
 * 
 * @author Keyur Chitnis
 *
 */

public class Url {

	private String url;
	public String getUrl() {
		return url;
	}


	private int urlHitCount;
	private int httpStatus;
	
	
	public Url(String url)
	{
		this.url=url;
		this.urlHitCount=0;	
	}


	public int getUrlHitCount() {
		return urlHitCount;
	}


	public void setUrlHitCount(int urlHitCount) {
		this.urlHitCount = urlHitCount;
	}


	public int getHttpStatus() {
		return httpStatus;
	}


	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	
}
