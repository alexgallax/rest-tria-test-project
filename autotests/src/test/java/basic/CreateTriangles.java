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
import testframework.consts.Consts;
import testframework.models.TriangleSidesModel;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsEqual.equalToObject;
import static testframework.app.App.*;

@DisplayName("Create and get triangles with different sides lengths")
public class CreateTriangles {

    private String id;

    @RegisterExtension
    public App app = createApp();

    private static Stream<Arguments> triangleSides() {
        return Consts.triangleSides();
    }

    @ParameterizedTest(name = "Create and get triangle with sides [\"{0}\", \"{1}\", \"{2}\"] by id")
    @Description("Should create and get sides of valid triangle.")
    @MethodSource("triangleSides")
    public void shouldCreateAndGetCorrectIdAndSides(double firstSide, double secondSide, double thirdSide) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode payload = objectMapper.createObjectNode()
                .put(INPUT_FIELD, String.format("%s;%s;%s", firstSide, secondSide, thirdSide));

        Response createResponse = app.authJsonRequest()
                .body(payload)
                .post(createPath());

        TriangleSidesModel actualCreatePayload = objectMapper.readValue(createResponse.body().asString(),
                TriangleSidesModel.class);
        assertThat(String.format("Add triangle request doesn't return id\n%s", createResponse.then().log().all()),
                actualCreatePayload.id(), notNullValue());

        id = createResponse.body().jsonPath().getString("id");
        TriangleSidesModel expectedPayload = new TriangleSidesModel()
                .id(id)
                .firstSide(firstSide)
                .secondSide(secondSide)
                .thirdSide(thirdSide);

        assertThat(String.format("Add triangle request returns incorrect payload\n%s", createResponse.then().log().all()),
                actualCreatePayload, equalToObject(expectedPayload));

        Response getResponse = app.authJsonRequest().get(getPath(id));

        assertThat(String.format("Get triangle request returns incorrect status code\n%s", getResponse.then().log().all()),
                getResponse.statusCode(), equalTo(200));

        TriangleSidesModel actualGetPayload = objectMapper.readValue(getResponse.body().asString(),
                TriangleSidesModel.class);
        assertThat(String.format("Add triangle request returns incorrect payload\n%s", getResponse.then().log().all()),
                actualGetPayload, equalToObject(expectedPayload));
    }

    @AfterEach
    public void cleanData() {
        app.authJsonRequest().delete(deletePath(id));
    }
}
