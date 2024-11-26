package jpql;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {

    private String city;
    private String string;
    private String zipcode;

    public Address() {
    }

    public Address(String city, String string, String zipcode) {
        this.city = city;
        this.string = string;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getString() {
        return string;
    }

    public String getZipcode() {
        return zipcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(
            getString(), address.getString()) && Objects.equals(getZipcode(),
            address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getString(), getZipcode());
    }
}
