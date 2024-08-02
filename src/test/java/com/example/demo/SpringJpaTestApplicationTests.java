package com.example.demo;

import com.example.demo.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringJpaTestApplicationTests {
	@Test
	void contextLoads() {
	}

	@Test
	void Test() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		Member member = new Member();
		member.setId(1L);
		member.setName("HelloA");
		em.persist(member);

		tx.commit();
		em.close();
		emf.close();
	}

}
