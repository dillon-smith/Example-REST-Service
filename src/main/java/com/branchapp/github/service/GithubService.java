package com.branchapp.github.service;

import com.branchapp.github.component.GitHubUserMapper;
import com.branchapp.github.dto.GitHubUserDto;
import com.branchapp.github.model.GitHubRepo;
import com.branchapp.github.model.GitHubUser;
import com.branchapp.github.rest.client.GitHubClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.util.List;
import java.util.function.Function;

import static java.util.Objects.isNull;

/**
 * This service invokes two GitHub endpoints and combines the resulting responses. An attempt will be made to cache the
 * combined result via a Caffeine instance (named gitCache). This implementation of the service is intended to fail
 * quickly, any non-success or empty response received from the client will be immediately thrown as a
 * {@link ErrorResponseException}.
 *
 * @see GitHubClient
 * @see GitHubUserMapper
 */
@Slf4j
@Service
public class GithubService
{

    private final GitHubClient client;
    private final GitHubUserMapper mapper;

    public GithubService(GitHubClient client,
                         GitHubUserMapper mapper)
    {
        this.client = client;
        this.mapper = mapper;
    }

    /**
     * Gets GitHub user and repository information corresponding to the given username.
     *
     * @param userName the username whose user and repository information is desired
     * @return the resulting data
     */
    @Cacheable("gitCache")
    public GitHubUserDto getGitHubUser(String userName) throws ErrorResponseException
    {
        GitHubUser user = invokeClient(client::getUser, userName);
        List<GitHubRepo> repos = invokeClient(client::getUserRepos, userName);
        return mapper.mapDto(user, repos);
    }

    private <T> T invokeClient(Function<String, ResponseEntity<T>> apiCall, String userName)
    {
        ResponseEntity<T> resp = apiCall.apply(userName);
        if (isNull(resp)) {
            throw notFound();
        }

        if (isErrorResponse(resp)) {
            throw new ErrorResponseException((resp.getStatusCode()));
        }

        if (isNull(resp.getBody())) {
            throw notFound();
        }

        return resp.getBody();
    }

    private static ErrorResponseException notFound()
    {
        return new ErrorResponseException(HttpStatus.NOT_FOUND);
    }

    private static boolean isErrorResponse(ResponseEntity<?> entity)
    {
        return entity.getStatusCode().isError();
    }
}
