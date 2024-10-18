package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {  // JPA 저장하기 전까지는 id 값이 없음 -> 즉, id 값이 없으면 새로 생성한 객체라는 의미
            em.persist(item);  // persist 호출
        } else {
            em.merge(item);  // update 와 비슷 (강제로 업데이트)
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
            .getResultList();
    }
}
