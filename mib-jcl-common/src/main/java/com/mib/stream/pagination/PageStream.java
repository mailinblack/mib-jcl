package com.mib.stream.pagination;

import static java.lang.Long.MAX_VALUE;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.concat;
import static java.util.stream.StreamSupport.stream;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Utility methods to build a {@link Stream} based on pagination.
 */
public enum PageStream {

    ;

    /**
     * Returns a {@link Stream} from the given {@code pageSupplier}.
     * <p>
     * The page supplier is requested when the stream is consumed - only when required.
     * <p>
     * Note: a call to {@code parallel()} does not do anything.
     *
     * @param pageSupplier the page supplier
     * @param <T> the page element type
     * @return the stream
     * @throws NullPointerException if {@code pageSupplier} is {@code null}
     */
    public static <T> Stream<T> pageStream(final Supplier<Page<T>> pageSupplier) {
        requireNonNull(pageSupplier);

        return chainLazily(Page.emptyHavingNext(), pageSupplier)
            .filter(v -> !v.elements().isEmpty())
            .flatMap(r -> r.elements().stream());
    }

    private static <T> Stream<Page<T>> chainLazily(final Page<T> r, final Supplier<Page<T>> pageSupplier) {
        final Stream<Page<T>> first = Stream.of(r);
        final Stream<Page<T>> nextResults = stream(new PageSplitIterator<>(pageSupplier), false);
        return concat(first, nextResults);
    }

    private static class PageSplitIterator<T> extends AbstractSpliterator<Page<T>> {

        private final Supplier<Iterator<Page<T>>> queryIterator;

        PageSplitIterator(final Supplier<Page<T>> pageSupplier) {
            super(MAX_VALUE, NONNULL);
            queryIterator = () -> chainLazily(pageSupplier.get(), pageSupplier).iterator();
        }

        @Override
        public boolean tryAdvance(final Consumer<? super Page<T>> action) {
            final Page<T> nextResult = queryIterator.get().next();
            action.accept(nextResult);
            return nextResult.hasPotentiallyOtherResults();
        }

        @Override
        public Spliterator<Page<T>> trySplit() {
            // Parallelism is not supported
            return null;
        }
    }
}
