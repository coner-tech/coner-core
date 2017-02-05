package org.coner.core.util.swagger;

public final class ApiResponseConstants {

    public static final class Created {
        public static final String MESSAGE = "Created";

        public static final class Headers {
            public static final String NAME = "Location";
            public static final String DESCRIPTION = "URI of created entity";

            private Headers() {
                // no-op
            }
        }

        private Created() {
            // no-op
        }
    }

    private ApiResponseConstants() {
        // no-op
    }
}
