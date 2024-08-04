package com.example.demo;

import com.example.demo.entity.MemberTest;
import com.example.demo.jpashop1.entity.Member;
import com.example.demo.jpashop1.entity.Order;
import com.example.demo.jpateam.entity.Member1;
import com.example.demo.jpateam.entity.Team1;
import com.example.demo.jpateam2.entity.Member2;
import com.example.demo.jpateam2.entity.Team2;
import com.example.demo.jpateam3.entity.Member3;
import com.example.demo.jpateam3.entity.Team3;
import com.example.demo.jpateam4.entity.Member4;
import com.example.demo.jpateam4.entity.Team4;
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
			MemberTest memberTest = new MemberTest();
			memberTest.setId(1L);
			memberTest.setName("HelloA");
			em.persist(memberTest);

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
			MemberTest findMemberTest = em.find(MemberTest.class, 1L);
			System.out.println("findMember.getId() = " + findMemberTest.getId());
			System.out.println("findMember.getName() = " + findMemberTest.getName());

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
			MemberTest findMemberTest = em.find(MemberTest.class, 1L);
			findMemberTest.setName("HelloJPA");

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
			MemberTest memberTest = new MemberTest();
			memberTest.setId(2L);
			memberTest.setName("HelloB");

			//영속 상태
			System.out.println("=====BEFORE======");
			em.persist(memberTest);
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
			MemberTest memberTest = new MemberTest();
			memberTest.setId(3L);
			memberTest.setName("HelloC");

			//영속 상태
			System.out.println("=====BEFORE======");
			em.persist(memberTest);
			System.out.println("=====AFTER======");

			//실행시 Select 쿼리문이 동작을 안했는데도 불구하고 정상 조회가됨.
			//이유는 TestPersistance에 설명한것처럼 JPA특성 때문임.
			MemberTest findMemberTest = em.find(MemberTest.class, 3L);
			System.out.println("findMember = " + findMemberTest);

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
			MemberTest findMember1Test = em.find(MemberTest.class, 3L);
			MemberTest findMember2Test = em.find(MemberTest.class, 3L);

			//같은 객체이다. 즉 싱글톤을 유지
			System.out.println("findMember = " + findMember1Test);
			System.out.println("findMember = " + findMember2Test);

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
			MemberTest findMemberTest = em.find(MemberTest.class, 3L);
			findMemberTest.setName("HelloJPA");

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

	@Test
	void TestMapping() {
        /*
		객체와 테이블 맵핑 : @Entity, @Table
		필드와 컬럼 맵핑 : @Column
		기본키 맵핑 : @Id
		연관관계 맵핑 : @ManyToOne, @JoinColumn

		기본키 맵핑에서 IDENTITY전략을 사용할 경우에는 persist하는 시점에 insert 쿼리문이 수행됨.
		이유는 영속성 컨텍스트 1차 캐시에 보통 기본키 값을 요구를 하는데 IDENTITY(AUTO_INCREMENT)는 기본키 값을 DB에서 마지막 COMMIT할때
		값이 할당이 됨. 그래서 예외적으로 IDENTITY 전략은 .persist()하는 시점에 insert 쿼리가 수행됨.
		 */
	}

	@Test
	void TestJPAShop1() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//jpashop1 package 기준은 객체 지향 코드 보다는 테이블 기준으로 코드를 작성하여 아래처럼 매끄럽지 못한 코드가 발생함.
			Order order = em.find(Order.class, 1L);
			Long memberId = order.getMemberId();

			Member member = em.find(Member.class, memberId);

			// 그럼 객체지향 코드로 바꾼다면 아래처럼 코드가 작성 되지 않을까?
            /*
			Order order = em.find(Order.class, 1L);
			Member member = order.getMember();
			 */

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestJPATeam1() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//팀 저장
			Team1 team1 = new Team1();
			team1.setName("TeamA");
			em.persist(team1);

			//회원 저장
			Member1 member1 = new Member1();
			member1.setName("member1");
			member1.setTeamId(team1.getId());
			em.persist(member1);

			//조회
			Member1 findMember1 = em.find(Member1.class, member1.getId());
			//연관관계가 없음
			Team1 findTeam1 = em.find(Team1.class, team1.getId());

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestJPATeam2() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//팀 저장
			Team2 team2 = new Team2();
			team2.setName("TeamA");
			em.persist(team2);

			//회원 저장
			Member2 member2 = new Member2();
			member2.setName("member1");
			member2.setTeam(team2); //단방향 연관관계 설정, 참조 저장
			em.persist(member2);

			//조회
			Member2 findMember2 = em.find(Member2.class, member2.getId());
			//참조를 사용해서 연관관계 조회
			Team2 findTeam2 = findMember2.getTeam();

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestJPATeam3() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//회원 저장
			Member3 member3 = new Member3();
			member3.setName("member1");
			em.persist(member3);

			//팀 저장
			Team3 team3 = new Team3();
			team3.setName("TeamA");
			team3.getMembers().add(member3);
			em.persist(team3);

            /*
			Member Table에 TEAM_ID의 값이 NULL이다. 이유는 양방향 맵핑에서 @XXXToXXX(mappedBy = "XXX")로 주종관계 설정을한 필드는
			쓰기가 불가능한 읽기 전용이므로 불가능하다.
			추가적으로 양방향 맵핑시 mappedBy라는 주종 관계를 설정하는 이유는 아래와 같다.

			객체지향 프로그래밍 JAVA와 DB의 테이블의 구조는 다르다. 예를들어 추상적으로 설계한 같은 무언가(여기서는 Member, Team Class와 Member, Team Table을 뜻함)
			에 대해서 조회를 하고자 한다면, DB에 경우에는 Join이라는 구문이 있기에 Join만 가능하다면 어느 테이블에서 조회 및 쓰기를 하던간에 가능하다.(마치 양방향 맵핑이 된것 처럼)
			하지만 JPA에서는 다르다. 자바는 DB에서 JOIN과 같은 기능을 제공 하는것이 없기에 사실상 서로가 단방향으로 바라는 구조로 마치 양방향인것 처럼 구현 한것이다. (아래 그림과 같음)
			|-----------|						|-----------|
			|			|---------------------->|			|
			|	MEMBER	|						|	Team	|
			|			|<----------------------|			|
			|-----------|						|-----------|
			그럼 여기서 발생하는 문제가 Member 객체의 값의 조작은 어디까지 허용할까? 라는 문제가 발생한다 이유는 Team이라는 객체에서도 Member 클래스를 갖고 있기 때문이다.
			예를들어 Member 클래스에 name이라는 값을 "B"라고 바꾸고 Team 클래스에 해당 Member 객체의 name을 "C"라고 바꾸면 충돌이 일어날것이다. 이를 방지하기 위해 주종관계인
			mappedBy가 나타난것이다.
			 */
			em.flush();
			em.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestJPATeam4() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//팀 저장
			Team3 team3 = new Team3();
			team3.setName("TeamA");
			em.persist(team3);

			//회원 저장
			Member3 member3 = new Member3();
			member3.setName("member1");
			member3.setTeam(team3);
			em.persist(member3);

            /*
			이와 같이 코드를 작성하게 되면 TestJPATeam3와 달리 정상적으로 동작하는걸 확인할수 있다.
			 */

			em.flush();
			em.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}


	@Test
	void TestJPATeam5() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//팀 저장
			Team3 team3 = new Team3();
			team3.setName("TeamA");
			em.persist(team3);

			//회원 저장
			Member3 member3 = new Member3();
			member3.setName("member1");
			member3.setTeam(team3);
			em.persist(member3);

			em.flush();
			em.clear();
			// 위에 flush 와 clear로 1차 캐시와 쓰기 지연 저장소(이전에 객체 데이터 즉 416라인 코드부터 424라인 코드)에 있던 데이터를 비웠기때문에
			// 아래 코드 수행시 쿼리문이 동작하여 1차 캐시에 다시 저장한다. (이때 저장한 데이터에는 List<Members3> members변수에는 정상적인 모든 데이터가 있다.)
			//그래서 정상적인 동작이 가능.
			Team3 findTeam3 =  em.find(Team3.class, team3.getId());
			for(Member3 m : findTeam3.getMembers()) {
				System.out.println("m.getName() = " + m.getName());
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestJPATeam6() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//팀 저장
			Team3 team3 = new Team3();
			team3.setName("TeamA");
			em.persist(team3);

			//회원 저장
			Member3 member3 = new Member3();
			member3.setName("member1");
			member3.setTeam(team3);
			em.persist(member3);

			// 여기서는 아무 데이터가 없다고 나온다. 이유는 현재 commit이전 이기 때문에 쿼리문은 나가기전이기에 사실상 1차캐시에 의존하여 동작한다.
			//하지만 여기까지의 1차 캐시의 데이터라곤 위에 Member 객체에 Team3 team과 String name변수에 데이터 말고는 모두 null이고, Team또한 String name외에는
			//모두 null값이 들어가 있다. 그리고 이러한 데이터 바탕으로 동작하다보니 당연히 List<Member3> members의 값은 null 인 것이다.
			Team3 findTeam3 =  em.find(Team3.class, team3.getId());
			System.out.println("==========================");
			for(Member3 m : findTeam3.getMembers()) {
				System.out.println("m.getName() = " + m.getName());
			}
			System.out.println("==========================");
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestJPATeam7() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//팀 저장
			Team3 team3 = new Team3();
			team3.setName("TeamA");
			em.persist(team3);

			//회원 저장
			Member3 member3 = new Member3();
			member3.setName("member1");
			member3.setTeam(team3);
			em.persist(member3);

			team3.getMembers().add(member3);

			// 그래서 TestJPATeam6에 보완하고자 500번째 라인의 코드가 추가된것이다.
			Team3 findTeam3 =  em.find(Team3.class, team3.getId());
			System.out.println("==========================");
			for(Member3 m : findTeam3.getMembers()) {
				System.out.println("m.getName() = " + m.getName());
			}
			System.out.println("==========================");
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	@Test
	void TestJPATeam8() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			//팀 저장
			Team4 team4 = new Team4();
			team4.setName("TeamA");
			em.persist(team4);

			//회원 저장
			Member4 member4 = new Member4();
			member4.setName("member1");
			member4.setTeam4(team4);
			em.persist(member4);

			// Member class에 setTeam4() 메소드를 추가함으로써 TestJPATeam7()에 번거러움을 해결
			Team4 findTeam4 =  em.find(Team4.class, team4.getId());
			System.out.println("==========================");
			for(Member4 m : findTeam4.getMembers()) {
				System.out.println("m.getName() = " + m.getName());
			}
			System.out.println("==========================");
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
}
