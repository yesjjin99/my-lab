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
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(20);
            em.persist(member);

            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            List<Member> resultList = query1.getResultList();

            TypedQuery<Member> query2 = em.createQuery("select m from Member m where m.id = 1L", Member.class);
            Member result = query2.getSingleResult();

            Member result1 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")  // 파라미터 바인딩
                .getSingleResult();

            // ---

            Member member2 = new Member();
            member.setUsername("member2");
            member.setAge(20);
            em.persist(member);

            em.flush();
            em.close();

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

            tx.commit();  // 트랜잭션 커밋
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
