package com.github.hekonsek.rxjava.failable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static io.reactivex.Observable.empty;
import static io.reactivex.Observable.just;

public class FailableFlatMap<Upstream, Downstream> implements ObservableTransformer<Upstream, Downstream> {

    public static void main(String[] args) {
        just(1, 2, 0, 3, 4).
                compose(failable(i -> just(12 / i), e -> System.out.println("Error: " + e.cause().getMessage() + " || Event: " + e.value()))).
                subscribe(System.out::println);
    }

    private final Function<Upstream, ? extends ObservableSource<? extends Downstream>> mapper;

    private final Consumer<Failure<Upstream>> failureCallback;

    // Constructors

    public FailableFlatMap(Function<Upstream, ? extends ObservableSource<? extends Downstream>> mapper, Consumer<Failure<Upstream>> failureCallback) {
        this.mapper = mapper;
        this.failureCallback = failureCallback;
    }

    // Factories

    public static <Upstream, Downstream> FailableFlatMap<Upstream, Downstream> failableFlatMap(
            Function<Upstream, ? extends ObservableSource<? extends Downstream>> mapper,
            Consumer<Failure<Upstream>> failureCallback) {
        return new FailableFlatMap<>(mapper, failureCallback);
    }

    public static <Upstream, Downstream> FailableFlatMap<Upstream, Downstream> failable(
            Function<Upstream, ? extends ObservableSource<? extends Downstream>> mapper,
            Consumer<Failure<Upstream>> failureCallback) {
        return failableFlatMap(mapper, failureCallback);
    }

    @Override public ObservableSource<Downstream> apply(Observable<Upstream> upstream) {
        return upstream.flatMap(i -> {
            try {
                return mapper.apply(i);
            } catch (Throwable t) {
                failureCallback.accept(new Failure<>(i, t));
                return empty();
            }
        });
    }

}