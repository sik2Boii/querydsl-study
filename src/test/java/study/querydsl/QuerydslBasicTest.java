package study.querydsl;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

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
    }

    @Test
    public void findMemberByJPQL() {
        // given
        String username = "member1";
        String query = """
                SELECT m
                FROM Member m
                WHERE m.username=:username
                """;

        // when
        Member findMember = em.createQuery(query, Member.class)
                .setParameter("username", username)
                .getSingleResult();

        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void findMemberByQuerydsl() {
        // given
        String username = "member1";

        // when
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq(username))
                .fetchOne();

        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void searchByQuerydsl() {
        // given
        String username = "member1";
        int age = 10;

        // when
        Member findMember = queryFactory
                .selectFrom(member)
                .from(member)
                .where(
                        member.username.eq(username),
                        member.age.eq(age)
                )
                .fetchOne();

        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void fetchByQuerydsl() {
        // List
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        // 1 건
        Member fetchOne = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        // count
        long fetchCount = queryFactory
                .selectFrom(member)
                .fetchCount();

        // List + count
        QueryResults<Member> fetchResults = queryFactory
                .selectFrom(member)
                .fetchResults();
    }

}
