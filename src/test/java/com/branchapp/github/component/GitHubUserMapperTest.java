package com.branchapp.github.component;

import org.junit.jupiter.api.Test;

import static com.branchapp.github.TestSamples.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GitHubUserMapperTest
{

    private final GitHubUserMapper mapper = new GitHubUserMapper();

    @Test
    public void verifyMapDto()
    {
        assertEquals(gitHubUserDto(), mapper.mapDto(gitHubUser(), gitHubRepos()));
    }

    @Test
    public void verifyMapRepos()
    {
        assertEquals(gitHubRepoDtos(), mapper.mapRepos(gitHubRepos()));
    }

    @Test
    public void verifyMapRepo()
    {
        assertEquals(gitHubRepoDto(), mapper.mapRepo(gitHubRepo()));
    }

}