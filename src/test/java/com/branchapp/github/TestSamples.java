package com.branchapp.github;

import com.branchapp.github.dto.GitHubRepoDto;
import com.branchapp.github.dto.GitHubUserDto;
import com.branchapp.github.model.GitHubRepo;
import com.branchapp.github.model.GitHubUser;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * A class with static accessors corresponding to various models and DTOs within the application.
 */
public class TestSamples
{

    public static GitHubRepoDto gitHubRepoDto()
    {
        return new GitHubRepoDto("boysenberry-repo-1",
                "https://github.com/octocat/boysenberry-repo-1");
    }

    public static GitHubUserDto gitHubUserDto()
    {
        return new GitHubUserDto("octocat",
                "The Octocat",
                "https://avatars.githubusercontent.com/u/583231?v=4",
                "San Francisco",
                null,
                "https://github.com/octocat",
                OffsetDateTime.parse("2011-01-25T18:44:36Z"),
                gitHubRepoDtos());
    }

    public static List<GitHubRepoDto> gitHubRepoDtos()
    {
        return List.of(
                new GitHubRepoDto("boysenberry-repo-1", "https://github.com/octocat/boysenberry-repo-1"),
                new GitHubRepoDto("git-consortium", "https://github.com/octocat/git-consortium")
        );
    }

    public static GitHubRepo gitHubRepo()
    {
        return new GitHubRepo("boysenberry-repo-1",
                "https://github.com/octocat/boysenberry-repo-1");
    }

    public static ResponseEntity<GitHubUser> gitHubUserResponseEntity()
    {
        return new ResponseEntity<>(gitHubUser(), HttpStatus.OK);
    }

    public static GitHubUser gitHubUser()
    {
        return new GitHubUser("octocat",
                "The Octocat",
                "https://avatars.githubusercontent.com/u/583231?v=4",
                "San Francisco",
                null,
                "https://github.com/octocat",
                OffsetDateTime.parse("2011-01-25T18:44:36Z"));
    }

    public static ResponseEntity<List<GitHubRepo>> gitHubRepoResponseEntity()
    {
        return new ResponseEntity<>(gitHubRepos(), HttpStatus.OK);
    }

    public static List<GitHubRepo> gitHubRepos()
    {
        return List.of(
                new GitHubRepo("boysenberry-repo-1", "https://github.com/octocat/boysenberry-repo-1"),
                new GitHubRepo("git-consortium", "https://github.com/octocat/git-consortium")
        );
    }

    public static <T> ResponseEntity<T> successResponseEntityNullBody()
    {
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }

    public static Stream<Arguments> clientErrors()
    {
        return Arrays.stream(HttpStatus.values()).filter(HttpStatus::is4xxClientError).map(Arguments::of);
    }

    public static Stream<Arguments> serverErrors()
    {
        return Arrays.stream(HttpStatus.values()).filter(HttpStatus::is5xxServerError).map(Arguments::of);
    }

    public static Stream<Arguments> malformedResponseEntities()
    {
        return Stream.of(Arguments.of(ResponseEntity.ok(null)));
    }

}
