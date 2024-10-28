package jpashop.jpabook;

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
            tx.commit();  // 트랜잭션 커밋
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
