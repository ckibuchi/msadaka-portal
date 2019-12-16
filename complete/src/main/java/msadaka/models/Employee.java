package msadaka.models;

import com.sun.corba.se.spi.ior.Identifiable;
import msadaka.enums.Gender;
import org.hibernate.annotations.GenericGenerator;
import org.omg.CORBA_2_3.portable.OutputStream;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="employees")
public class Employee implements Serializable,Identifiable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    public String empEmail;

    public String empNationalId;
    public String name;
    public String designation;
    public String phone;
    public String dob;

    public String empNo;

    @Enumerated
    public Gender gender;
    @ManyToOne
    public  Skill skill;
    @ManyToOne
    public Education education;
    @OneToOne
    public User user;
    @ManyToOne
    public Experience experience;





    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpNationalId() {
        return empNationalId;
    }

    public void setEmpNationalId(String empNationalId) {
        this.empNationalId = empNationalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmpNo() {
        return this.empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }


    @Override
    public void write(OutputStream arg0) {

    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", empEmail='" + empEmail + '\'' +
                ", empNationalId='" + empNationalId + '\'' +
                ", name='" + name + '\'' +
                ", designation='" + designation + '\'' +
                ", phone='" + phone + '\'' +
                ", dob='" + dob + '\'' +
                ", empNo='" + empNo + '\'' +
                ", gender=" + gender +
                ", skill=" + skill +
                ", education=" + education +
                ", user=" + user +
                ", experience=" + experience +
                '}';
    }
}
