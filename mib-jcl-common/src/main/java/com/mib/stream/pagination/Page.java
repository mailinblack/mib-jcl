package com.mib.stream.pagination;

import static java.util.Collections.emptyList;

import java.util.List;
import org.immutables.value.Value;

/**
 * Representation of a page of elements.
 *
 * @param <T> the elements type
 */
@Value.Immutable
public interface Page<T> {

    @Value.Parameter
    List<T> elements();

    @Value.Parameter
    boolean hasPotentiallyOtherResults();

    static <T> Page<T> emptyHavingNext() {
        return ImmutablePage.of(emptyList(), true);
    }
}
