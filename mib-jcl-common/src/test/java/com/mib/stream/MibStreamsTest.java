package com.mib.stream;

import static com.google.common.collect.ImmutableList.of;
import static com.mib.stream.MibStreams.repeatTwice;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link MibStreams}.
 */
class MibStreamsTest {

    @Test
    void shouldConvertToStream() {

        final Iterator<String> iterator = of("e1", "e2", "e3").iterator();

        final Stream<String> stream = MibStreams.toStream(iterator);

        assertThat(stream.collect(toList())).containsExactly("e1", "e2", "e3");
    }

    @Test
    void shouldGroupByCorrectly() {

        final CustomAddress address1 = new CustomAddress("Paris");
        final CustomAddress address2 = new CustomAddress("Dublin");
        final CustomAddress address3 = new CustomAddress("London");
        final List<CustomAddress> addresses = ImmutableList.<CustomAddress>builder()
            .add(address1)
            .add(address2)
            .add(address3)
            .add(address1)
            .add(address2)
            .add(address3)
            .build();

        final List<List<CustomAddress>> groupsOfCities = MibStreams.groupBy(addresses.stream(), CustomAddress::cityName)
            .collect(toList());

        assertThat(groupsOfCities.size()).isEqualTo(3);
        groupsOfCities.forEach(group -> assertThat(group.size()).isEqualTo(2));
    }

    @Test
    void shouldRepeatTwiceCorrectly() {
        final Supplier<Stream<String>> supplier = () -> of("e1", "e2").stream();

        final List<String> repeated = repeatTwice(supplier).collect(toList());

        assertThat(repeated).containsExactly("e1", "e2", "e1", "e2");
    }

    private static class CustomAddress {

        private final String cityName;

        private CustomAddress(String cityName) {
            this.cityName = cityName;
        }

        String cityName() {
            return cityName;
        }
    }
}