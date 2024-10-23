package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired EntityManager em;

    @Test
    public void updateTest() throws Exception {
        Book book = em.find(Book.class, 1L);

        // 트랜잭션
        book.setName("adddfsds");

        // 변경 감지 == dirty checking (트랜잭션 커밋 시점에 변경 감지를 통해 업데이트 쿼리를 날리고 트랜잭션 커밋이 일어난다)
        // 트랜잭션 커밋
    }
}
