package jpashop.jpabook.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {

    @Column(length = 10)  // 값 타입의 장점은 이러한 validation 룰들을 공통으로 관리할 수 있다
    private String city;
    private String street;
    private String zipcode;

    public String fullAddress() {  // 값 타입의 장점은 의미있는 비즈니스 메서드를 추가할 수 있다는 것
        return getCity() + " " + getStreet() + " " + getZipcode();
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

    private void setCity(String city) {
        this.city = city;
    }

    private void setStreet(String street) {
        this.street = street;
    }

    private void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public boolean equals(Object o) {  // JPA에서는 getter로 접근해야 프록시일 때도 프록시 객체가 진짜 객체한테 전달되는 등 프록시를 고려하여 equals()와 hashCode()를 getter 를 사용해서 구현하는 게 좋다
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(
            getStreet(), address.getStreet()) && Objects.equals(getZipcode(),
            address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
