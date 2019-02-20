package com.mib.stream;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;

/**
 * Helper functions that operate on any {@code Stream}.
 */
public enum MibStreams {
    ;

    public static <T> Iterator<T> iterator(final Stream<T> stream) {
        return Spliterators.iterator(stream.spliterator());
    }
}
