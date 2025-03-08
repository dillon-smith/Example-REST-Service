package com.branchapp.github.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static com.branchapp.github.TestSamples.gitHubUserDto;
import static com.branchapp.github.TestUtils.asString;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GitHubUserDtoJsonTest
{

    @Autowired
    private JacksonTester<GitHubUserDto> tester;

    @Value("classpath:branchapp-octocat/octocat.json")
    private Resource branchappOctocat;

    @Test
    public void verifySerialize() throws IOException
    {
        assertThat(tester.write(gitHubUserDto())).isEqualToJson(asString(branchappOctocat));
    }

}