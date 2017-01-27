package org.coner.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Jackson related utility class.
 */
public final class JacksonUtil {
    /**
     * Private default constructor due to only static/utility methods.
     */
    private JacksonUtil() {
    }

    /**
     * Configure jackson databind object mapper.
     *
     * @param objectMapper mapper to be configure
     */
    public static void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
