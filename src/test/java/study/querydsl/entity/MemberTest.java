package study.querydsl.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        // when
        List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();

        // then
        Member foundMember1 = members.get(0);
        Assertions.assertThat(foundMember1.getUsername()).isEqualTo("member1");
        Assertions.assertThat(foundMember1.getAge()).isEqualTo(10);
        Assertions.assertThat(foundMember1.getTeam().getName()).isEqualTo("teamA");

        Member foundMember2 = members.get(1);
        Assertions.assertThat(foundMember2.getUsername()).isEqualTo("member2");
        Assertions.assertThat(foundMember2.getAge()).isEqualTo(20);
        Assertions.assertThat(foundMember2.getTeam().getName()).isEqualTo("teamA");

        Member foundMember3 = members.get(2);
        Assertions.assertThat(foundMember3.getUsername()).isEqualTo("member3");
        Assertions.assertThat(foundMember3.getAge()).isEqualTo(30);
        Assertions.assertThat(foundMember3.getTeam().getName()).isEqualTo("teamB");

        Member foundMember4 = members.get(3);
        Assertions.assertThat(foundMember4.getUsername()).isEqualTo("member4");
        Assertions.assertThat(foundMember4.getAge()).isEqualTo(40);
        Assertions.assertThat(foundMember4.getTeam().getName()).isEqualTo("teamB");

        for (Member member : members) {
            System.out.println("##### member = " + member);
            System.out.println("##### member.team = " + member.getTeam());
        }

    }

}