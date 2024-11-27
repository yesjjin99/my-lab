package jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;
import jpql.dto.MemberDTO;

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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(20);

            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            List<Member> resultList = query1.getResultList();

            TypedQuery<Member> query2 = em.createQuery("select m from Member m where m.id = 1L", Member.class);
            Member result = query2.getSingleResult();

            Member result1 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")  // 파라미터 바인딩
                .getSingleResult();

            // ---

            /* 엔티티 프로젝션을 하면 -> 조회한 값들이 모두 영속성 컨텍스트에서 관리된다 */
            List<Member> result2 = em.createQuery("select m from Member m", Member.class)
                .getResultList();

            Member findMember = result2.get(0);
            findMember.setAge(30);  // dirty checking 발생 -> 데이터 정상적으로 반영된다


            List<MemberDTO> result3 = em.createQuery(
                    "select new jpql.dto.MemberDTO(m.username, m.age) from Member m",
                    MemberDTO.class)  // 엔티티 프로젝션이 아닌 여러 값을 프로젝션하는 경우에는 DTO로 조회하기 위해 new 연산을 사용해야 한다
                .getResultList();

            MemberDTO memberDTO = result3.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());

            // ----

            List<Member> result4 = em.createQuery("select m from Member m order by m.age desc", Member.class)
                .setFirstResult(0)  // 페이징 API : 조회 시작 위치
                .setMaxResults(10)  // 페이징 API : 조회할 데이터 수
                .getResultList();

            // ----

            List<Member> result5 = em.createQuery("select m from Member m join m.team t", Member.class)
                .getResultList();

            em.createQuery("select m from Member m join m.team t on t.name = :teamName", Member.class)  // JOIN - ON절 : 조인 대상 필터링, 연관관계 없는 엔티티 외부 조인
                .setParameter("teamName", "teamA")
                .getResultList();

            // ----

            /* 서브 쿼리 - select, where, having, from (하이버네이트6부터) 절에서 모두 사용 가능 */
            em.createQuery("select m from Member m where exists (select t from m.team t where t.name = :teamName)", Member.class)  // 서브 쿼리 - exists (서브 쿼리에 결과가 존재하면 참)
                .setParameter("teamName", "teamA")
                .getResultList();

            em.createQuery("select o from Order o where o.orderAmount > all (select p.stockAmount from Product p)", Order.class)  // 서브 쿼리 - all (모두 만족하면 참)
                .getResultList();

            em.createQuery("select m from Member m where m.team = any (select t from Team t)", Member.class)  // 서브 쿼리 - any (조건을 하나라도 만족하면 참)
                .getResultList();

            tx.commit();  // 트랜잭션 커밋
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
