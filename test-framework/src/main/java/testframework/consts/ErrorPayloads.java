package testframework.consts;

import testframework.models.ErrorModel;

public class ErrorPayloads {

    public static ErrorModel unauthorizedPayload() {
        return new ErrorModel().status(401)
                .error("Unauthorized").message("You are unauthorized to see this data");
    }

    public static ErrorModel notFoundPayload() {
        return new ErrorModel().status(404)
                .error("Not Found").message("Not Found")
                .exception("com.natera.test.triangle.exception.NotFoundException");
    }

    public static ErrorModel unprocessableEntity() {
        return new ErrorModel().status(422)
                .error("Unprocessable Entity").message("Cannot process input")
                .exception("com.natera.test.triangle.exception.UnprocessableDataException");
    }

    public static ErrorModel limitExceeded() {
        return new ErrorModel().status(422)
                .error("Unprocessable Entity").message("Limit exceeded")
                .exception("com.natera.test.triangle.exception.LimitExceededException");
    }

    public static ErrorModel unsupportedMediaTypePayload() {
        return new ErrorModel().status(415)
                .error("Unsupported Media Type").message("Content type 'text/plain;charset=ISO-8859-1' not supported")
                .exception("org.springframework.web.HttpMediaTypeNotSupportedException");
    }

    public static ErrorModel requestBodyMissingPayload() {
        return new ErrorModel().status(400)
                .error("Bad Request").message("Required request body is missing: public com.natera.test.triangle.model.Triangle com.natera.test.triangle.controller.TriangleController.addTriangle(com.natera.test.triangle.model.TriangleInput,javax.servlet.http.HttpServletRequest)")
                .exception("org.springframework.http.converter.HttpMessageNotReadableException");
    }

    public static ErrorModel invalidJsonBodyPayload() {
        return new ErrorModel().status(400)
                .error("Bad Request")
                .exception("org.springframework.http.converter.HttpMessageNotReadableException");
    }
}
