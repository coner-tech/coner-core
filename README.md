# Coner

Coner is an autocross event operations system.

## Development Status

Coner is in a very early stage of development. It currently does more nothing than anything.

![Travis CI Build Status](https://travis-ci.org/carltonwhitehead/coner.svg?branch=master)

## Build, Test, and Run

These steps assume you've cloned this repo, and have Maven and the Java 8 runtime installed already.

1. `mvn clean install`
2. `java -jar dropwizard-app/target/dropwizard-app-0.1-SNAPSHOT.jar server dropwizard-app/src/test/resources/config/test.yml`

The server should start up and spit out many lines of logging. It's probably worth taking a few minutes to look through those logs to get an idea of the structure of the app. The last few lines should resemble the following:

```
INFO  [2014-12-31 17:13:40,897] org.eclipse.jetty.server.ServerConnector: Started application@7942a854{HTTP/1.1}{0.0.0.0:8080}
INFO  [2014-12-31 17:13:40,897] org.eclipse.jetty.server.ServerConnector: Started admin@53b8afea{HTTP/1.1}{0.0.0.0:8081}
INFO  [2014-12-31 17:13:40,897] org.eclipse.jetty.server.Server: Started @1990ms
```

Now you should be able to make some simple requests.

```
$ curl http://localhost:8080/events
{"events":[]}
$ curl http://localhost:8081/healthcheck
{"deadlocks":{"healthy":true},"hibernate":{"healthy":true}}
```

Accessing API (not completed): http://localhost:8080/swagger

## Contributing

Interested in contributing to Coner? Please take a look at our [contributing guidlines](https://github.com/carltonwhitehead/coner/blob/master/CONTRIBUTING.md)
