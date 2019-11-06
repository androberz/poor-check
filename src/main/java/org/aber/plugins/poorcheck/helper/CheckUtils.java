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
