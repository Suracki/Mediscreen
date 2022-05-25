package com.abernathy.mediscreen.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name="patient")
public class Patient implements DomainElement {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int patientId;
    @NotEmpty(message = "Family Name is mandatory")
    private String familyName;
    @NotEmpty(message = "Given Name is mandatory")
    private String givenName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    @Pattern(regexp = "^[FM]|[fm]$", message="Sex must be entered as either 'M' or 'F'")
    private String sex;
    @NotEmpty(message = "Address is mandatory")
    private String address;
    @Pattern(regexp = "^\\d\\d\\d-{1}\\d\\d\\d-{1}\\d\\d\\d\\d$",
    message="Phone must be entered in the format 123-456-7890")
    private String phone;

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "{\"patient\":{\"patientId\": \"" + patientId +
                "\", \"familyName\": \"" + familyName +
                "\", \"givenName\": \"" + givenName +
                "\", \"dob\": \"" + dob +
                "\", \"sex\": \"" + sex +
                "\", \"address\": \"" + address +
                "\", \"phone\": \"" + phone +"\"}}";
    }

    public void setId(Integer id) {
        this.patientId = id;
    }
}
