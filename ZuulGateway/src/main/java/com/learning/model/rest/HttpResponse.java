package com.learning.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

@Setter
@Getter
@Data
public class HttpResponse {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "Morocco/Casablanca")
	private Date timeStamp;
	private int httpStatusCode; // 200, 201, 400, 500
	private HttpStatus httpStatus;
	private String reason;
	private String message;
	private String path;
	private Map<?, ?> data;
	private String developperMessage;

	public HttpResponse(Date timeStamp, int httpStatusCode, HttpStatus httpStatus, String reason, String message,
			String path, Map<?, ?> data, String developperMessage) {
		super();
		this.timeStamp = new Date();
		this.httpStatusCode = httpStatusCode;
		this.httpStatus = httpStatus;
		this.reason = reason;
		this.message = message;
		this.path = path;
		this.data = data;
		this.developperMessage = "AYOUB OUKACH";
	}
}
