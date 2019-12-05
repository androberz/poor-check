/**
 * The MIT License (MIT)
 *
 * Copyright © «2019» «ANDREY BEREZIN»
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.aber.plugins.poorcheck.helper;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public final class CheckUtils {

    private CheckUtils() {
    }

    public static <T> void doIfNonNull(T toCheckForNull, Consumer<T> consumer) {
        if (Objects.nonNull(toCheckForNull)) {
            consumer.accept(toCheckForNull);
        }
    }

    public static <T, R> R getIfNonNull(T toCheckForNull, Function<T, R> function) {
        if (Objects.nonNull(toCheckForNull)) {
            return function.apply(toCheckForNull);
        }

        return null;
    }

    public static <T> void doIfInstanceIs(Object toCheck, Class<T> instanceType, Consumer<T> consumer) {
        doIfNonNull(toCheck, checkedForNull -> {
            if (instanceType.isAssignableFrom(checkedForNull.getClass())) {
                consumer.accept(instanceType.cast(toCheck));
            }
        });
    }

    public static <T, R> R getIfInstanceIs(Object toCheck, Class<T> instanceType, Function<T, R> function) {
        return getIfNonNull(toCheck, checkedForNull -> {
            if (instanceType.isAssignableFrom(checkedForNull.getClass())) {
                return function.apply(instanceType.cast(toCheck));
            }
            return null;
        });
    }
}
