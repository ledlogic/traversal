package com.github.ledlogic.traveral.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TraverseRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// input
	private String initalUrl;
	private String targetUrl;

	// output
	private String resultUrl;
	private int statusCode;
	private String entity;
	private boolean matched;
	private String exceptionMessage;
	
	public TraverseRequest() {
	}
	
	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	public String getEntity() {
		return entity;
	}

	public void setResultUrl(String resultUrl) {
		this.resultUrl = resultUrl;
	}
	
	public String getResultUrl() {
		return resultUrl;
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getInitalUrl() {
		return initalUrl;
	}

	public void setInitalUrl(String initalUrl) {
		this.initalUrl = initalUrl;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public boolean isMatched() {
		return matched;
	}

	public void setMatched(boolean matched) {
		this.matched = matched;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}