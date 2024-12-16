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
            member.setUsername("member2");
            member.setAge(20);
            member.setType(MemberType.ADMIN);

            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            List<Member> resultList = query1.getResultList();

            TypedQuery<Member> query2 = em.createQuery("select m from Member m where m.id = 1L", Member.class);
            Member result = query2.getSingleResult();

            Member result1 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member2")  // 파라미터 바인딩
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

            /* 페이징 */
            List<Member> result4 = em.createQuery("select m from Member m order by m.age desc", Member.class)
                .setFirstResult(0)  // 페이징 API : 조회 시작 위치
                .setMaxResults(10)  // 페이징 API : 조회할 데이터 수
                .getResultList();

            // ----

            /* 조인 */
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

            // ----

            /* JPQL 타입 표현 */
            String query3 = "select m.username, 'HELLO', true from Member m "
                            + "where m.type = :userType";
            em.createQuery(query3)
                .setParameter("userType", MemberType.ADMIN)
                .getResultList();

            // ----

            /* 조건식 */
            String query4 = "select "
                + "case when m.age <= 10 then '학생요금'"
                + "     when m.age >= 60 then '경로요금'"
                + "     else '일반요금' "
                + "end "
                + "from Member m";
            em.createQuery(query4, String.class).getResultList();

            em.createQuery("select coalesce(m.username, '이름 없는 회원') from Member m")
                .getResultList();  // m.username 이 null 이면, '이름 없는 회원' 반환

            em.createQuery("select nullif(m.username, '관리자') from Member m")
                .getResultList();  // 사용자 이름이 ‘관리자’면 null 을 반환하고 나머지는 본인의 이름을 반환

            // ----

            /* JPQL 기본 함수 */
            em.createQuery("select 'a' || 'b' from Member m").getResultList();
            em.createQuery("select concat('a', 'b') from Member m").getResultList();

            em.createQuery("select substring(m.username, 2, 3) from Member m").getResultList();

            em.createQuery("select locate('de', 'abcdefg') from Member m").getResultList();  // 앞의 값이 있는 index 반환

            em.createQuery("select size(t.members) from Team t").getResultList();  // 양방향 연관관계 시 컬렉션의 크기 반환

            // ----

            /* 경로 표현식 */
            em.createQuery("select m.id from Member m").getResultList();  // 상태 필드
            em.createQuery("select m.team from Member m").getResultList();  // 단일 값 연관 필드
            em.createQuery("select m.team.name from Member m").getResultList();  // 묵시적 내부 조인(inner join) 발생, 탐색O

            em.createQuery("select t.members from Team t").getResultList();  // 컬렉션 값 연관 필드
            em.createQuery("select t.members.size from Team t").getSingleResult();  // 묵시적 내부 조인 발생, 탐색X
            em.createQuery("select m.username from Team t join t.members m").getResultList();  // FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능


            // ----

            /* fetch join */

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamA.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.changeTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원1");
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.changeTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query5 = "select m from Member m join fetch m.team";  // N + 1 문제를 해결하는 fetch join
            List<Member> result6 = em.createQuery(query5, Member.class).getResultList();

            for (Member m : result6) {
                /*
                이 때는 fetch join 으로 한 번에 team 을 모두 join 해서 가져오기 때문에 프록시를 넣어주는 게 아니다.
                즉, 영속성 컨텍스트에 각 member에 해당하는 team 들이 모두 존재한다.
                이미 객체 그래프를 한 방 쿼리로 모두 DB에서 가져와 1차 캐시에 올려놓은 상태이기 때문이다.
                따라서 추가 N번 쿼리가 나가지 않는다(N + 1 문제 해결)
                 */
                System.out.println("member = " + m.getUsername() + ", " + m.getTeam().getName());
            }

            String query6 = "select t from Team t join fetch t.members";
            List<Team> result7 = em.createQuery(query6, Team.class).getResultList();

            for (Team t : result7) {
                System.out.println("team = " + t.getName() + ", " + t.getMembers().size());
            }

            tx.commit();  // 트랜잭션 커밋
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
