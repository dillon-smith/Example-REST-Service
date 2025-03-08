package com.branchapp.github.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A {@code ClientHttpRequestInterceptor} which simply logs the {@link HttpRequest request} and
 * {@link ClientHttpRequestExecution response}.
 *
 * @TODO: Might be better to replace this with some type of logging Aspect
 * @see ClientHttpRequestInterceptor
 * @see HttpRequest
 * @see ClientHttpRequestExecution
 */
@Component
public class LoggingInterceptor implements ClientHttpRequestInterceptor
{
    private final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException
    {
        log.info("Calling HTTP {} {}", request.getMethod(), request.getURI());
        var response = execution.execute(request, body);
        log.info("Received HTTP {} from HTTP {} {}", response.getStatusCode(), request.getMethod(), request.getURI());
        return response;
    }
}
