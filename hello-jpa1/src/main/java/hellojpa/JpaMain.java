package hellojpa;

import jakarta.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        /* 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유 */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // Persistence 클래스가 persistence.xml 설정정보를 읽어와 EntityManagerFactory 생성
        /* 엔티티 매니저는 요청이 올 때마다 생성하고 사용한 뒤 해당 요청이 끝나면 종료 (쓰레드 간 공유X) */
        EntityManager em = emf.createEntityManager();  // EntityManagerFactory 가 EntityManager 생성

        /* JPA의 모든 데이터 변경은 트랜잭션 안에서 실행 */
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 1. 저장
            Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");  // --> 비영속 상태

            em.persist(member);  // --> 영속 상태

            // 2. 조회
            Member member1 = em.find(Member.class, 1L);  // PK 조회

            List findMembers = em.createQuery("select m from Member m", Member.class)  // JPQL
                .getResultList();

            // 3. 수정
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");  // JPA가 트랜잭션 내에서 트랜잭션 시점에 변경 감지를 해서 update 쿼리를 날린다 -> 트랜잭션 커밋

            // 4. 삭제
            em.remove(findMember);

            tx.commit();  // 트랜잭션 커밋
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
