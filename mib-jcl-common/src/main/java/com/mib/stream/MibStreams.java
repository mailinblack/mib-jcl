package com.mib.stream;

import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Utility methods related to {@link Stream}.
 * <p>
 * Note: similar to Guava Streams - but not considered as "Beta".
 */
public enum MibStreams {
    ;

    public static <T> Stream<T> toStream(final Iterator<T> iterator) {
        return stream(spliteratorUnknownSize(iterator, 0), false);
    }

    public static <T> Stream<T> toStream(final Iterable<T> iterable) {
        if(iterable instanceof Collection) {
            return ((Collection<T>) iterable).stream();
        }
        return stream(iterable.spliterator(), false);
    }
}
