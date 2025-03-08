package com.branchapp.github;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

public class TestUtils
{
    /**
     * Reads the resource as a String, capturing any IO Exceptions as Unchecked IO Exceptions.
     *
     * @param resource the resource to read
     * @return the resource as a String
     * @throws UncheckedIOException if an IO error occurs
     */
    public static String asString(Resource resource) throws UncheckedIOException
    {
        try {
            return resource.getContentAsString(Charset.defaultCharset());
        } catch (IOException ex) {
            throw new UncheckedIOException(String.format("Failed to read Resource '%s' as a String",
                    resource.getFilename()), ex);
        }
    }

}
