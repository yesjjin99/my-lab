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
            member.setName("member1");
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.close();
            /*
            위의 Team, Member 객체는 생성과 함께 순수 객체 상태로 영속성 컨텍스트(1차 캐시)에 올라간다
            1. 만약 em.flush(), em.close() 를 중간에 해준다면 DB에 반영하고 1차 캐시를 지우게 된다
               -> 이후 Team 을 조회할 때 1차 캐시에 존재하지 않으므로 DB에 조회 쿼리를 날리게 되고, JPA 가 매핑된 값까지 모두 세팅해서 Team 을 반환해주게 된다
               -> team.getMembers() 했을 때 값이 조회된다
            2. em.flush(), em.close() 를 중간에 해주지 않고 그대로 진행한다면 1차 캐시에 현재 Team과 Member 가 남아있는 상태
               -> Team 을 조회하게 되면 1차 캐시에서 값을 가져오게 된다
               -> 1차 캐시에 저장되어 있는 값은 아직 DB에 반영되지 않은 순수 객체 상태이므로 team.getMembers() 를 해도 아무런 값을 가져오지 못한다

               => 따라서 양방향 연관관계 매핑 시 순수한 객체 관계를 고려하여 항상 양쪽 다 값을 입력해야 한다
             */

            Member findMember = em.find(Member.class, member.getId());  // 1차 캐시
            List<Member> members = findMember.getTeam().getMembers();

            tx.commit();  // 트랜잭션 커밋
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
