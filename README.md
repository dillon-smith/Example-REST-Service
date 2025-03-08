# Example Spring Boot Service

This project is intended to be a very simple REST service showing examples of the following.

* A typical Spring Boot architecture with a `RestController` that delegates to a `Service` and other Spring Beans.
* Integrating and consolidating a pair of external API requests.
* Using Caffeine to cache the consolidated data.
* Response DTOs to insulate consumers from downstream contract and response changes.
* Separation of concerns between beans/components to simplify testing and feature additions.

# Running the Application

Requirements:
* Minimum JDK Version 21: https://openjdk.org/projects/jdk/21/
* Gradle 8

If this works you are in business:

    $ git clone git://github.com/dillon-smith/Example-REST-Service.git
    $ cd Example-REST-Service
    $ ./gradlew bootRun

> Note: When running via Gradle this application runs under port 8080    

The service implements an OpenAPI specification that is viewable at the following URIs.

```
http://localhost:8080
http://localhost:8080/swagger-ui/index.html
```

It has a single endpoint (/users/{userName}) that you can test with the following URI.

```
http://localhost:8080/users/octocat
```

The service should respond with a JSON document that resembles the following.

```JSON
{
  "user_name": "octocat",
  "display_name": "The Octocat",
  "avatar": "https://avatars.githubusercontent.com/u/583231?v=4",
  "geo_location": "San Francisco",
  "email": null,
  "url": "https://github.com/octocat",
  "created_at": "2011-01-25 18:44:36",
  "repos": [
    {
      "name": "boysenberry-repo-1",
      "url": "https://github.com/octocat/boysenberry-repo-1"
    },
    {
      "name": "git-consortium",
      "url": "https://github.com/octocat/git-consortium"
    },
    {
      "name": "hello-worId",
      "url": "https://github.com/octocat/hello-worId"
    },
    {
      "name": "Hello-World",
      "url": "https://github.com/octocat/Hello-World"
    },
    {
      "name": "linguist",
      "url": "https://github.com/octocat/linguist"
    },
    {
      "name": "octocat.github.io",
      "url": "https://github.com/octocat/octocat.github.io"
    },
    {
      "name": "Spoon-Knife",
      "url": "https://github.com/octocat/Spoon-Knife"
    },
    {
      "name": "test-repo1",
      "url": "https://github.com/octocat/test-repo1"
    }
  ]
}
```

# To-do

1) Investigate authenticating as a GitHub app or on behalf of the current user.
    1) https://docs.github.com/en/apps/creating-github-apps/authenticating-with-a-github-app/about-authentication-with-a-github-app
2) GitHubClient
    1) Add retry implementation
    2) Switch from `RestTemplate` to `RestClient` for a fluent API.
3) Generate base controller and DTOs from OpenAPI specification.
4) Generate GitHub client models from GitHub's API specification.
