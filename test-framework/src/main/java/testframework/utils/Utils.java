package testframework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import testframework.app.App;
import testframework.models.TriangleSidesModel;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static testframework.app.App.*;

public class Utils {

    public static List<String> getIds(Response response) {
        return response.body().jsonPath().getList("", TriangleSidesModel.class)
                .stream().map(TriangleSidesModel::id).collect(toList());
    }

    public static float countTrianglePerimeter(double firstSide, double secondSide, double thirdSide) {
        return (float) (firstSide + secondSide + thirdSide);
    }

    public static float countTriangleArea(double firstSide, double secondSide, double thirdSide) {
        double hp = (firstSide + secondSide + thirdSide) / 2.0;
        return (float) Math.sqrt(hp * (hp - firstSide) * (hp - secondSide) * (hp - thirdSide));
    }

    public static String createTriangleWithSides(int firstSide, int secondSide, int thirdSide) {
        return createTriangleWithSides(new ObjectMapper().createObjectNode()
                .put(INPUT_FIELD, String.format("%s;%s;%s", firstSide, secondSide, thirdSide)));
    }

    public static String createTriangleWithSides(double firstSide, double secondSide, double thirdSide) {
        return createTriangleWithSides(new ObjectMapper().createObjectNode()
                .put(INPUT_FIELD, String.format("%s;%s;%s", firstSide, secondSide, thirdSide)));
    }

    private static String createTriangleWithSides(ObjectNode payload) {
        App app = createApp();
        Response response = app.authJsonRequest()
                .body(payload)
                .post(createPath());
        response.then()
                .statusCode(200);

        return response.body().jsonPath().getString("id");
    }

    public static List<String> createBulkTrianglesWithSides(int firstSide, int secondSide, int thirdSide, int count) {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(createTriangleWithSides(firstSide, secondSide, thirdSide));
        }
        return ids;
    }

    public static List<String> createBulkTrianglesWithSides(double firstSide, double secondSide, double thirdSide, int count) {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(createTriangleWithSides(firstSide, secondSide, thirdSide));
        }
        return ids;
    }

    public static void deleteAllTriangles() {
        App app = createApp();
        Response response = app.authJsonRequest().get(getAllPath());
        List<String> ids = getIds(response);
        ids.forEach(id -> app.authJsonRequest().delete(deletePath(id)));
    }
}
