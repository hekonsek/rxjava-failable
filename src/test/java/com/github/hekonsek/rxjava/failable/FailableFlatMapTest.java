package com.github.hekonsek.rxjava.failable;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.github.hekonsek.rxjava.failable.FailableFlatMap.failable;
import static io.reactivex.Observable.just;
import static io.reactivex.Observable.range;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(VertxUnitRunner.class)
public class FailableFlatMapTest {

    @Test
    public void shouldExecuteFailureCallback(TestContext testContext) {
        Async async = testContext.async();
        range(0, 4).
                compose(failable(i -> just(4/i), failure -> async.complete())).
                subscribe();
    }

    @Test
    public void shouldExecuteMapper() {
        Iterable<Integer> results = range(0, 4).
                compose(failable(i -> just(4/i), failure -> {})).
                blockingIterable();
        assertThat(results).hasSize(3);
    }

}
