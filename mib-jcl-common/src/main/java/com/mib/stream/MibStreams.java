package com.mib.stream;

import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Utility mehods related to {@link Stream}.
 */
public enum MibStreams {
    ;

    public static <T> Stream<T> toStream(final Iterator<T> iterator) {
        return stream(spliteratorUnknownSize(iterator, 0), false);
    }


}
