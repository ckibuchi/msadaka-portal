package msadaka.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="adresses")
public class Adress implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    public String state;
    public String country;
    public String box;
    public String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Adress{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", box='" + box + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
