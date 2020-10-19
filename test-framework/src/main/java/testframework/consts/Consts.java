package testframework.consts;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class Consts {

    public static Stream<Arguments> triangleSides() {
        return Stream.of(
                arguments(2, 3, 4),
                arguments(4, 4, 3),
                arguments(3, 3, 3),
                arguments(3, 4, Math.sqrt(Math.pow(3, 2) + Math.pow(4, 2))),
                arguments(10.00000000000000000001, 9.2, 8.3),
                arguments(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE)
        );
    }

    public static Stream<Arguments> bigTriangleSides() {
        return Stream.of(
                arguments(Float.MAX_VALUE, Float.MAX_VALUE - 1, Float.MAX_VALUE - 2),
                arguments(-Float.MAX_VALUE, -Float.MAX_VALUE + 1, -Float.MAX_VALUE + 2)
        );
    }
}
