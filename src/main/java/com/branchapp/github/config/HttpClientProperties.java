package com.branchapp.github.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * A set of HTTP client connection related properties.
 *
 * @see HttpClientConfig
 */
@Configuration
@ConfigurationProperties(prefix = "http.client")
@Data
public class HttpClientProperties
{

    private int connectTimeout;
    private int readTimeout;
    private int connectionRequestTimeout;
    private long timeToLive;
    private long evictIdleConnections;

}
