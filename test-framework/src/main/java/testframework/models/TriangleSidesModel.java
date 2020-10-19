package testframework.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
public class TriangleSidesModel {

    @JsonProperty
    private String id;
    @JsonProperty
    private double firstSide;
    @JsonProperty
    private double secondSide;
    @JsonProperty
    private double thirdSide;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriangleSidesModel that = (TriangleSidesModel) o;
        return Double.compare(that.firstSide, firstSide) == 0 &&
                Double.compare(that.secondSide, secondSide) == 0 &&
                Double.compare(that.thirdSide, thirdSide) == 0 &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstSide, secondSide, thirdSide);
    }

    @Override
    public String toString() {
        return "TriangleSidesModel{" +
                "id='" + id + '\'' +
                ", firstSide=" + firstSide +
                ", secondSide=" + secondSide +
                ", thirdSide=" + thirdSide +
                '}';
    }
}
