package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Commit
@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void testMember() {
        //g
        Member member = new Member("memberA");
        memberJpaRepository.save(member);

        //w
        Member findMember = memberJpaRepository.find(member.getId());

        //t
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD() {
        //g
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //w1
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        //t1
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //w2
        List<Member> all = memberJpaRepository.findAll();

        //t2
        assertThat(all.size()).isEqualTo(2);

        //w3
        long count = memberJpaRepository.count();

        //t3
        assertThat(count).isEqualTo(2);

        //w4
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        //t4
        assertThat(memberJpaRepository.count()).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        //g
        Member m1 = new Member("memberA", 10);
        Member m2 = new Member("memberA", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        //w
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("memberA", 15);

        //t
        assertThat(result.get(0)).isEqualTo(m2);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testNamedQuery() {
        //g
        Member m1 = new Member("memberA", 10);
        Member m2 = new Member("memberB", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        //w
        List<Member> result = memberJpaRepository.findByUsername("memberA");

        //t
        assertThat(result.get(0)).isEqualTo(m1);
    }
}