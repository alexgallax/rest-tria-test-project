package basic;

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
import testframework.models.TriangleSidesModel;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsEqual.equalToObject;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static testframework.app.App.*;

@DisplayName("Create triangles with different separators")
public class CreateTrianglesWIthDifferentSeparators {

    private static final int FIRST_SIDE = 2;
    private static final int SECOND_SIDE = 3;
    private static final int THIRD_SIDE = 4;

    private String id;

    @RegisterExtension
    public App app = createApp();

    public static Stream<Arguments> payloads() {
        return Stream.of(
                arguments(new ObjectMapper().createObjectNode()
                        .put(SEPARATOR_FIELD, "")
                        .put(INPUT_FIELD, String.format("%s%s%s", FIRST_SIDE, SECOND_SIDE, THIRD_SIDE))),
                arguments(new ObjectMapper().createObjectNode()
                        .put(SEPARATOR_FIELD, "-----")
                        .put(INPUT_FIELD, String.format("%s-----%s-----%s", FIRST_SIDE, SECOND_SIDE, THIRD_SIDE)))
        );
    }

    @ParameterizedTest(name = "Create triangle with different separators")
    @Description("Should create triangles with different valid separators.")
    @MethodSource("payloads")
    public void shouldCreateTriangle(ObjectNode payload) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Response createResponse = app.authJsonRequest()
                .body(payload)
                .post(createPath());

        TriangleSidesModel actualCreatePayload = objectMapper.readValue(createResponse.body().asString(),
                TriangleSidesModel.class);

        id = createResponse.body().jsonPath().getString("id");
        TriangleSidesModel expectedPayload = new TriangleSidesModel()
                .id(id)
                .firstSide(FIRST_SIDE)
                .secondSide(SECOND_SIDE)
                .thirdSide(THIRD_SIDE);

        assertThat(String.format("Create triangle request returns incorrect status code:\n%s",
                createResponse.then().log().all()),
                createResponse.statusCode(), equalTo(200));
        assertThat("Add triangle request returns incorrect payload",
                actualCreatePayload, equalToObject(expectedPayload));
    }

    @AfterEach
    public void cleanData() {
        app.authJsonRequest().delete(deletePath(id));
    }
}
