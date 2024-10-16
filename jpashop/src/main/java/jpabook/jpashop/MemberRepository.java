package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    /*
    - JPA 를 사용하려면 EntityManger 를 주입받아야 한다
    - @PersistenceContext 있으면 스프링부트가 EntityManger 를 주입해준다
     */
    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
