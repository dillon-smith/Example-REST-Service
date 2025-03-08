package com.branchapp.github.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(name = "GitHubUser",
        description = "A schema describing a GitHub user's information and any repositories they are associated with.")
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Builder
public class GitHubUserDto
{

    @Schema(description = "The user's GitHub handle.", example = "octocat")
    @JsonProperty(value = "user_name")
    private String userName;

    @Schema(description = "The user's displayed name.", example = "monalisa octocat")
    @JsonProperty(value = "display_name")
    private String displayName;

    @Schema(description = "The user's avatar URL.", example = "https://github.com/images/error/octocat_happy.gif")
    private String avatar;

    @Schema(description = "The user's geolocation.", example = "San Francisco")
    @JsonProperty(value = "geo_location")
    private String geoLocation;

    @Schema(description = "The user's publicly visible email address.", example = "octocat@github.com")
    private String email;

    @Schema(description = "The user's profile URL.", example = "https://github.com/octocat")
    private String url;

    @Schema(description = "The user's sign-up date and time.", example = "2011-01-25 18:44:36")
    @JsonProperty(value = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime createdAt;

    @Schema(description = "A listing of any repos this user is associated with.")
    private List<GitHubRepoDto> repos;

}
