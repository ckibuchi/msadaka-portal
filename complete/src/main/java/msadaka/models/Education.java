package msadaka.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="education")
public class Education implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    public String startYear;
    public String endYear;
    public String course;

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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Education{" +
                "id=" + id +
                ", startYear='" + startYear + '\'' +
                ", endYear='" + endYear + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}

