package hellojpa;

import jakarta.persistence.Embeddable;

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

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
