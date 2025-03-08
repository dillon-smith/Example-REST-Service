package com.branchapp.github.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(name = "GitHubRepo", description = "A schema describing a GitHub repository.")
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class GitHubRepoDto
{

    @Schema(description = "The repository's name.", example = "octocat")
    private String name;

    @Schema(description = "The repository's URL.", example = "https://github.com/octocat/boysenberry-repo-1")
    private String url;

}
