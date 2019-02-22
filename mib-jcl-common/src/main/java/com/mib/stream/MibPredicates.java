package com.mib.stream;

import static java.util.concurrent.ConcurrentHashMap.newKeySet;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utility methods for {@link Predicate}.
 */
public enum MibPredicates {
    ;

    /**
     * Returns a predicate avoiding redundancy (keeping first unique element)
     * based on the given function.
     *
     * See <a href="https://stackoverflow.com/questions/23699371/java-8-distinct-by-property">Stack overflow</a>
     * @param propertyBasedFunction the function used to check unique elements
     * @param <T> the element type
     * @return  the predicate
     */
    public static <T> Predicate<T> distinct(final Function<? super T, ?> propertyBasedFunction) {
        final Set<Object> seen = newKeySet();
        return t -> seen.add(propertyBasedFunction.apply(t));
    }
}
