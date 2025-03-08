package com.branchapp.github.controller;

import com.branchapp.github.dto.GitHubUserDto;
import com.branchapp.github.service.GithubService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
public class GitHubController
{

    private final GithubService service;

    public GitHubController(GithubService service)
    {
        this.service = service;
    }

    @GitHubApiOperation(summary = "Get a GitHub User", tags = "GitHub",
            description = "Get a GitHub User's publicly available information and repositories.")
    @GetMapping(path = "/api/v1/users/{userName}", produces = "application/json")
    public ResponseEntity<GitHubUserDto> getGithubUser(@PathVariable("userName")
                                                       @Parameter(required = true,
                                                               description = "The handle for the GitHub user account",
                                                               example = "octocat")
                                                       @NotEmpty
                                                       String userName)
    {
        log.info("Retrieving GitHub data for GitHub handle '{}'", userName);
        return ok(service.getGitHubUser(userName));
    }

}
