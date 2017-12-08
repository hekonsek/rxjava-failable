/**
 * Licensed to the RxJava Failable under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hekonsek.rxjava.failable;

import io.reactivex.Observable;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutorService;

import static com.github.hekonsek.rxjava.failable.FailableFlatMap.failable;
import static io.reactivex.Observable.empty;
import static io.reactivex.Observable.fromFuture;
import static io.reactivex.Observable.just;
import static io.reactivex.Observable.range;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.Timeout.seconds;

@RunWith(VertxUnitRunner.class)
public class FailableFlatMapTest {

    @Rule
    public Timeout timeout = seconds(5);

    ExecutorService executor = newCachedThreadPool();
    
    Observable<Integer> observable = Observable.just(0, 1, 2 ,3);

    // Tests

    @Test
    public void shouldExecuteFailureCallback(TestContext testContext) {
        Async async = testContext.async();
        observable.
                compose(failable(i -> just(4 / i), (cause, value) -> async.complete())).
                subscribe();
    }

    @Test
    public void applyingNewErrorHandlerShouldNotOverrideFailureHandler(TestContext testContext) {
        Async async = testContext.async();
        observable.
                compose(failable(i -> just(4 / i), (cause, value) -> async.complete())).
                onErrorResumeNext(throwable -> {
                    return empty();
                }).
                subscribe();
    }

    @Test
    public void shouldExecuteAsyncFailureCallback(TestContext testContext) {
        Async async = testContext.async();
        observable.
                compose(failable(i -> fromFuture(executor.submit(() -> 4 / i)), (cause, value) -> async.complete())).
                subscribe();
    }

    @Test
    public void shouldExecuteMapper() {
        Iterable<Integer> results = observable.
                compose(failable(i -> just(4 / i), (cause, value) -> {
                })).
                blockingIterable();
        assertThat(results).hasSize(3);
    }

    @Test
    public void shouldExecuteAsyncMapper() {
        Iterable<Integer> results = observable.
                compose(failable(i -> fromFuture(executor.submit(() -> 4 / i)), (cause, value) -> {
                })).
                blockingIterable();
        assertThat(results).hasSize(3);
    }

    @Test
    public void failureShouldIncludeOriginalValue(TestContext context) {
        Async async = context.async();
        observable.
                compose(failable(i -> just(4 / i), (cause, value) -> {
                    assertThat(value).isEqualTo(0);
                    async.complete();
                })).
                subscribe();
    }

    @Test
    public void failureShouldIncludeCause(TestContext context) {
        Async async = context.async();
        observable.
                compose(failable(i -> just(4 / i), (cause, value) -> {
                    assertThat(cause).isInstanceOf(ArithmeticException.class);
                    async.complete();
                })).
                subscribe();
    }

}
