package com.branchapp.github.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * An alias annotation for {@link Operation} that also applies {@link DocumentedApiResponse}.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@DocumentedApiResponse
public @interface GitHubApiOperation
{
    @AliasFor(annotation = Operation.class, attribute = "summary")
    String summary() default "";

    @AliasFor(annotation = Operation.class, attribute = "description")
    String description() default "";

    @AliasFor(annotation = Operation.class, attribute = "tags")
    String tags() default "";
}
