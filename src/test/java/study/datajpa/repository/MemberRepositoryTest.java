package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Commit
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void testMember() {
        //g
        Member member = new Member("memberA");
        memberRepository.save(member);

        //w
        Member findMember = memberRepository.findById(member.getId()).get();

        //t
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD() {
        //g
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //w1
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        //t1
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //w2
        List<Member> all = memberRepository.findAll();

        //t2
        assertThat(all.size()).isEqualTo(2);

        //w3
        long count = memberRepository.count();

        //t3
        assertThat(count).isEqualTo(2);

        //w4
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        //t4
        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        //g
        Member m1 = new Member("memberA", 10);
        Member m2 = new Member("memberA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //w
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("memberA", 15);

        //t
        assertThat(result.get(0)).isEqualTo(m2);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testNamedQuery() {
        //g
        Member m1 = new Member("memberA", 10);
        Member m2 = new Member("memberB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //w
        List<Member> result = memberRepository.findByUsername("memberA");

        //t
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    void testQuery() {
        //g
        Member m1 = new Member("memberA", 10);
        Member m2 = new Member("memberA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //w
        List<Member> result = memberRepository.findUser("memberA", 20);

        //t
        assertThat(result.get(0)).isEqualTo(m2);
    }

    @Test
    void findUsernameList() {
        //g
        Member m1 = new Member("memberA", 10);
        Member m2 = new Member("memberB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //w
        List<String> result = memberRepository.findUsernameList();

        //t
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void findMemberDto() {
        //g
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("memberA", 10, team);
        memberRepository.save(m1);

        //w
        List<MemberDto> usernameList = memberRepository.findMemberDto();

        //t
        for (MemberDto memberDto : usernameList) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findByNames() {
        //g
        Member m1 = new Member("memberA", 10);
        Member m2 = new Member("memberB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //w
        List<Member> result = memberRepository.findByNames(Arrays.asList("memberA", "memberB"));

        //t
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void returnType() {
        //g
        Member m1 = new Member("memberA", 10);
        Member m2 = new Member("memberB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //w
        List<Member> list = memberRepository.findListByUsername("memberA");
        Member member = memberRepository.findMemberByUsername("memberA");
        Optional<Member> optional = memberRepository.findOptionalByUsername("memberA");

        //t
        System.out.println("list = " + list);
        System.out.println("member = " + member);
        System.out.println("optional = " + optional);
    }

    @Test
    void paging() {
        //g
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "username"));

        //w
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        //t
        assertThat(content.size()).isEqualTo(2);
        assertThat(totalElements).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(1);
        assertThat(page.isFirst()).isFalse();
        assertThat(page.hasNext()).isFalse();
    }

    @Test
    void bulkUpdate() {
        //g
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //w
        int resultCount = memberRepository.bulkAgePlus(20);

        //t
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    void findMemberLazy() {
        //g
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //w
        List<Member> members = memberRepository.findNamedEntityGraphByUsername("member1");

        //t
        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    void queryHint() {
        Member member = memberRepository.save(new Member("member1", 10));

        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    void lock() {
        Member member = memberRepository.save(new Member("member1", 10));

        em.flush();
        em.clear();

        List<Member> result = memberRepository.findLockByUsername("member1");
    }

    @Test
    void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    void specBasic() {
        //g
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("m1", 10, team);
        Member m2 = new Member("m2", 20, team);
        memberRepository.save(m1);
        memberRepository.save(m2);

        em.flush();
        em.clear();

        //w
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        //t
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void queryByExample() {
        //g
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member m1 = new Member("m1", 10, teamA);
        Member m2 = new Member("m2", 20, teamA);
        memberRepository.save(m1);
        memberRepository.save(m2);

        em.flush();
        em.clear();

        //w
        Member member = new Member("m1");
        Team team = new Team("teamA");
        member.setTeam(team);

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);
        List<Member> result = memberRepository.findAll(example);

        //t
        assertThat(result.get(0).getUsername()).isEqualTo("m1");
    }
}