package com.employee.management.domain;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

public class HttpResponse {
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "MM-dd-yyyy hh:mm:ss",timezone = "Asia/Kolkata")
	private Date timesStamp;
	private int httpStatusCode;
	private HttpStatus httpStatus;
	private String reason;
	private String message;
	public HttpResponse() {}
	public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
		this.timesStamp= new Date();
		this.httpStatusCode = httpStatusCode;
		this.httpStatus = httpStatus;
		this.reason = reason;
		this.message = message;
	}
	public Date getTimesStamp() {
		return timesStamp;
	}
	public void setTimesStamp(Date timesStamp) {
		this.timesStamp = timesStamp;
	}
	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
