package org.coner.core.exception;

/**
 * Indicates something does not belong to its corresponding Event.
 */
public class EventMismatchException extends Exception {

    public EventMismatchException() {
        super("The entity does not belong to the corresponding Event");
    }
}
