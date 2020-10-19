package negative;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import testframework.app.App;
import testframework.models.ErrorModel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsEqual.equalToObject;
import static org.hamcrest.core.IsNot.not;
import static testframework.app.App.*;
import static testframework.consts.ErrorPayloads.*;

@DisplayName("Error message on invalid request")
public class InvalidRequest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RegisterExtension
    public App app = createApp();

    @Test
    @DisplayName("Unauthorized request")
    @Description("Should return correct error message on unauthorized request.")
    public void errorIfUnauthorizedRequest() throws JsonProcessingException {
        Response response = app.nonAuthRequest()
                .get(getAllPath());

        assertThat(String.format("Unauthorized request returns non-error status code\n%s",
                response.then().log().all()),
                response.statusCode(), not(equalTo(200)));

        ErrorModel payload = objectMapper.readValue(response.body().asString(), ErrorModel.class);

        assertThat(String.format(":\n%s",
                response.then().log().all()),
                payload, equalToObject(unauthorizedPayload().path("/triangle/all")));
    }

    @Test
    @DisplayName("Non existing path")
    @Description("Should return correct error message on request to non existing path.")
    public void errorNonExistingPath() throws JsonProcessingException {
        String nonExistingPath = "nonexists";

        Response response = app.nonAuthRequest()
                .get(String.format("%s/%s", props().baseUrl(), nonExistingPath));

        assertThat(String.format("Request to non existing path returns non-error status code\n%s",
                response.then().log().all()),
                response.statusCode(), not(equalTo(200)));

        ErrorModel payload = objectMapper.readValue(response.body().asString(), ErrorModel.class);

        assertThat(String.format(":\n%s",
                response.then().log().all()),
                payload, equalToObject(notFoundPayload().path(String.format("/%s", nonExistingPath))));
    }

    @Test
    @DisplayName("Empty body")
    @Description("Should return correct error message on request with empty body.")
    public void errorIfEmptyRequestBody() throws JsonProcessingException {
        Response response = app.authJsonRequest()
                .post(createPath());

        assertThat(String.format("Request with empty body returns non-error status code\n%s",
                response.then().log().all()),
                response.statusCode(), not(equalTo(200)));

        ErrorModel payload = objectMapper.readValue(response.body().asString(), ErrorModel.class);

        assertThat(String.format(":\n%s",
                response.then().log().all()),
                payload, equalToObject(requestBodyMissingPayload().path("/triangle")));
    }

    @Test
    @DisplayName("Invalid media type")
    @Description("Should return correct error message on request with invalid media type.")
    public void errorIfInvalidMediaTypeBody() throws JsonProcessingException {
        Response response = app.authRequest()
                .body(new ObjectMapper().createObjectNode().put(INPUT_FIELD, String.format("%s;%s;%s", 2, 3, 4)))
                .post(createPath());

        assertThat(String.format("Request with invalid media type returns non-error status code\n%s",
                response.then().log().all()),
                response.statusCode(), not(equalTo(200)));

        ErrorModel payload = objectMapper.readValue(response.body().asString(), ErrorModel.class);

        assertThat(String.format(":\n%s",
                response.then().log().all()),
                payload, equalToObject(unsupportedMediaTypePayload().path("/triangle")));
    }

    @Test
    @DisplayName("Non Json body")
    @Description("Should return correct error message on request with non-json body.")
    public void errorIfNonJsonBody() throws JsonProcessingException {
        String body = "invalid";
        Response response = app.authJsonRequest()
                .body(body)
                .post(createPath());

        assertThat(String.format("Request with non-json body returns non-error status code\n%s",
                response.then().log().all()),
                response.statusCode(), not(equalTo(200)));

        ErrorModel payload = objectMapper.readValue(response.body().asString(), ErrorModel.class);

        assertThat(String.format(":\n%s",
                response.then().log().all()),
                payload.message(null), equalToObject(invalidJsonBodyPayload().path("/triangle")));
    }
}
