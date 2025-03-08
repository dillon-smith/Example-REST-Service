package com.branchapp.github.controller;

import com.branchapp.github.TestSamples;
import com.branchapp.github.service.GithubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.ErrorResponseException;

import static com.branchapp.github.TestUtils.asString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GitHubController.class)
class GitHubControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GithubService service;

    @Value("classpath:branchapp-octocat/octocat.json")
    private Resource branchappOctocat;


    @Test
    public void verifySuccessHasExpectedJson() throws Exception
    {
        when(service.getGitHubUser("octocat")).thenReturn(TestSamples.gitHubUserDto());
        mockMvc.perform(get("/api/v1/users/{userName}", "octocat"))
                .andExpect(status().isOk())
                .andExpect(content().json(asString(branchappOctocat)));
    }

    @ParameterizedTest
    @MethodSource({"com.branchapp.github.TestSamples#clientErrors", "com.branchapp.github.TestSamples#serverErrors"})
    public void verifyErrorResponseHasExpectedJson(HttpStatus status) throws Exception
    {
        when(service.getGitHubUser("octocat")).thenThrow(new ErrorResponseException(status));
        mockMvc.perform(get("/api/v1/users/{userName}", "octocat"))
                .andExpect(status().is(status.value()))
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.status").value(status.value()))
                .andExpect(jsonPath("$.instance").value("/api/v1/users/octocat"));
    }

    @Test
    public void verifyControllerAdviceHandlesAllOther() throws Exception
    {
        when(service.getGitHubUser("octocat")).thenThrow(new RuntimeException("Test Exception"));
        mockMvc.perform(get("/api/v1/users/{userName}", "octocat"))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.instance").value("/api/v1/users/octocat"));
    }

}