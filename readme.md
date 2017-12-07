# RxJava Failable

[![Version](https://img.shields.io/badge/RxJava%20Failable-0.2-blue.svg)](https://github.com/hekonsek/rxjava-failable/releases)
[![Build](https://api.travis-ci.org/hekonsek/rxjava-failable.svg)](https://travis-ci.org/hekonsek/rxjava-failable)
[![Coverage](https://sonarcloud.io/api/badges/measure?key=com.github.hekonsek%3Arxjava-failable&metric=coverage)](https://sonarcloud.io/component_measures?id=com.github.hekonsek%3Arxjava-failable&metric=coverage)

RxJava Failable provides constructs for handling RxJava processing exceptions without interrupting continuous stream.

## Installation

In order to start using Vert.x Failable add the following dependency to your Maven project:

    <dependency>
      <groupId>com.github.hekonsek</groupId>
      <artifactId>rxjava-failable</artifactId>
      <version>0.2</version>
    </dependency>

## Usage

The following example demonstrates how to create callback invoked whenever flat map block throws an exception:

```
import static com.github.hekonsek.rxjava.failable.FailableFlatMap.failableFlatMap;

...

Observable.just(0, 1, 2, 4).
  compose(
    failableFlatMap(i -> just(4/i), failure -> {
      System.out.print("Error " + failure.cause().getClass().getSimpleName());
      System.out.println(" for value: " + failure.value());
    })
  ).subscribe(System.out::println);

```

The snippet above displays the following output:

```
Error ArithmeticException for value: 0
4
2
1
```

Please note that an invocation of a failure handles doesn't stop `Observable` from processing. What is also important, the original value
processing of which caused the exception is sent to failure callback as well.

## License

This project is distributed under Apache 2.0 license.