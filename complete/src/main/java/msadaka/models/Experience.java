package msadaka.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="experiences")
public class Experience implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public String startYear;
    public String endYear;
    public String position;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Experience{" +
                "id=" + id +
                ", startYear='" + startYear + '\'' +
                ", endYear='" + endYear + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
