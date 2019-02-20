package com.mib.stream.pagination;

import java.util.function.Supplier;

/**
 * Representation of a page supplier.
 *
 * @param <T> the page element type
 */
public interface PageSupplier<T> extends Supplier<Page<T>> {
    // Marker interface
}
