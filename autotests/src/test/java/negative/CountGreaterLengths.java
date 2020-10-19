package negative;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testframework.app.App;
import testframework.consts.Consts;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static testframework.app.App.*;
import static testframework.utils.Utils.createTriangleWithSides;

@DisplayName("Count area and perimeter if result more float max value")
public class CountGreaterLengths {

    private String id;

    @RegisterExtension
    public App app = createApp();

    private static Stream<Arguments> bigTriangleSides() {
        return Consts.bigTriangleSides();
    }

    @ParameterizedTest(name = "Calculate triangle's area with sides [\"{0}\", \"{1}\", \"{2}\"]")
    @Description("Should return correct error message when try to calculate area increasing float max value.")
    @MethodSource("bigTriangleSides")
    public void shouldCalculateCorrectAreaBigLengths(double firstSide, double secondSide, double thirdSide) {
        id = createTriangleWithSides(firstSide, secondSide, thirdSide);

        Response response = app.authJsonRequest()
                .get(getAreaPath(id));

        assertThat(String.format("Calculating triangle's area increasing float max value returns non-error status code\n%s",
                response.then().log().all()),
                response.statusCode(), not(equalTo(200)));
    }

    @ParameterizedTest(name = "Calculate triangle's perimeter with sides [\"{0}\", \"{1}\", \"{2}\"]")
    @Description("Should return correct error message when try to calculate perimeter increasing float max value.")
    @MethodSource("bigTriangleSides")
    public void shouldCalculateCorrectPerimeterBigLengths(double firstSide, double secondSide, double thirdSide) {
        id = createTriangleWithSides(firstSide, secondSide, thirdSide);

        Response response = app.authJsonRequest()
                .get(getPerimeterPath(id));

        assertThat(String.format("Calculating triangle's area increasing float max value returns non-error status code\n%s",
                response.then().log().all()),
                response.statusCode(), not(equalTo(200)));
    }

    @AfterEach
    public void cleanData() {
        app.authJsonRequest().delete(deletePath(id));
    }
}
