package com.branchapp.github.rest.client;

import com.branchapp.github.model.GitHubRepo;
import com.branchapp.github.model.GitHubUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * A client for invoking GitHub APIs.
 */
@Component
@Slf4j
public class GitHubClient
{
    private static final ParameterizedTypeReference<GitHubUser> GITHUB_USER_TYPE_REF =
            new ParameterizedTypeReference<GitHubUser>()
            {
            };

    private static final ParameterizedTypeReference<List<GitHubRepo>> GITHUB_REPO_LIST_TYPE_REF =
            new ParameterizedTypeReference<List<GitHubRepo>>()
            {
            };


    private final RestTemplate restTemplate;
    private String usersUrl;
    private String reposUrl;

    public GitHubClient(RestTemplateBuilder builder,
                        @Value("${github.api.users_url}")
                        String usersUrl,
                        @Value("${github.api.users.repos_url}")
                        String reposUrl)
    {
        this.restTemplate = builder.build();
        this.usersUrl = usersUrl;
        this.reposUrl = reposUrl;
    }

    /**
     * Gets the GitHub user by their username (referred to as GitHub Handle by GitHub's API docs).
     *
     * @param userName the username/handle
     * @return the response from the GitHub API. Note: the ResponseEntity is not guaranteed to contain a success
     *         response. If the ResponseEntity represents an error, the body will be null.
     * @throws ErrorResponseException if the API call has failed but the failure was not mapped to an HTTP Status.
     */
    public ResponseEntity<GitHubUser> getUser(String userName) throws ErrorResponseException
    {
        log.info("Requesting user information for {}", userName);
        return getByUserName(usersUrl, userName, GITHUB_USER_TYPE_REF);
    }

    private <T> ResponseEntity<T> getByUserName(String uri, String userName,
                                                ParameterizedTypeReference<T> returnType) throws ErrorResponseException
    {
        try {
            return restTemplate.exchange(uri,
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    returnType,
                    Map.of("userName", userName));
        } catch (HttpStatusCodeException ex) {
            return responseEntityFromException(ex, returnType);
        } catch (Exception ex) {
            throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private <T> ResponseEntity<T> responseEntityFromException(HttpStatusCodeException ex,
                                                              ParameterizedTypeReference<T> returnType)
    {
        return new ResponseEntity<>(null, ex.getResponseHeaders(), ex.getStatusCode());
    }

    /**
     * Gets the GitHub user's repos.
     *
     * @param userName the username/handle
     * @return the response from the GitHub API. Note: the ResponseEntity is not guaranteed to contain a success *
     *         response. If the ResponseEntity represents an error, the body will be null.
     * @throws ErrorResponseException if the API call has failed but the failure was not mapped to an HTTP Status.
     */
    public ResponseEntity<List<GitHubRepo>> getUserRepos(String userName) throws ErrorResponseException
    {
        log.info("Requesting repos for {}", userName);
        return getByUserName(reposUrl, userName, GITHUB_REPO_LIST_TYPE_REF);
    }
}
