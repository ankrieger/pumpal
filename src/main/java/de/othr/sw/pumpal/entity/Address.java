package de.othr.sw.pumpal.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Embeddable
public class Address {
    @Size(min = 2, max = 100, message = "Street must be between 2 and 100 characters long")
    private String street;

    @Size(min = 1, max = 10, message = "Street number must be between 2 and 10 characters long")
    private String streetNumber;

    @Pattern(regexp = "(^[0-9]+$|^$)", message = "Only numericals (0-9) are permitted")
    @Size(min = 5, max = 10, message = "ZIP code must be between 5 and 10 characters long")
    private String zip;

    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters long")
    private String city;

    public Address() {

    }

    public Address(String street, String streetNumber, String zip, String city) {
        this.street = street;
        this.streetNumber = streetNumber;
        this.zip = zip;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}



