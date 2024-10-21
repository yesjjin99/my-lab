package jpabook.jpashop.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // createOrder() 메서드를 사용할 것이기 때문에 외부에서 생성자를 사용하여 객체를 만들지 못하도록 하기 위함
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")  // 매핑을 무엇으로 할지 외래키 이름 지정
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // CascadeType.ALL -> Order 엔티티를 persist 할 때 딜리버리 엔티티도 함께 persist 해준다
    @JoinColumn(name = "delivery_id")  // 일대일 관계에서는 외래키를 어디에 두어도 상관없지만, 주로 액세스를 많이 하는 곳에서 외래키를 지정하면 더 편하다
    private Delivery delivery;

    private LocalDateTime orderDate;  // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;  // 주문 상태 [ORDER, CANCEL]

    /**
     * == 연관관계 메서드 ==
     * 연관관계 매핑이 되어있다면 DB에 저장할 때는 문제없지만, 순수 자바 코드에서 객체끼리 연결되지는 않았기 때문에 객체 탐색을 하는 로직을 위해 상호간의 참조를 넣어주어야 한다
     * 외래키를 관리하는 연관관계의 주인에서만 관계를 맺어주어도 정상적으로 양쪽 모두 조회가 된다
     * 하지만 JPA는 ORM 방식이므로, 객체까지 고려하여 양쪽의 관계를 다 맺어주어야 좋다
     * -> 객체의 양방향 연관관계는 양쪽 엔티티 모두 관계를 맺어주어야 순수한 자바 객체 상태에서도 정상적으로 동작한다
     * => 양방향에서는 연관관계 편의 메서드가 있는 것이 좋다!
     */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);  // 반대로 양방향도 저장
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // == 생성 메서드 (정적 팩토리 메서드) == //
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // == 비즈니스 로직 == //
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // == 조회 로직 == //
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        return orderItems.stream()
            .mapToInt(OrderItem::getTotalPrice)
            .sum();
    }
}
