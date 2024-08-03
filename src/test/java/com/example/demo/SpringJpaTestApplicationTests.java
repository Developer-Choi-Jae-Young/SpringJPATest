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

		try {
			Member member = new Member();
			member.setId(1L);
			member.setName("HelloA");
			em.persist(member);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestFind() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member findMember = em.find(Member.class, 1L);
			System.out.println("findMember.getId() = " + findMember.getId());
			System.out.println("findMember.getName() = " + findMember.getName());

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestUpdate() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member findMember = em.find(Member.class, 1L);
			findMember.setName("HelloJPA");

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestPersistance() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//비영속 상태
			Member member = new Member();
			member.setId(2L);
			member.setName("HelloB");

			//영속 상태
			System.out.println("=====BEFORE======");
			em.persist(member);
			//로그를 확인하면 persist 한다고 쿼리문이 날라가는 것이 아닌 commit을 해야 쿼리문이 날라가는것이 확인이 됨.
			//그럼 persist는 무엇을 하는 것인가???
            /*
			사실 JPA는 해당 객체를 바로 DB에 저장하는것이 아닌 사이에 영속성 컨텍스트라는 1차 캐시를 두고 작동을 하게된다.
			즉, persist는 영속성 컨텍스트 1차 캐시라는 곳에 영속 화시키는 것을 말하며, 해당 캐시에 기준으로 Commit하여 DB에 반영한다.

			[JPA] -> [영속성 컨텍스트] -> [DB]
			 */
			System.out.println("=====AFTER======");

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestPersistance2() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//비영속 상태
			Member member = new Member();
			member.setId(3L);
			member.setName("HelloC");

			//영속 상태
			System.out.println("=====BEFORE======");
			em.persist(member);
			System.out.println("=====AFTER======");

			//실행시 Select 쿼리문이 동작을 안했는데도 불구하고 정상 조회가됨.
			//이유는 TestPersistance에 설명한것처럼 JPA특성 때문임.
			Member findMember = em.find(Member.class, 3L);
			System.out.println("findMember = " + findMember);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestPersistance3() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//TestPersistance2를 실행 했다는 가정하에 테스트

            /*
			아래 해당 코드가 실행시 select 쿼리문이 1회만 수행이됨.
			이유는 현재 코드는 영속성 컨텍스트가 새로 만들어 져있어서 1차캐시에 아무것도 없는상태에서
			id가 3인 Member객체를 가져올때 캐시에 아무것도 없으니, DB에 select쿼리 요청을하여 가져오고,
			두번째는 이미 한번 가져와서 1차 캐시에 데이터가 존재 하기때문에 DB까지 요청을 안하고 1차 캐시에서
			데이터를 가져오기 때문
			 */
			Member findMember1 = em.find(Member.class, 3L);
			Member findMember2 = em.find(Member.class, 3L);

			//같은 객체이다. 즉 싱글톤을 유지
			System.out.println("findMember = " + findMember1);
			System.out.println("findMember = " + findMember2);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestUpdate2() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member findMember = em.find(Member.class, 3L);
			findMember.setName("HelloJPA");

			//??? 1차 캐시에 바꾼 데이터를 넣지 않아도 update 쿼리문이 수행이됨. 이게 어떻게 되는것일까?
			//em.persist(findMember);

			System.out.println("====================");

			/*
			JPA는 commit을 하는 순간 1차캐시 데이터와 현재 바뀐 객체의 데이터와 비교했을 때 다르면 update 쿼리문을 수행해준다.
			 */
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
}
