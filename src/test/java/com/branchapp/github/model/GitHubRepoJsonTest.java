package com.branchapp.github.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static com.branchapp.github.TestSamples.gitHubRepo;
import static com.branchapp.github.TestUtils.asString;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GitHubRepoJsonTest
{

    @Autowired
    private JacksonTester<GitHubRepo> tester;

    @Value("classpath:github-octocat/octocat-repo.json")
    private Resource octocatRepo;

    @Value("classpath:github-octocat/octocat-repo-partial.json")
    private Resource octocatRepoPartial;

    @Test
    public void verifyDeserializeWithUnknownFields() throws IOException
    {
        assertThat(tester.parse(asString(octocatRepo))).usingRecursiveComparison().isEqualTo(gitHubRepo());
    }

    @Test
    public void verifyDeserialize() throws IOException
    {
        assertThat(tester.parse(asString(octocatRepoPartial))).usingRecursiveComparison().isEqualTo(gitHubRepo());
    }

    @Test
    public void verifySerialize() throws IOException
    {
        assertThat(tester.write(gitHubRepo())).isEqualToJson(asString(octocatRepoPartial));
    }

}
