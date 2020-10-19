package negative;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testframework.app.App;
import testframework.models.ErrorModel;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsEqual.equalToObject;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static testframework.app.App.*;
import static testframework.consts.ErrorPayloads.unprocessableEntity;

@DisplayName("Error messages for incorrect triangle sides lengths")
public class NotTrianglesSides {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RegisterExtension
    public App app = createApp();

    public static Stream<Arguments> triangleIncorrectSides() {
        return Stream.of(
                arguments(1, 1, 5),
                arguments(-1, 2, 3),
                arguments(2, -1, 4),
                arguments(3, 2, -4),
                arguments(-2, -3, -4),
                arguments(0, 0, 0),
                arguments(7, 0, 0),
                arguments(0, 2, 0),
                arguments(0, 0, 1000),
                arguments(0, 5, 3),
                arguments(7, 0, 1000),
                arguments(1, 3, 0),
                arguments(1, Double.MAX_VALUE, 2),
                arguments(1, 2, Double.MAX_VALUE + 1),
                arguments(-Double.MAX_VALUE, 2, 3),
                arguments(2, -Double.MAX_VALUE - 1, 3)
        );
    }

    @ParameterizedTest(name = "Error message if incorrect triangle sides lengths [\"{0}\", \"{1}\", \"{2}\"]")
    @Description("Should return correct error message when try to add triangle with sides lengths" +
            " that are not correspond to valid triangle.")
    @MethodSource("triangleIncorrectSides")
    public void shouldReturnErrorIfIncorrectSidesLengths(double firstSide, double secondSide, double thirdSide) throws JsonProcessingException {
        ObjectNode payload = objectMapper.createObjectNode()
                .put(INPUT_FIELD, String.format("%s;%s;%s", firstSide, secondSide, thirdSide));

        Response response = app.authJsonRequest()
                .body(payload)
                .post(createPath());

        assertThat(String.format("Create triangle with incorrect sides lengths [%s, %s, %s] returns" +
                        " non-error status code\n%s", firstSide, secondSide, thirdSide, response.then().log().all()),
                response.statusCode(), not(equalTo(200)));

        ErrorModel actualPayload = new ObjectMapper().readValue(response.body().asString(), ErrorModel.class);

        assertThat(String.format(":\n%s",
                response.then().log().all()),
                actualPayload, equalToObject(unprocessableEntity().path("/triangle")));
    }
}
