package com.packt.cardatabase.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Owner {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long ownerid;
    private String firstname, lastname;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="owner")
    @JsonIgnore
    private List<Car> cars;

    public Owner() {}
    public Owner(String firstname, String lastname) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
    }
    public long getOwnerid() {
        return ownerid;
    }
    public void setOwnerid(long ownerid) {
        this.ownerid = ownerid;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public List<Car> getCars() {
        return cars;
    }
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}
