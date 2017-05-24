package org.coner.core.util;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public final class UnitTestUtils {

    private UnitTestUtils() {
    }

    public static String getEntityIdFromResponse(Response response) {
        String location = (String) response.getHeaders().get("Location").get(0);
        return StringUtils.substringAfterLast(location, "/");
    }

}
