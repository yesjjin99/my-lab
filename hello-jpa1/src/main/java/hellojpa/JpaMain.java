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
            /*
            // 1. 저장
            Member member = new Member();
            member.setId(1L);
            member.setUsername("HelloA");  // --> 비영속 상태

            em.persist(member);  // --> 영속 상태

            // 2. 조회
            Member member1 = em.find(Member.class, 1L);  // PK 조회

            List findMembers = em.createQuery("select m from Member m", Member.class)  // JPQL
                .getResultList();

            // 3. 수정
            Member findMember = em.find(Member.class, 1L);
            findMember.setUsername("HelloJPA");  // JPA가 트랜잭션 내에서 트랜잭션 시점에 변경 감지를 해서 update 쿼리를 날린다 -> 트랜잭션 커밋

            // 4. 삭제
            em.remove(findMember);
            */

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member");
            member.setHomeAddress(new Address("city", "street", "10000"));

            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member m = em.find(Member.class, member.getId());

            System.out.println("m.getTeam() = " + m.getTeam().getClass());  // 프록시

            System.out.println("==============");
            m.getTeam().getName();  // 지연로딩 -> 실제 Team 의 속성을 사용하는 시점에 프록시 객체가 초기화되면서 DB에서 값을 가지고 온다
            System.out.println("==============");

            // ----

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);  // CASCADE 에 의해 연관된 child1, child2 까지 persist 됨

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0);  // 컬렉션에서 제거되면 연관관계가 끊어진 자식 엔티티가 되므로 orphanRemoval 옵션에 의해 delete 쿼리를 날림

            tx.commit();  // 트랜잭션 커밋
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
