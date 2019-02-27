package com.mib.stream;

import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.StreamSupport.stream;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Utility methods related to {@link Stream}.
 */
public enum MibStreams {
    ;

    /**
     * Note: similar to Guava Streams equivalent - but not considered as "Beta".
     */
    public static <T> Stream<T> toStream(final Iterator<T> iterator) {
        return stream(spliteratorUnknownSize(iterator, 0), false);
    }

    public static <T> Stream<T> toStream(final Iterable<T> iterable) {
        if(iterable instanceof Collection) {
            return ((Collection<T>) iterable).stream();
        }
        return stream(iterable.spliterator(), false);
    }

    /**
     * Shortcut to do grouping of a stream of elements with the given {@code groupingFunction}.
     * <p>
     * Note: Beware of the performance here: calls internally {@code collect()}
     * on the given {@code stream} and then build a stream from the intermediate result.
     *
     * @param stream the Stream
     * @param groupingFunction the {@link Function} used to group the elements
     * @param <T> the type of the elements
     * @param <C> the type of the discriminating criterion used to group the elements
     * @return a stream of groups (as a list)
     * @throws NullPointerException if any of the given parameters is {@code null}
     */
    public static <T, C> Stream<List<T>> groupBy(final Stream<T> stream, final Function<T, C> groupingFunction) {
        return stream.collect(groupingBy(groupingFunction, mapping(u -> u, toList()))).values().stream();
    }

    /**
     * Shortcut to repeat two times a stream based on the given {@link Stream} supplier.
     *
     * @param streamSupplier the stream {@link Supplier}
     * @param <T> the stream element type
     * @return concatenated stream.
     */
    public static <T> Stream<T> repeatTwice(final Supplier<Stream<T>> streamSupplier) {
        return concat(streamSupplier.get(), streamSupplier.get());
    }
}
