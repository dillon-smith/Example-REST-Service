package com.branchapp.github.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URI;

import static org.mockito.Mockito.*;

class LoggingInterceptorTest
{
    private LoggingInterceptor interceptor = new LoggingInterceptor();
    private Logger logger;

    private HttpRequest request;
    private ClientHttpResponse response;
    private ClientHttpRequestExecution execution;

    @BeforeEach
    public void setup() throws IOException
    {
        logger = mock(Logger.class);
        request = mock(HttpRequest.class);
        response = mock(ClientHttpResponse.class);
        execution = mock(ClientHttpRequestExecution.class);
        ReflectionTestUtils.setField(interceptor, "log", logger);
        mockRestCall();
    }

    private void mockRestCall() throws IOException
    {
        mockHttpRequest();
        mockClientHttpResponse();
        mockExecution();
    }

    private void mockHttpRequest()
    {
        when(request.getMethod()).thenReturn(HttpMethod.GET);
        when(request.getURI()).thenReturn(URI.create("www.example.com"));
    }

    private void mockClientHttpResponse() throws IOException
    {
        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
    }

    private void mockExecution() throws IOException
    {
        when(execution.execute(any(), any())).thenReturn(response);
    }

    @Test
    public void verifyRequestLogged() throws IOException
    {
        interceptor.intercept(request, new byte[0], execution);
        verify(logger).info("Calling HTTP {} {}", request.getMethod(), request.getURI());
    }

    @Test
    public void verifyResponseLogged() throws IOException
    {
        interceptor.intercept(request, new byte[0], execution);
        verify(logger).info("Received HTTP {} from HTTP {} {}", response.getStatusCode(), request.getMethod(), request.getURI());
    }
}