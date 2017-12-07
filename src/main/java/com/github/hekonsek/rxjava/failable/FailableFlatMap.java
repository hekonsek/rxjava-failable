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
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static io.reactivex.Observable.defer;
import static io.reactivex.Observable.empty;

public class FailableFlatMap<Upstream, Downstream> implements ObservableTransformer<Upstream, Downstream> {

    // Members

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

    // Transformation

    @Override public ObservableSource<Downstream> apply(Observable<Upstream> upstream) {
        return upstream.flatMap(value -> defer(() -> mapper.apply(value)).onErrorResumeNext(cause -> {
            failureCallback.accept(new Failure<>(value, cause));
            return empty();
        }));
    }

}