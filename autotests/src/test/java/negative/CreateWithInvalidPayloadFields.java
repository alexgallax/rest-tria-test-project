package negative;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
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

@DisplayName("Error message for invalid payload when create triangle")
public class CreateWithInvalidPayloadFields {

    @RegisterExtension
    public App app = createApp();

    public static Stream<Arguments> invalidPayloads() {
        return Stream.of(
                arguments(new ObjectMapper().createObjectNode()
                        .put("incorrectFieldName", String.format("%s;%s;%s", 2, 3, 4))),
                arguments(new ObjectMapper().createObjectNode()
                        .put(INPUT_FIELD, "")),
                arguments(new ObjectMapper().createObjectNode()
                        .put(INPUT_FIELD, String.format("%s;%s", 2, 3))),
                arguments(new ObjectMapper().createObjectNode()
                        .put(INPUT_FIELD, String.format("%s;%s;%s;%s", 2, 3, 4, 2))),
                arguments(new ObjectMapper().createObjectNode()
                        .put(SEPARATOR_FIELD, ",")
                        .put(INPUT_FIELD, String.format("%s;%s;%s", 2, 3, 4))),
                arguments(new ObjectMapper().createObjectNode()
                        .put(SEPARATOR_FIELD, "")
                        .put(INPUT_FIELD, String.format("%s;%s;%s", 2, 3, 4)))
        );
    }

    @ParameterizedTest(name = "Error message if invalid payload when create triangle")
    @Description("Should return correct error message when try to create triangle with invalid payload in request.")
    @MethodSource("invalidPayloads")
    public void shouldReturnErrorIfInvalidPayload(ObjectNode payload) throws JsonProcessingException {
        Response response = app.authJsonRequest()
                .body(payload)
                .post(createPath());

        assertThat(String.format("Adding triangle with invalid payload returns non-error status code\n%s",
                response.then().log().all()),
                response.statusCode(), not(equalTo(200)));

        ErrorModel actualPayload = new ObjectMapper().readValue(response.body().asString(), ErrorModel.class);

        assertThat(String.format(":\n%s",
                response.then().log().all()),
                actualPayload, equalToObject(unprocessableEntity().path("/triangle")));
    }
}
