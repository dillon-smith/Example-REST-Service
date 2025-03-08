package com.branchapp.github.controller.examples;

/**
 * This class contains basic examples for error response bodies.
 */
public class ErrorResponses
{

    public static final String NOT_FOUND = """
            {
              "type": "about:blank",
              "title": "Not Found",
              "status": 404,
              "instance": "/api/v1/users/octocat"
            }
            """;

    public static final String TOO_MANY_REQUESTS = """
            {
              "type": "about:blank",
              "title": "Too Many Requests",
              "status": 429,
              "instance": "/api/v1/users/octocat"
            }
            """;

    public static final String INTERNAL_SERVER_ERROR = """
            {
              "type": "about:blank",
              "title": "Internal Server Error",
              "status": 500,
              "instance": "/api/v1/users/octocat"
            }
            """;
}
