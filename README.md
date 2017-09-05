# Coner Core

Coner Core is a REST API service for core autocross event operations.

![Travis CI Build Status](https://travis-ci.org/caeos/coner-core.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/caeos/coner-core/badge.svg?branch=master)](https://coveralls.io/github/caeos/coner-core?branch=master)

## Features

- Create and access:
  - Events
  - Registrations
  - Runs
  - Handicap/Competition Groups
- Attach a time to the first run at a given event lacking a time
- Score runs for a single registration

## Status

Coner Core is in an early alpha stage of development.

* ~~[Scaffold Phase 1](https://github.com/caeos/coner-core/milestone/2) aims to implement the bare minimum level of functionality in the core service such that UI development can begin~~
* [Scaffold Phase 2](https://github.com/caeos/coner-core/milestone/3) aims to fill in the gaps left in phase 1 such that the UI can reach a bare minimum level of functionality

## Try it!

### Run

#### Released Versions

Refer to this section for released versions of Coner Core.

##### Requirements

To run a release build you'll need
- Java 8
- Port 8080 and 8081 available

##### Download

Download a release from this project's [Releases][releases] page and unzip it.

##### Start

1. `cd` into the folder where you extracted the release and do:
2. `java -jar coner-core-service-${version}.jar server test.yml`

[releases]: https://github.com/caeos/coner-core/releases

#### Build from Source

Refer to this section if you want to compile the code yourself.

##### Requirements

To build from source, you'll need
- JDK 8
- Maven
- Port 8080 and 8081 available

##### Build

1. `mvn clean package`
2. `java -jar service/target/coner-core-service-${version}.jar server service/src/test/resources/config/test.yml`

### Actions

After you've started the service using any of the above methods, the last few lines should resemble the following:

```
INFO  [2017-09-05 02:19:14,843] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@161451a4{/,null,AVAILABLE}
INFO  [2017-09-05 02:19:14,848] org.eclipse.jetty.server.AbstractConnector: Started application@3a33872d{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
INFO  [2017-09-05 02:19:14,848] org.eclipse.jetty.server.AbstractConnector: Started admin@439be00{HTTP/1.1,[http/1.1]}{0.0.0.0:8081}
INFO  [2017-09-05 02:19:14,848] org.eclipse.jetty.server.Server: Started @3545ms
```

#### List Events

You may request a listing of events:

```
$ curl http://localhost:8080/events
{"events":[]}
```
Since you've probably just started up the service, there aren't any yet.
Refer to the API spec for information about more operations.

#### Service Health

You may check the service's health:

```
$ curl http://localhost:8081/healthcheck
{"deadlocks":{"healthy":true},"hibernate":{"healthy":true}}
```

### API Spec

Coner Core uses Swagger to document its API resources. While the service is
running, visit its [Swagger UI](http://localhost:8080/swagger).

## Contributing

Interested in contributing to Coner Core? Please take a look at our [contributing guidlines](https://github.com/carltonwhitehead/coner/blob/master/CONTRIBUTING.md)
