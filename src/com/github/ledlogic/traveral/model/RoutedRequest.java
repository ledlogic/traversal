package com.github.ledlogic.traveral.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RoutedRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String entity;
	private String resultUrl;
	private int statusCode;
	
	public RoutedRequest() {
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
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}