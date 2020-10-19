package limits;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import testframework.app.App;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static testframework.app.App.createApp;
import static testframework.app.App.getAllPath;
import static testframework.utils.Utils.*;

@DisplayName("Get all triangles")
public class GetAllTriangles {

    @RegisterExtension
    public App app = createApp();

    @BeforeEach
    public void cleanAllTriangles() {
        deleteAllTriangles();
    }

    @Test
    @DisplayName("Get all triangles if 1 exists")
    @Description("Get all should return correct number of triangles if 1 exists.")
    public void shouldGetAllIfOneExists() {
        String id = createTriangleWithSides(2, 3, 4);

        Response response = app.authJsonRequest()
                .get(getAllPath());
        assertThat(String.format("Get all triangles request returns incorrect status code when 1 triangle exists\n%s",
                response.then().log().all()),
                response.statusCode(), equalTo(200));

        List<String> actualIds = getIds(response);

        assertThat("Ids of fetched triangles aren't match expected", actualIds, containsInAnyOrder(id));
        assertThat("Number of fetched triangles isn't match expected", actualIds.size(), equalTo(1));
    }

    @Test
    @DisplayName("Get all triangles if more than 1 exist")
    @Description("Get all should return correct number of triangles if more than 1 exist but not exceed limit.")
    public void shouldGetAllIfSeveralExist() {
        List<String> ids = createBulkTrianglesWithSides(2, 3, 4, 3);

        Response response = app.authJsonRequest()
                .get(getAllPath());
        assertThat(String.format("Get all triangles request returns incorrect status code when 3 triangle exist\n%s",
                response.then().log().all()),
                response.statusCode(), equalTo(200));

        List<String> actualIds = getIds(response);

        assertThat("Ids of fetched triangles aren't match expected",
                actualIds, containsInAnyOrder(ids.toArray(new String[] {})));
        assertThat("Number of fetched triangles isn't match expected", actualIds.size(), equalTo(3));
    }

    @Test
    @DisplayName("Get all triangles if non exists")
    @Description("Get all should return empty array of no triangles exist.")
    public void should0() {
        Response response = app.authJsonRequest()
                .get(getAllPath());

        assertThat(String.format("Get all triangles request returns incorrect status code when no triangles exist\n%s",
                response.then().log().all()),
                response.statusCode(), equalTo(200));
        assertThat(String.format("Arrays of fetched triangles isn't empty\n%s", response.then().log().all()),
                response.body().jsonPath().getList(""), hasSize(0));
    }

    @AfterEach
    public void cleanData() {
        deleteAllTriangles();
    }
}
