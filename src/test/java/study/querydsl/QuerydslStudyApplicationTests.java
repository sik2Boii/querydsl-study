package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.QTestEntity;
import study.querydsl.entity.TestEntity;

@SpringBootTest
@Transactional
class QuerydslStudyApplicationTests {

    @PersistenceContext
    EntityManager em;

    @Test
    void contextLoads() {
    }

    @Test
    void querydslTest() {
        // given
        TestEntity testEntity = new TestEntity();
        em.persist(testEntity);

        JPAQueryFactory query = new JPAQueryFactory(em);
        QTestEntity qTestEntity = QTestEntity.testEntity;

        // when
        TestEntity result = query
                .selectFrom(qTestEntity)
                .fetchOne();

        // then
        Assertions.assertThat(result).isEqualTo(testEntity);
        Assertions.assertThat(result.getId()).isEqualTo(testEntity.getId());
    }

}
