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

import static java.lang.String.format;

public class Failure<Upstream> {

    private final Upstream value;

    private final Throwable cause;

    public Failure(Upstream value, Throwable cause) {
        this.value = value;
        this.cause = cause;
    }

    public Upstream value() {
        return value;
    }

    public Throwable cause() {
        return cause;
    }

    @Override public String toString() {
        String message = cause.getMessage() != null ? cause.getMessage() : "";
        return format("Upstream value: %s. Cause: %s: %s", value, cause.getClass().getSimpleName(), message);
    }

}