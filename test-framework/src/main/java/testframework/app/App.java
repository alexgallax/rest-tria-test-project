package testframework.app;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.extension.TestWatcher;

public class App implements TestWatcher {

    public static final String INPUT_FIELD = "input";
    public static final String SEPARATOR_FIELD = "separator";

    private static final String TRIANGLE_PATH = "triangle";

    private static PropertiesHolder props;

    @Getter
    private final RequestSpecification nonAuthRequest;
    @Getter
    private final RequestSpecification authRequest;
    @Getter
    private final RequestSpecification authJsonRequest;

    private App() {
        nonAuthRequest = RestAssured.given();
        authRequest = RestAssured.given()
                .header("X-User", props().token());
        authJsonRequest = RestAssured.given()
                .header("X-User", props().token())
                .contentType(ContentType.JSON);
    }

    public static App createApp() {
        return new App();
    }

    public static String getPath(String id) {
        return String.format("%s/%s/%s", props().baseUrl(), TRIANGLE_PATH, id);
    }

    public static String getAllPath() {
        return String.format("%s/%s/all", props().baseUrl(), TRIANGLE_PATH);
    }

    public static String getPerimeterPath(String id) {
        return String.format("%s/%s/%s/perimeter", props().baseUrl(), TRIANGLE_PATH, id);
    }

    public static String getAreaPath(String id) {
        return String.format("%s/%s/%s/area", props().baseUrl(), TRIANGLE_PATH, id);
    }

    public static String createPath() {
        return String.format("%s/%s", props().baseUrl(), TRIANGLE_PATH);
    }

    public static String deletePath(String id) {
        return String.format("%s/%s/%s", props().baseUrl(), TRIANGLE_PATH, id);
    }

    public static PropertiesHolder props() {
        if (props == null) {
            props = ConfigFactory.create(PropertiesHolder.class);
        }
        return props;
    }
}
