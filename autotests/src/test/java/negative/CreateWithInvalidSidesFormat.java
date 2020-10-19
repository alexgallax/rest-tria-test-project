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

@DisplayName("Error messages for invalid sides format when create triangle")
public class CreateWithInvalidSidesFormat {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RegisterExtension
    public App app = createApp();

    public static Stream<Arguments> triangleIncorrectSideTypes() {
        return Stream.of(
                arguments("a", "2", "3"),
                arguments("4", "K", "3"),
                arguments("3", "2", "abs"),
                arguments("4", "±!@#$%^&*()_+|:LM<>\"", "3"),
                arguments("3", "2", ""),
                arguments(".1", "2", "3"),
                arguments("3", "..2", "3"),
                arguments("!5", "10", "15"),
                arguments("2+3", "4", "4")
        );
    }

    @ParameterizedTest(name = "Error message if invalid sides when create triangle")
    @Description("Should return correct error message when try to create triangle with invalid sides format in request.")
    @MethodSource("triangleIncorrectSideTypes")
    public void shouldReturnErrorIfIncorrectSideType(String firstSide, String secondSide, String thirdSide) throws JsonProcessingException {
        ObjectNode payload = objectMapper.createObjectNode()
                .put(INPUT_FIELD, String.format("%s;%s;%s", firstSide, secondSide, thirdSide));

        Response response = app.authJsonRequest()
                .body(payload)
                .post(createPath());

        assertThat(String.format("Adding triangle with invalid sides returns non-error status code\n%s",
                response.then().log().all()),
                response.statusCode(), not(equalTo(200)));

        ErrorModel actualPayload = new ObjectMapper().readValue(response.body().asString(), ErrorModel.class);

        assertThat(String.format(":\n%s",
                response.then().log().all()),
                actualPayload, equalToObject(unprocessableEntity().path("/triangle")));
    }
}
