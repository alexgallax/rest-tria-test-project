package testframework.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
public class ErrorModel {

    @JsonProperty
    private double timestamp;
    @JsonProperty
    private int status;
    @JsonProperty
    private String error;
    @JsonProperty
    private String exception;
    @JsonProperty
    private String message;
    @JsonProperty
    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorModel that = (ErrorModel) o;
        return status == that.status &&
                Objects.equals(error, that.error) &&
                Objects.equals(exception, that.exception) &&
                Objects.equals(message, that.message) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, error, exception, message, path);
    }

    @Override
    public String toString() {
        return "ErrorModel{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", exception='" + exception + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
