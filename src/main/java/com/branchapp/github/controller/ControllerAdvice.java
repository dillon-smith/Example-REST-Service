package com.branchapp.github.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A {@code RestControllerAdvice} that extends {@code ResponseEntityExceptionHandler} to get out-of-the box RFC 9457
 * compliant error responses.
 * <p>
 * This class also provides a generic error handler for any exceptions not caught by
 * {@code ResponseEntityExceptionHandler} base functionality.
 *
 * @see ResponseEntityExceptionHandler
 * @see org.springframework.http.ProblemDetail
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457.html">RFC 9457</a>
 * @see <a href="https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-rest-exceptions.html">Spring
 *         Error Responses</a>
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler
{

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllOther(Exception ex, WebRequest request)
    {
        var pd = createProblemDetail(ex, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", null, null, request);
        return handleExceptionInternal(ex, pd, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
