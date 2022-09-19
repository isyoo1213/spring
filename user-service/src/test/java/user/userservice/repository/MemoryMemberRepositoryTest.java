package user.userservice.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import user.userservice.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save(){
        Member member = new Member();
        member.setName("spring");

        repository.save(member);
        //setId를 통해 id 설정 > store.put을 통해 map구조로 저장됨

        Member result = repository.findById(member.getId()).get();
        //Optional에서 값을 꺼낼 때 바로 처리하는 것은 바람직하지 않으나 테스트코드이므로 간략화
        System.out.println("result == " + (result == member));
        //member변수와 save를 통해 store에 저장어 findById를 통해 가져온 객체가 같은지 확인

        //org.junit.jupiter.api 에서 제공하는 클래스
//        assertEquals(member, result);
        //parameter 정보보기 단축키 - ctrl + P
        //( expected , actual )

        //org.assertj.core.api 에서 제공하는 클래스 - 조금 더 직관적으로 쓸 수 있음
        //Assersions에 Alt+Enter >> static으로 지정 후 바로 사용가능
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();
        assertThat(member1).isEqualTo(result);
//        assertThat(member2).isEqualTo(result);

        System.out.println(member1);
        System.out.println(member2);

    }

    @Test
    public void findAll(){
        Member member1 = new Member();
        Member member2 = new Member();

        member1.setName("spring1");
        member2.setName("spring2");

        repository.save(member1);
        repository.save(member2);

        List<Member> result = repository.findAll();

        System.out.println(result);

        assertThat(result.size()).isEqualTo(2);
    }

}
