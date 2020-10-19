package basic;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import testframework.app.App;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static testframework.app.App.*;
import static testframework.utils.Utils.createTriangleWithSides;

@DisplayName("Delete triangle")
public class DeleteTriangles {

    private static final int FIRST_SIDE = 2;
    private static final int SECOND_SIDE = 3;
    private static final int THIRD_SIDE = 4;

    private String id;

    @RegisterExtension
    public App app = createApp();

    @BeforeEach
    public void createTestTriangle() {
        id = createTriangleWithSides(FIRST_SIDE, SECOND_SIDE, THIRD_SIDE);
    }

    @Test
    @DisplayName("Delete triangle by id")
    @Description("Should delete triangle by id.")
    public void shouldDeleteTriangle() {
        Response deleteResponse = app.authJsonRequest().delete(deletePath(id));

        assertThat(String.format("Delete triangle request returns incorrect status code:\n%s",
                deleteResponse.then().log().all()),
                deleteResponse.statusCode(), equalTo(200));

        Response getResponse = app.authJsonRequest().get(getPath(id));

        assertThat(String.format("Deleted triangle data is returned by get request\n%s", getResponse.then().log().all()),
                getResponse.statusCode(), equalTo(404));
    }

    @AfterEach
    public void cleanData() {
        app.authJsonRequest().delete(deletePath(id));
    }
}
