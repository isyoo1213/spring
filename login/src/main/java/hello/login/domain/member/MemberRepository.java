package hello.login.domain.member;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {//실제로는 인터페이스로 추상화시키고 구현체로 다양한 DB접근이 올 수 있도록 설계하는 것이 바람직

    private static Map<Long, Member> store = new HashMap<>(); //static 사용
    private static long sequance = 0L; //static 사용

    public Member save(Member member){
        member.setId(++sequance);
        log.info("save: member = {}", member);
        store.put(member.getId(), member);
        return store.get(member.getId()); //Test 필요하지만 간단하게 store에서 저장된 member를 꺼내도록 구성
    }

    public Member findById(long id){
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId){
/*
        List<Member> all = findAll();
        for (Member member : all) {
            if(member.getLoginId().equals(loginId)){
                return Optional.of(member);
            }
        }
        return Optional.empty();
*/
        return findAll().stream()
                .filter(model->model.getLoginId().equals(loginId))
                .findFirst();
    }

    public void clearStore(){
        store.clear();
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }
}
