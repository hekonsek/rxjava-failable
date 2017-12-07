package com.github.hekonsek.rxjava.failable;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import static com.github.hekonsek.rxjava.failable.FailableFlatMap.failable;
import static io.reactivex.Observable.just;
import static io.reactivex.Observable.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.Timeout.seconds;

@RunWith(VertxUnitRunner.class)
public class FailableFlatMapTest {

    @Rule
    public Timeout timeout = seconds(5);

    @Test
    public void shouldExecuteFailureCallback(TestContext testContext) {
        Async async = testContext.async();
        range(0, 4).
                compose(failable(i -> just(4 / i), failure -> async.complete())).
                subscribe();
    }

    @Test
    public void shouldExecuteMapper() {
        Iterable<Integer> results = range(0, 4).
                compose(failable(i -> just(4 / i), failure -> {
                })).
                blockingIterable();
        assertThat(results).hasSize(3);
    }

    @Test
    public void failureShouldIncludeOriginalValue(TestContext context) {
        Async async = context.async();
        range(0, 4).
                compose(failable(i -> just(4 / i), failure -> {
                    assertThat(failure.value()).isEqualTo(0);
                    async.complete();
                })).
                subscribe();
    }

    @Test
    public void failureShouldIncludeCause(TestContext context) {
        Async async = context.async();
        range(0, 4).
                compose(failable(i -> just(4 / i), failure -> {
                    assertThat(failure.cause()).isInstanceOf(ArithmeticException.class);
                    async.complete();
                })).
                subscribe();
    }

}
