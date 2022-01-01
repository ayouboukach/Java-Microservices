package com.learning.exception;

import static com.learning.constant.ExceptionHandlingConstant.ACCOUNT_DISABLED;
import static com.learning.constant.ExceptionHandlingConstant.ACCOUNT_LOCKED;
import static com.learning.constant.ExceptionHandlingConstant.ERROR_PATH;
import static com.learning.constant.ExceptionHandlingConstant.ERROR_PROCESSING_FILE;
import static com.learning.constant.ExceptionHandlingConstant.INCORRECT_CREDENTIALS;
import static com.learning.constant.ExceptionHandlingConstant.MAIL_CONNECTIONS_FAILED;
import static com.learning.constant.ExceptionHandlingConstant.METHOD_IS_NOT_ALLOWED;
import static com.learning.constant.ExceptionHandlingConstant.NOT_ENOUGH_PERMISSION;
import static com.learning.constant.ExceptionHandlingConstant.NO_MAPPING_FOR_THIS_URL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.learning.exception.exceptions.EmailExistException;
import com.learning.exception.exceptions.EmailNotFoundException;
import com.learning.exception.exceptions.NotAnImageFileException;
import com.learning.exception.exceptions.UserNotFoundException;
import com.learning.exception.exceptions.UsernameExistException;
import com.learning.model.rest.HttpResponse;
import com.sun.mail.util.MailConnectException;

import lombok.extern.slf4j.Slf4j;

@Component
@RestControllerAdvice
@Slf4j
public class ExceptionHandling implements ErrorController {

	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
		return new ResponseEntity<>(new HttpResponse(null, httpStatus.value(), httpStatus,
				httpStatus.getReasonPhrase().toUpperCase(), message, null, null, null), httpStatus);
	}

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<HttpResponse> accountDisabledException() {
		return createHttpResponse(BAD_REQUEST, ACCOUNT_DISABLED);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<HttpResponse> badCredentialsException() {
		return createHttpResponse(BAD_REQUEST, INCORRECT_CREDENTIALS);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<HttpResponse> accessDeniedException() {
		return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
	}

	@ExceptionHandler(LockedException.class)
	public ResponseEntity<HttpResponse> lockedException() {
		return createHttpResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
		return createHttpResponse(UNAUTHORIZED, exception.getMessage());
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<HttpResponse> SqlExceptionHelper(SQLException exception) {
		return createHttpResponse(UNAUTHORIZED, exception.getMessage());
	}
	
	@ExceptionHandler(InternalAuthenticationServiceException.class)
	public ResponseEntity<HttpResponse> internalAuthenticationServiceException(InternalAuthenticationServiceException exception) {
		return createHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
	}

	@ExceptionHandler(EmailExistException.class)
	public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception) {
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<HttpResponse> validationLogin(MethodArgumentNotValidException exception) {
		String error = exception.getAllErrors() + "";
		return createHttpResponse(BAD_REQUEST,
				error.substring(error.lastIndexOf("default message [") + 17, error.length() - 2) + "");
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<HttpResponse> validationLogin(ConstraintViolationException exception) {
		String error = exception.getMessage() + "";
		return createHttpResponse(BAD_REQUEST,
				error.substring(error.lastIndexOf("messageTemplate='") + 17, error.length() - 4) + "");
	}

	@ExceptionHandler(MailConnectException.class)
	public ResponseEntity<HttpResponse> mailConnectException(MailConnectException exception) {
		return createHttpResponse(INTERNAL_SERVER_ERROR, MAIL_CONNECTIONS_FAILED);
	}

	@ExceptionHandler(UsernameExistException.class)
	public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException exception) {
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(SignatureVerificationException.class)
	public ResponseEntity<HttpResponse> signatureVerificationException(SignatureVerificationException exception) {
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception) {
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception) {
		return createHttpResponse(NOT_FOUND, exception.getMessage());
	}

//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<HttpResponse> noHandlerFoundException(NoHandlerFoundException e) {
//        return createHttpResponse(BAD_REQUEST, "There is no mapping for this URL");
//    }

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
		return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
		log.error(exception.getMessage());
		return createHttpResponse(INTERNAL_SERVER_ERROR, exception.getCause()+"");
	}

	@ExceptionHandler(NotAnImageFileException.class)
	public ResponseEntity<HttpResponse> notAnImageFileException(NotAnImageFileException exception) {
		log.error(exception.getMessage());
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
		log.error(exception.getMessage());
		return createHttpResponse(NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<HttpResponse> iOException(IOException exception) {
		log.error(exception.getMessage());
		return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
	}

	@RequestMapping(ERROR_PATH)
	public ResponseEntity<HttpResponse> notFound404() {
		return createHttpResponse(NOT_FOUND, NO_MAPPING_FOR_THIS_URL);
	}

//	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}
