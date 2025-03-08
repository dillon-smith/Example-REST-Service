package com.branchapp.github.controller;

import com.branchapp.github.controller.examples.ErrorResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ProblemDetail;

import java.lang.annotation.*;


/**
 * An annotation for documenting API responses.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404",
                description = "Resource Not Found",
                content = @Content(examples = @ExampleObject(value = ErrorResponses.NOT_FOUND),
                        schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "429",
                description = "Too Many Requests",
                content = @Content(examples = @ExampleObject(value = ErrorResponses.TOO_MANY_REQUESTS),
                        schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500",
                description = "Internal Server Error",
                content = @Content(examples = @ExampleObject(value = ErrorResponses.INTERNAL_SERVER_ERROR),
                        schema = @Schema(implementation = ProblemDetail.class))),
})
public @interface DocumentedApiResponse
{
}
