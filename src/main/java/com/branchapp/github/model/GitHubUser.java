package com.branchapp.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class GitHubUser
{

    private String login;
    private String name;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String location;
    private String email;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    private GitHubUser()
    {
    }

}
