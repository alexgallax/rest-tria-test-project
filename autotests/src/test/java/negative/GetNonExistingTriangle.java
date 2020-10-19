package negative;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static testframework.consts.ErrorPayloads.notFoundPayload;

@DisplayName("Error message when get triangle with non-existing id")
public class GetNonExistingTriangle {

    private static final String NON_EXISTING_ID = "1234";

    @RegisterExtension
    public App app = createApp();

    public static Stream<Arguments> responses() {
        return Stream.of(
                arguments(createApp().authJsonRequest().get(getPath(NON_EXISTING_ID)),
                        String.format("/triangle/%s", NON_EXISTING_ID)),
                arguments(createApp().authJsonRequest().delete(getPath(NON_EXISTING_ID)),
                        String.format("/triangle/%s", NON_EXISTING_ID)),
                arguments(createApp().authJsonRequest().get(getPerimeterPath(NON_EXISTING_ID)),
                        String.format("/triangle/%s/perimeter", NON_EXISTING_ID)),
                arguments(createApp().authJsonRequest().get(getAreaPath(NON_EXISTING_ID)),
                        String.format("/triangle/%s/area", NON_EXISTING_ID))
        );
    }

    @ParameterizedTest(name = "Error message if \"{1}\" triangle with non-existing id")
    @Description("Should return correct error message when try to request triangle by non-existing id.")
    @MethodSource("responses")
    public void shouldIfNonExistingId(Response response, String path) throws JsonProcessingException {
        assertThat(String.format("'%s' request with non-existing id returns non-error status code\n%s",
                path, response.then().log().all()),
                response.statusCode(), not(equalTo(200)));

        ErrorModel actualPayload = new ObjectMapper().readValue(response.body().asString(), ErrorModel.class);

        assertThat(String.format(":\n%s",
                response.then().log().all()),
                actualPayload, equalToObject(notFoundPayload().path(path)));
    }
}
