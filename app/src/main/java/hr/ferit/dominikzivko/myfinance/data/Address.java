package hr.ferit.dominikzivko.myfinance.data;

import androidx.room.Ignore;

import java.util.Objects;

public class Address {
    private String street;
    private String streetNumber;
    private String city;
    private String zipcode;

    @Ignore
    public Address() {
    }

    public Address(String street, String streetNumber, String city, String zipcode) {
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.zipcode = zipcode;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(streetNumber, address.streetNumber) &&
                Objects.equals(city, address.city) &&
                Objects.equals(zipcode, address.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, streetNumber, city, zipcode);
    }
}
