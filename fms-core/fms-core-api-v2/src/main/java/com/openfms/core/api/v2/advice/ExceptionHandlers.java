package com.openfms.core.api.v2.advice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.openfms.core.api.v2.ApiError;
import com.openfms.model.exceptions.AuthenticationFailedException;
import com.openfms.model.exceptions.ConcurrencyException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;
import com.openfms.model.exceptions.NotAuthenticatedException;
import com.openfms.model.exceptions.NotAuthorizedException;
import com.openfms.model.exceptions.OperationFailedException;
import com.openfms.model.exceptions.VersioningException;

@ControllerAdvice
public class ExceptionHandlers {

	public static Log log = LogFactory.getLog(ExceptionHandlers.class);
	
	@ExceptionHandler(TypeMismatchException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiError handleTypeMismatchException(TypeMismatchException error) {
		return new ApiError(error);
	}
	
	@ExceptionHandler(ConcurrencyException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public ApiError handleConcurrencyException(ConcurrencyException error) {
		return new ApiError(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public List<ObjectError> handleMethodArgumentNotValidException(MethodArgumentNotValidException error) {
		log.info(" ----> exception: "+error.getClass(), error);
		return error.getBindingResult().getAllErrors();
	}

	/**
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ApiError handleBadCredentialsException(BadCredentialsException error) {
		log.info(" ----> exception: "+error.getClass(), error);
		return new ApiError(error);
	}
	**/
	
	@ExceptionHandler(VersioningException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public ApiError handleVersioningException(VersioningException error) {
		log.info(" ----> exception: "+error.getClass(), error);
		return new ApiError(error);
	}
	
	@ExceptionHandler(InvalidParameterException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiError handleInvalidParameterException(InvalidParameterException error) {
		log.info(" ----> exception: "+error.getClass(), error);
		return new ApiError(error);
	}
	
	@ExceptionHandler(DatabaseException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ApiError handleDatabaseException(DatabaseException error) {
		log.info(" ----> exception: "+error.getClass(), error);
		return new ApiError(error);
	}
	
	@ExceptionHandler(OperationFailedException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ApiError handleOperationFailedException(OperationFailedException error) {
		log.info(" ----> exception: "+error.getClass(), error);
		return new ApiError(error);
	}
	
	@ExceptionHandler(DisabledException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ResponseBody
	public ApiError handleDisabledException(DisabledException error) {
		log.info(" ----> exception: "+error.getClass(), error);
		return new ApiError(error);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ApiError handleEntityNotFoundException(EntityNotFoundException error) {
		log.info(" ----> exception: "+error.getClass(), error);
		return new ApiError(error);
	}
	
	@ExceptionHandler(NotAuthorizedException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ResponseBody
	public ApiError handleNotAuthorizedException(NotAuthorizedException error, HttpServletRequest request, HttpServletResponse response) {
		log.info(" ----> exception: "+error.getClass(), error);
		return new ApiError(error);
	}

	@ExceptionHandler(AuthenticationFailedException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ResponseBody
	public ApiError handleAuthenticationFailedException(AuthenticationFailedException error, HttpServletRequest request, HttpServletResponse response) {
		log.info(" ----> exception: "+error.getClass(), error);
		return new ApiError(error);
	}
	
	@ExceptionHandler(NotAuthenticatedException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ApiError handleNotAuthorizedException(NotAuthenticatedException error, HttpServletRequest request, HttpServletResponse response) {
		log.info(" ----> exception: "+error.getClass(), error);
		return new ApiError(error);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ApiError handleOtherException(Exception error) {
		log.info(" ----> exception: "+error.getClass(), error);
		return new ApiError(error);
	}
	

}
