package limits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import testframework.app.App;
import testframework.models.ErrorModel;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsEqual.equalToObject;
import static org.hamcrest.core.IsNot.not;
import static testframework.app.App.*;
import static testframework.consts.ErrorPayloads.limitExceeded;
import static testframework.utils.Utils.*;

@DisplayName("Limits of adding triangles")
public class CreationLimits {

    public static final int MAX_TRIANGLES = 10;

    @RegisterExtension
    public App app = createApp();

    @BeforeEach
    public void cleanAllTriangles() {
        deleteAllTriangles();
    }

    @Test
    @DisplayName("Create maximum number of triangles")
    @Description("Should correctly create maximum number of triangles if limit is not exceeded.")
    public void shouldCreateMaxCount() {
        List<String> createdIds = createBulkTrianglesWithSides(2, 3, 4, MAX_TRIANGLES);

        assertThat("Incorrect number of triangles created when try to create maximum number",
                createdIds.size(), equalTo(MAX_TRIANGLES));

        Response response = app.authJsonRequest()
                .get(getAllPath());
        assertThat(String.format("Get all triangles request returns incorrect status code" +
                        " when maximum number of triangles created\n%s", response.then().log().all()),
                response.statusCode(), equalTo(200));

        List<String> actualIds = getIds(response);

        assertThat("Ids of created triangles aren't match expected",
                actualIds, containsInAnyOrder(createdIds.toArray(new String[] {})));
        assertThat("Number of created triangles isn't match expected", actualIds.size(), equalTo(MAX_TRIANGLES));
    }

    @Test
    @DisplayName("Exceed maximum number of triangles")
    @Description("Should get error message when try to exceed maximum number of triangles.")
    public void errorIfExceedMaxCount() throws JsonProcessingException {
        createBulkTrianglesWithSides(2, 3, 4, MAX_TRIANGLES);

        Response response = app.authJsonRequest()
                .body(new ObjectMapper().createObjectNode()
                        .put(INPUT_FIELD, String.format("%s;%s;%s", 2, 3, 4)))
                .post(createPath());

        assertThat(String.format("Adding triangle exceeding max number returns non-error status code\n%s",
                response.then().log().all()),
                response.statusCode(), not(equalTo(200)));

        ErrorModel actualPayload = new ObjectMapper().readValue(response.body().asString(), ErrorModel.class);

        assertThat(String.format(":\n%s",
                response.then().log().all()),
                actualPayload, equalToObject(limitExceeded().path("/triangle")));
    }

    @AfterEach
    public void cleanData() {
        deleteAllTriangles();
    }
}
