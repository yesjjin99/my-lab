package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

@Repository  // 컴포넌트 스캔의 대상이 되어 스프링 빈으로 등록
public class MemberRepository {

    @PersistenceContext  // 스프링이 생성한 JPA의 EntityManager 를 주입
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);  // persist 를 통해 영속성 컨텍스트에 멤버 객체를 넣고 이후 트랜잭션이 commit 되는 시점에 DB에 반영된다(insert query)
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);  // JPA의 find(type, pk) 메서드는 단건조회
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)  // JPQL : SQL은 테이블을 대상으로 쿼리를 날리는 반면, JPQL은 엔티티(객체)를 대상으로 쿼리를 날린다
            .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
            .setParameter("name", name)  // 파라미터 바인딩
            .getResultList();
    }
}
