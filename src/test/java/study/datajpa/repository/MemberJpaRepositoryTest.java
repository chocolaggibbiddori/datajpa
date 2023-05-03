package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

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
}