package com.mib.stream.pagination;

import static java.time.Duration.ofMillis;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.javafaker.Faker;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@link PageStream}
 */
class PageStreamTest {

    private static Logger LOG = LoggerFactory.getLogger(PageStreamTest.class);

    private static final Faker FAKER = new Faker();

    @Mock
    public PageSupplier<String> pageSupplier;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldPaginateCorrectly() {

        LOG.info("--------------------------------------------------------------");
        LOG.info("Testing nominal pagination case with 2 pages of 2 elements");
        LOG.info("[Page1 with {element1 ; element2} , Page2 with {element3 ; element4}]");
        LOG.info("--------------------------------------------------------------\n\n");

        final Page<String> firstPage  = newHasNextPage();
        final Page<String> lastPage   = newLastPage();

        when(pageSupplier.get())
            .thenReturn(firstPage)
            .thenReturn(lastPage);

        final Stream<String> pageStream = PageStream.pageStream(pageSupplier);
        final Iterator<String> iterator = pageStream.iterator();

        LOG.info("'Building a pageStream does not request a page'\n");

        verify(pageSupplier, times(0)).get();

        LOG.info("'Asking for the first element requests only one page'");
        LOG.info("\t\t[Page1 - element1]: '{}'\n", iterator.next());

        verify(pageSupplier, times(1)).get();

        LOG.info("'Asking for the last element of the first page does not request next page'");
        LOG.info("\t\t[Page1 - element2]: '{}'\n", iterator.next());

        verify(pageSupplier, times(1)).get();

        LOG.info("'Asking for another element requests next page'");
        LOG.info("\t\t[Page2 - element3]: '{}'\n", iterator.next());

        verify(pageSupplier, times(2)).get();

        LOG.info("'Asking for last element does not request for a new page'");
        LOG.info("\t\t[Page2 - element4]: '{}'\n", iterator.next());

        verify(pageSupplier, times(2)).get();

        LOG.info("'Stream is finished when there is no other page'");
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    void shouldPaginateWithParallelCallDoesNotMakeTheStreamToFail() {

        LOG.info("--------------------------------------------------------------");
        LOG.info("Testing that a call to parallel does not make the stream to fail");
        LOG.info("[Page1 with {element1 ; element2} , Page2 with {element3 ; element4}]");
        LOG.info("--------------------------------------------------------------\n\n");

        final Page<String> firstPage  = newHasNextPage();
        final Page<String> lastPage   = newLastPage();

        when(pageSupplier.get())
            .thenReturn(firstPage)
            .thenReturn(lastPage);

        final Stream<String> pageStream = PageStream.pageStream(pageSupplier);

        assertTimeoutPreemptively(ofMillis(500), () -> {
            assertThat(pageStream.parallel().collect(toList()).size()).isEqualTo(4);
        });
    }

    private static Page<String> newLastPage() {
        return ImmutablePage.of(buildUniversalFacts(), false);
    }

    private static Page<String> newHasNextPage() {
        return ImmutablePage.of(buildUniversalFacts(), true);
    }

    private static List<String> buildUniversalFacts() {
        return IntStream.range(0, 2)
            .boxed()
            .map(i -> FAKER.chuckNorris().fact())
            .collect(toList());
    }
}