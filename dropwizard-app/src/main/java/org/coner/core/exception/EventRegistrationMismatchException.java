package org.coner.core.exception;

/**
 * Indicates the Registration does not belong to the Event.
 */
public class EventRegistrationMismatchException extends Exception {

    /**
     * Default constructor for EventRegistrationMismatchException.
     */
    public EventRegistrationMismatchException() {
        super("The Registration does not belong to the Event");
    }
}
