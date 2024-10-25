package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // Persistence 클래스가 persistence.xml 설정정보를 읽어와 EntityManagerFactory 생성
        EntityManager em = emf.createEntityManager();  // EntityManagerFactory 가 EntityManager 생성
        //code

        em.close();
        emf.close();
    }
}
