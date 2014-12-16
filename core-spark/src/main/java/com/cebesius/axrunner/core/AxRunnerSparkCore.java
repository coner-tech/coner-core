package com.cebesius.axrunner.core;

import com.cebesius.axrunner.core.exception.NotFoundException;
import com.cebesius.axrunner.core.model.gateway.EventGateway;
import com.cebesius.axrunner.core.model.gateway.RegistrationGateway;
import com.cebesius.axrunner.core.model.gateway.ResultsGateway;
import com.cebesius.axrunner.core.model.request.AddEventRequest;
import com.cebesius.axrunner.core.model.request.AddRegistrationRequest;
import com.cebesius.axrunner.core.service.AxRunnerCoreService;
import com.cebesius.axrunner.core.util.AcceptType;
import com.cebesius.axrunner.core.util.JsonResponseTransformer;
import com.google.gson.Gson;
import org.eclipse.jetty.http.HttpStatus;
import spark.Session;

import static spark.Spark.*;

/**
 *
 */
public class AxRunnerSparkCore {

    private static final String ATTRIBUTE_ROLE_STAFF = "staff";

    public static void main(String[] args) {
        EventGateway eventGateway = new EventGateway();
        RegistrationGateway registrationGateway = new RegistrationGateway();
        ResultsGateway resultsGateway = new ResultsGateway();
        AxRunnerCoreService service = new AxRunnerCoreService(eventGateway, registrationGateway, resultsGateway);
        Gson gson = new Gson();
        JsonResponseTransformer jsonResponseTransformer = new JsonResponseTransformer(gson);

        // public API
        get("/public/events", AcceptType.JSON, (req, res) -> service.getEvents(), jsonResponseTransformer);
        get("/public/events/:eventId/registrations", AcceptType.JSON, (req, res) -> service.getRegistrations(req.params(":eventId")), jsonResponseTransformer);
        get("/public/events/:eventId/results/raw", AcceptType.JSON, (req, res) -> service.getResultsRaw(req.params(":eventId")), jsonResponseTransformer);
        get("/public/events/:eventId/results/pax", AcceptType.JSON, (req, res) -> service.getResultsPax(req.params(":eventId")), jsonResponseTransformer);
        get("/public/events/:eventId/results/class", AcceptType.JSON, (req, res) -> service.getResultsClass(req.params(":eventId")), jsonResponseTransformer);

        // staff API
        before("/staff/*", (req, res) -> {
            Session session = req.session(true);
            if (!Boolean.TRUE.equals(session.attribute(ATTRIBUTE_ROLE_STAFF))) {
                halt(HttpStatus.UNAUTHORIZED_401);
            }
        });
        post("/staff/events", AcceptType.JSON, (req, res) -> {
            AddEventRequest addEventRequest = gson.fromJson(req.body(), AddEventRequest.class);
            return service.addEvent(addEventRequest);
        }, jsonResponseTransformer);
        post("/staff/events/:eventId/registrations", AcceptType.JSON, (req, res) -> {
            AddRegistrationRequest addRegistrationRequest = gson.fromJson(req.body(), AddRegistrationRequest.class);
            addRegistrationRequest.setEventId(req.params(":eventId"));
            return service.addRegistrationRequest(addRegistrationRequest);
        }, jsonResponseTransformer);

        // secure API
        before("/secure/*", (req, res) -> {
            if (!req.ip().startsWith("127.")) {
                halt(HttpStatus.UNAUTHORIZED_401);
            }
        });
        post("/secure/roles/staff", AcceptType.JSON, (req, res) -> {
            Session session = req.session(true);
            session.attribute(ATTRIBUTE_ROLE_STAFF, Boolean.TRUE);
            return null;
        }, jsonResponseTransformer);

        // general filters, exceptions, etc
        after("/*", AcceptType.JSON, (request, response) -> {
            response.type(AcceptType.JSON);
        });
        exception(NotFoundException.class, (e, req, res) -> {
            e.printStackTrace();
            res.status(HttpStatus.NOT_FOUND_404);
        });
    }
}
