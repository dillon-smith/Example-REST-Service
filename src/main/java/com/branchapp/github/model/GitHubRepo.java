package com.branchapp.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class GitHubRepo
{

    private String name;

    @JsonProperty("html_url")
    private String url;

    private GitHubRepo()
    {
    }

}