package hellojpa;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable  // 값 타입 정의
public class Address {

    private String city;
    private String street;
    private String zipcode;

    public Address() {  // 임베디드 타입을 사용하기 위해서는 기본 생성자 필수
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }

    @Override
    public boolean equals(Object o) {  // 값 타입의 비교를 위해 equals()를 오버라이딩해야 함
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) &&
            Objects.equals(getStreet(), address.getStreet()) &&
            Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
