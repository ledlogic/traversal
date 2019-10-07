package com.github.ledlogic.traveral.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TraverseRequest implements Serializable {

	private static final long serialVersionUID = 2L;
	
	// input
	private String reason;
	private String initialUrl;
	private String targetUrl;

	// output
	private String resultUrl;
	private int statusCode;
	private boolean matched;
	private String exceptionMessage;
	
	public TraverseRequest() {
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getInitialUrl() {
		return initialUrl;
	}

	public void setInitialUrl(String initialUrl) {
		this.initialUrl = initialUrl;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
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
	
	public static String[] getWriteHeader() {
		String[] ret = {
			"reason",
			"initialUrl",
			"targetUrl",			
			"resultUrl",
			"statusCode",
			"matched",
			"exceptionMessage"
		};
		return ret;
	}

	public String[] getWriteData() {
		String[] ret = {
			reason,
			initialUrl,
			targetUrl,			
			resultUrl,
			Integer.toString(statusCode),
			Boolean.toString(matched),
			exceptionMessage,
		};
		return ret;
	}

}