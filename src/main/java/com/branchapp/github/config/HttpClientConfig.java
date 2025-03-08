package com.branchapp.github.config;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.concurrent.TimeUnit;

/**
 * This configuration provides a {@code ClientHttpRequestFactory} and {@code HttpClientConnectionManager}. HTTP
 * connection properties are sourced from {@code HttpClientProperties}.
 *
 * @see HttpClientProperties
 */
@Configuration
public class HttpClientConfig
{


    private final HttpClientProperties httpClientProperties;

    public HttpClientConfig(HttpClientProperties httpClientProperties)
    {
        this.httpClientProperties = httpClientProperties;
    }


    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClientConnectionManager connectionManager)
    {
        var httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .evictIdleConnections(TimeValue.of(httpClientProperties.getEvictIdleConnections(), TimeUnit.SECONDS))
                .build();

        var requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(httpClientProperties.getConnectTimeout());
        requestFactory.setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimeout());

        return new BufferingClientHttpRequestFactory(requestFactory);
    }

    @Bean
    public HttpClientConnectionManager connectionManager()
    {
        var connectionConfig = ConnectionConfig.copy(ConnectionConfig.DEFAULT)
                .setTimeToLive(TimeValue.of(httpClientProperties.getTimeToLive(), TimeUnit.DAYS))
                .build();

        var socketConfig = SocketConfig.copy(SocketConfig.DEFAULT)
                .setSoTimeout(httpClientProperties.getReadTimeout(), TimeUnit.MILLISECONDS)
                .build();

        return PoolingHttpClientConnectionManagerBuilder.create()
                .setDefaultConnectionConfig(connectionConfig)
                .setDefaultSocketConfig(socketConfig)
                .build();
    }

}
