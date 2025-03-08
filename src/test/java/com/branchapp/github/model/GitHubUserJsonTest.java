package com.branchapp.github.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static com.branchapp.github.TestSamples.gitHubUser;
import static com.branchapp.github.TestUtils.asString;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GitHubUserJsonTest
{

    @Autowired
    private JacksonTester<GitHubUser> tester;

    @Value("classpath:github-octocat/octocat.json")
    private Resource octocatJson;

    @Value("classpath:github-octocat/octocat-partial.json")
    private Resource octocatPartial;

    @Test
    public void verifyDeserializeWithUnknownFields() throws IOException
    {
        assertThat(tester.parse(asString(octocatJson))).usingRecursiveComparison().isEqualTo(gitHubUser());
    }

    @Test
    public void verifyDeserialize() throws IOException
    {
        assertThat(tester.parse(asString(octocatJson))).usingRecursiveComparison().isEqualTo(gitHubUser());
    }

    @Test
    public void verifySerialize() throws IOException
    {
        assertThat(tester.write(gitHubUser())).isEqualToJson(asString(octocatPartial));
    }

}