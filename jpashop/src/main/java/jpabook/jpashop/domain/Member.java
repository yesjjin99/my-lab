package jpabook.jpashop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded  // 내장 타입을 포함
    private Address address;

    // 이 연관관계의 주인은 Member 가 아닌 Order
    // mappedBy가 있으면 내가 매핑을 하는 애가 아니고 주인으로부터 매핑된 거울일 뿐이다라는 표현 -> 읽기 전용
    // mappedBy를 통해 Order 테이블의 member 필드에 의해 매핑된 것이라고 지정해준다
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
