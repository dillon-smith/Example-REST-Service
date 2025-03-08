package com.branchapp.github.component;

import com.branchapp.github.dto.GitHubRepoDto;
import com.branchapp.github.dto.GitHubUserDto;
import com.branchapp.github.model.GitHubRepo;
import com.branchapp.github.model.GitHubUser;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A component that maps {@code GitHubUser} and @code GitHubRepo} to their respective DTOs {@code GitHubUserDto} and
 * {@code GitHubRepoDto}.
 */
@Component
public class GitHubUserMapper
{

    public GitHubUserDto mapDto(GitHubUser gitHubUser, List<GitHubRepo> gitHubRepos)
    {
        return GitHubUserDto.builder()
                .userName(gitHubUser.getLogin())
                .displayName(gitHubUser.getName())
                .avatar(gitHubUser.getAvatarUrl())
                .geoLocation(gitHubUser.getLocation())
                .email(gitHubUser.getEmail())
                .url(gitHubUser.getHtmlUrl())
                .createdAt(gitHubUser.getCreatedAt())
                .repos(mapRepos(gitHubRepos))
                .build();
    }

    public List<GitHubRepoDto> mapRepos(List<GitHubRepo> gitHubRepos)
    {
        return gitHubRepos.stream()
                .map(this::mapRepo)
                .toList();
    }

    public GitHubRepoDto mapRepo(GitHubRepo gitHubRepo)
    {
        return new GitHubRepoDto(gitHubRepo.getName(), gitHubRepo.getUrl());
    }

}
