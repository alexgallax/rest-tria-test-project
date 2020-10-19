package basic;

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
import static testframework.app.App.*;
import static testframework.utils.Utils.countTrianglePerimeter;
import static testframework.utils.Utils.createTriangleWithSides;

@DisplayName("Count perimeter of triangles with different sides lengths")
public class CountTrianglePerimeter {

    private String id;

    @RegisterExtension
    public App app = createApp();

    private static Stream<Arguments> triangleSides() {
        return Consts.triangleSides();
    }

    @ParameterizedTest(name = "Calculate triangle's perimeter with sides [\"{0}\", \"{1}\", \"{2}\"]")
    @Description("Should correctly calculate perimeter of valid triangle.")
    @MethodSource("triangleSides")
    public void shouldCalculateCorrectPerimeter(double firstSide, double secondSide, double thirdSide) {
        id = createTriangleWithSides(firstSide, secondSide, thirdSide);

        Response response = app.authJsonRequest()
                .get(getPerimeterPath(id));

        assertThat(String.format("Get triangle request returns incorrect status code\n%s", response.then().log().all()),
                response.statusCode(), equalTo(200));
        assertThat(String.format("Triangle request returns incorrect perimeter for sides: [%s, %s, %s]\n%s",
                firstSide, secondSide, thirdSide, response.then().log().all()),
                response.body().jsonPath().get("result"),
                equalTo(countTrianglePerimeter(firstSide, secondSide, thirdSide)));
    }

    @AfterEach
    public void cleanData() {
        app.authJsonRequest().delete(deletePath(id));
    }
}
