# RxJava HTTP connector

[![Version](https://img.shields.io/badge/RxJava%20Connector%20HTTP-0.0-blue.svg)](https://github.com/hekonsek/rxjava-connector-http/releases)
[![Build](https://api.travis-ci.org/hekonsek/rxjava-connector-http.svg)](https://travis-ci.org/hekonsek/rxjava-connector-http)

Connector for RxJava bridging HTTP endpoint with [RxJava events](https://github.com/hekonsek/rxjava-event).

## Installation

In order to start using Vert.x Pipes add the following dependency to your Maven project:

    <dependency>
      <groupId>com.github.hekonsek</groupId>
      <artifactId>vertx-connector-http</artifactId>
      <version>0.0</version>
    </dependency>

## Usage

This is how you can start embedded Vert.x-based HTTP server and consume incoming requests:

```
HttpSourceFactory httpSourceFactory = new HttpSourceFactory(vertx);
httpSourceFactory.build("/foo").build().subscribe(event ->
  System.out.println("POSTed JSON: " + event.payload())
);
httpSourceFactory.listen().subscribe();
```

If you would like to send a response back to the client, you need to obtain a response callback from
the incoming request event:

```
import static import io.vertx.core.json.Json.encode;
import static com.github.hekonsek.rxjava.event.Headers.responseCallback;

...

HttpSourceFactory httpSourceFactory = new HttpSourceFactory(vertx);
httpSourceFactory.build("/foo").build().subscribe(event ->
  responseCallback(event).get().respond(encode(ImmutableMap.of("hello", ""world)));
);
httpSourceFactory.listen().subscribe();
```


## License

This project is distributed under Apache 2.0 license.