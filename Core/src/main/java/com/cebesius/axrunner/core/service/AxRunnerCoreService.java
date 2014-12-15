package com.cebesius.axrunner.core.service;

import com.cebesius.axrunner.core.exception.NotFoundException;
import com.cebesius.axrunner.core.model.gateway.EventGateway;
import com.cebesius.axrunner.core.model.gateway.RegistrationGateway;
import com.cebesius.axrunner.core.model.gateway.ResultsGateway;
import com.cebesius.axrunner.core.model.domain.Event;
import com.cebesius.axrunner.core.model.request.AddEventRequest;
import com.cebesius.axrunner.core.model.request.AddRegistrationRequest;
import com.cebesius.axrunner.core.model.response.*;

/**
 *
 */
public class AxRunnerCoreService {

    private final EventGateway eventGateway;
    private final RegistrationGateway registrationGateway;
    private final ResultsGateway resultsGateway;

    public AxRunnerCoreService(EventGateway eventGateway, RegistrationGateway registrationGateway, ResultsGateway resultsGateway) {
        this.eventGateway = eventGateway;
        this.registrationGateway = registrationGateway;
        this.resultsGateway = resultsGateway;
    }

    public GetEventsResponse getEvents() {
        return new GetEventsResponse(eventGateway.getAll());
    }

    public GetRegistrationsResponse getRegistrations(String eventId) {
        Event event = eventGateway.findById(eventId);
        if (event == null) {
            throwEventNotFoundException(eventId);
        }
        return new GetRegistrationsResponse(registrationGateway.getAllWithEvent(event));
    }

    public GetResultsRawResponse getResultsRaw(String eventId) {
        Event event = eventGateway.findById(eventId);
        if (event == null) {
            throwEventNotFoundException(eventId);
        }
        return new GetResultsRawResponse();
    }

    public GetResultsPaxResponse getResultsPax(String eventId) {
        Event event = eventGateway.findById(eventId);
        if (event == null) {
            throwEventNotFoundException(eventId);
        }
        return new GetResultsPaxResponse();
    }

    public GetResultsClassResponse getResultsClass(String eventId) {
        Event event = eventGateway.findById(eventId);
        if (event == null) {
            throwEventNotFoundException(eventId);
        }
        return new GetResultsClassResponse();
    }

    public AddEventResponse addEvent(AddEventRequest request) {
        return new AddEventResponse(eventGateway.create(request.getName()));
    }

    public AddRegistrationResponse addRegistrationRequest(AddRegistrationRequest addRegistrationRequest) {
        Event event = eventGateway.findById(addRegistrationRequest.getEventId());
        if (event == null) {
            throwEventNotFoundException(addRegistrationRequest.getEventId());
        }
        return new AddRegistrationResponse(registrationGateway.create(
                event,
                addRegistrationRequest.getFirstName(),
                addRegistrationRequest.getLastName()
        ));
    }

    private void throwEventNotFoundException(String eventId) {
        throw new NotFoundException("Event not found: " + eventId);
    }

}
