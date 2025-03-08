package com.branchapp.github.service;

import com.branchapp.github.component.GitHubUserMapper;
import com.branchapp.github.model.GitHubRepo;
import com.branchapp.github.model.GitHubUser;
import com.branchapp.github.rest.client.GitHubClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;

import static com.branchapp.github.TestSamples.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GithubServiceTest
{

    private final String userName = "octocat";
    private GithubService service;
    private GitHubClient client;
    private GitHubUserMapper mapper;

    @BeforeEach
    public void setup()
    {
        client = Mockito.mock(GitHubClient.class);
        mapper = Mockito.mock(GitHubUserMapper.class);
        service = new GithubService(client, mapper);
    }

    @Test
    public void verifySuccess()
    {
        mockCompleteSuccess();
        assertEquals(gitHubUserDto(), service.getGitHubUser(userName));

        verify(client, times(1)).getUser(userName);
        verify(client, times(1)).getUserRepos(userName);
        verify(mapper, times(1)).mapDto(gitHubUser(), gitHubRepos());
    }

    private void mockCompleteSuccess()
    {
        mockUsersSuccess();
        mockReposSuccess();
    }

    private void mockUsersSuccess()
    {
        when(client.getUser(userName)).thenReturn(ResponseEntity.ok(gitHubUser()));
        when(mapper.mapDto(gitHubUser(), gitHubRepos())).thenReturn(gitHubUserDto());
    }

    private void mockReposSuccess()
    {
        when(client.getUserRepos(userName)).thenReturn(ResponseEntity.ok(gitHubRepos()));
    }

    @ParameterizedTest
    @MethodSource({"com.branchapp.github.TestSamples#clientErrors", "com.branchapp.github.TestSamples#serverErrors"})
    public void verifyUserFail(HttpStatus status)
    {
        mockUserFail(status);

        var exception = assertThrows(ErrorResponseException.class, () -> service.getGitHubUser(userName));
        assertEquals(status.value(), exception.getStatusCode().value());

        verify(client, times(1)).getUser(userName);
        verify(client, never()).getUserRepos(userName);
        verify(mapper, never()).mapDto(gitHubUser(), gitHubRepos());
    }

    private void mockUserFail(HttpStatus status)
    {
        when(client.getUser(userName)).thenReturn(new ResponseEntity<>(status));
    }

    @ParameterizedTest
    @MethodSource({"com.branchapp.github.TestSamples#clientErrors", "com.branchapp.github.TestSamples#serverErrors"})
    public void verifyRepoFail(HttpStatus status)
    {
        mockUsersSuccess();
        mockReposFail(status);

        var exception = assertThrows(ErrorResponseException.class, () -> service.getGitHubUser(userName));
        assertEquals(status.value(), exception.getStatusCode().value());

        verify(client, times(1)).getUser(userName);
        verify(client, times(1)).getUserRepos(userName);
        verify(mapper, never()).mapDto(gitHubUser(), gitHubRepos());
    }

    private void mockReposFail(HttpStatus status)
    {
        when(client.getUserRepos(userName)).thenReturn(new ResponseEntity<>(status));
    }

    @ParameterizedTest
    @MethodSource("com.branchapp.github.TestSamples#malformedResponseEntities")
    @NullSource
    public void verifyUsersFailsOnMalformedResponse(ResponseEntity<GitHubUser> entity)
    {
        mockUsersMalformedEntity(entity);
        var exception = assertThrows(ErrorResponseException.class, () -> service.getGitHubUser(userName));
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());

        verify(client, times(1)).getUser(userName);
        verify(client, never()).getUserRepos(userName);
        verify(mapper, never()).mapDto(gitHubUser(), gitHubRepos());
    }

    private void mockUsersMalformedEntity(ResponseEntity malformedEntity)
    {
        when(client.getUser(userName)).thenReturn(malformedEntity);
    }

    @ParameterizedTest
    @MethodSource("com.branchapp.github.TestSamples#malformedResponseEntities")
    @NullSource
    public void verifyReposFailsOnMalformedResponse(ResponseEntity<GitHubRepo> entity)
    {
        mockUsersSuccess();
        mockReposMalformedEntity(entity);
        var exception = assertThrows(ErrorResponseException.class, () -> service.getGitHubUser(userName));
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());

        verify(client, times(1)).getUser(userName);
        verify(client, times(1)).getUserRepos(userName);
        verify(mapper, never()).mapDto(gitHubUser(), gitHubRepos());
    }

    private void mockReposMalformedEntity(ResponseEntity malformedEntity)
    {
        when(client.getUserRepos(userName)).thenReturn(malformedEntity);
    }

}