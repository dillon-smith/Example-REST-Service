package com.branchapp.github.rest.client;

import com.branchapp.github.model.GitHubRepo;
import com.branchapp.github.model.GitHubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.web.ErrorResponseException;

import java.io.IOException;
import java.util.List;

import static com.branchapp.github.TestSamples.gitHubRepo;
import static com.branchapp.github.TestSamples.gitHubUser;
import static com.branchapp.github.TestUtils.asString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(GitHubClient.class)
class GitHubClientTest
{
    private static final String userName = "test-user";

    private final RequestMatcher usersRequest = requestToUriTemplate("/api/v1/testing/users/{userName}", userName);
    private final RequestMatcher reposRequest = requestToUriTemplate("/api/v1/testing/users/{userName}/repos", userName);

    @Value("classpath:github-octocat/octocat.json")
    private Resource octocatJson;

    @Value("classpath:github-octocat/octocat-repos-single-entry.json")
    private Resource octocatReposJson;

    private GitHubClient client;
    private MockRestServiceServer server;

    public GitHubClientTest(@Autowired GitHubClient client, @Autowired MockRestServiceServer server)
    {
        this.client = client;
        this.server = server;
        ReflectionTestUtils.setField(client, "usersUrl", "/api/v1/testing/users/{userName}");
        ReflectionTestUtils.setField(client, "reposUrl", "/api/v1/testing/users/{userName}/repos");
    }

    @BeforeEach
    public void setup()
    {
        server.reset();
    }

    @Test
    public void verifyUsersExpectedApiCall()
    {
        mockUsersCall();

        ResponseEntity<GitHubUser> user = client.getUser(userName);
        assertEquals(HttpStatus.OK, user.getStatusCode());
        assertEquals(gitHubUser(), user.getBody());
    }

    private void mockUsersCall()
    {
        mockHttpGetServerCall(usersRequest, octocatJson);
    }

    private void mockHttpGetServerCall(RequestMatcher uriTemplateMatcher, Resource body)
    {
        server.expect(ExpectedCount.once(), uriTemplateMatcher)
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(asString(body)));
    }

    @Test
    public void verifyUserReposExpectedApiCall()
    {
        mockReposCall();

        ResponseEntity<List<GitHubRepo>> repos = client.getUserRepos(userName);
        assertEquals(HttpStatus.OK, repos.getStatusCode());
        assertEquals(List.of(gitHubRepo()), repos.getBody());
    }

    private void mockReposCall()
    {
        mockHttpGetServerCall(reposRequest, octocatReposJson);
    }

    @ParameterizedTest
    @MethodSource({"com.branchapp.github.TestSamples#clientErrors", "com.branchapp.github.TestSamples#serverErrors"})
    public void verifyUsersConvertedHttpStatusCodeExceptionToEntity(HttpStatus status)
    {
        mockFailedHttpGetServerCall(status, usersRequest);
        ResponseEntity<GitHubUser> repos = client.getUser(userName);
        assertEquals(status, repos.getStatusCode());
    }

    private void mockFailedHttpGetServerCall(HttpStatus status, RequestMatcher uriTemplateMatcher)
    {
        server.expect(ExpectedCount.once(), uriTemplateMatcher)
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(status));
    }

    @ParameterizedTest
    @MethodSource({"com.branchapp.github.TestSamples#clientErrors", "com.branchapp.github.TestSamples#serverErrors"})
    public void verifyReposConvertedHttpStatusCodeExceptionToEntity(HttpStatus status)
    {
        mockFailedHttpGetServerCall(status, reposRequest);
        ResponseEntity<List<GitHubRepo>> repos = client.getUserRepos(userName);
        assertEquals(status, repos.getStatusCode());
    }

    @Test
    public void verifyUsersConvertedNonHttpStatusCodeExceptionsToErrorResponseException()
    {
        server.expect(ExpectedCount.once(), usersRequest)
                .andExpect(method(HttpMethod.GET))
                .andRespond(withException(new IOException("Test Internal Failure During Rest Template Call")));

        var ex = assertThrows(ErrorResponseException.class, () -> client.getUser(userName));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getStatusCode().value());
    }

    @Test
    public void verifyReposConvertedNonHttpStatusCodeExceptionsToErrorResponseException()
    {
        server.expect(ExpectedCount.once(), reposRequest)
                .andExpect(method(HttpMethod.GET))
                .andRespond(withException(new IOException("Test Internal Failure During Rest Template Call")));

        var ex = assertThrows(ErrorResponseException.class, () -> client.getUserRepos(userName));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getStatusCode().value());
    }

}