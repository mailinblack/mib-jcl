package com.mib.stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link MibStreams}.
 */
class MibStreamsTest {

    @Test
    void shouldConvertToStream() {

        final Iterator<String> iterator = ImmutableList.of("e1", "e2", "e3").iterator();

        final Stream<String> stream = MibStreams.toStream(iterator);

        assertThat(stream.collect(toList())).containsExactly("e1", "e2", "e3");
    }
}