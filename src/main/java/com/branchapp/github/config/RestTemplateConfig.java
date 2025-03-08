package com.branchapp.github.config;

import com.branchapp.github.component.LoggingInterceptor;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;

/**
 * Provides the configuration for a RestTemplate.
 */
@Configuration
public class RestTemplateConfig
{

    private final ClientHttpRequestFactory requestFactory;

    public RestTemplateConfig(ClientHttpRequestFactory requestyFactory)
    {
        this.requestFactory = requestyFactory;
    }

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer()
    {
        return restTemplate -> {
            var defaultUriBuilderFactory = new DefaultUriBuilderFactory();
            defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
            restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
            restTemplate.setRequestFactory(requestFactory);
            restTemplate.getInterceptors().addAll(restTemplateInterceptors());
        };
    }

    private List<ClientHttpRequestInterceptor> restTemplateInterceptors()
    {
        return List.of(new LoggingInterceptor());
    }
}
