package jpabook.jpashop.domain.item;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  // 상속관계 전략 지정
@DiscriminatorColumn(name = "dtype")  // 싱글 테이블이므로 구분할 수 있는 type 이 있어야 한다 (상속관계 매핑)
@Getter @Setter
public abstract class Item {  // 구현체를 가지고 갈 것이기 때문에 추상 클래스로

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // == 비즈니스 로직 == //
    // 객체 지향적으로 생각했을 때, 데이터를 가지고 있는 곳에 비즈니스 로직이 있는 것이 가장 좋다 (도메인 주도 설계)
    /**
     * 재고 수량 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 수량 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
