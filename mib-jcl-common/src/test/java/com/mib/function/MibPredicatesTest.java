package com.mib.function;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link MibPredicates}.
 */
class MibPredicatesTest {

    @Test
    void shouldDistinctWorksCorrectly() {

        final List<CustomObject> customObjects = ImmutableList.<CustomObject>builder()
            .add(new CustomObject(1))
            .add(new CustomObject(1))
            .add(new CustomObject(1))
            .add(new CustomObject(2))
            .add(new CustomObject(2))
            .add(new CustomObject(3))
            .build();

        final List<CustomObject> distinctObjects = customObjects.stream()
            .filter(MibPredicates.distinct(o -> o.value))
            .collect(toList());

        assertThat(distinctObjects.stream().map(o -> o.value).collect(toList()))
            .containsExactly(1, 2, 3);
    }

    class CustomObject {
        int value;

        CustomObject(int value) {
            this.value = value;
        }
    }
}