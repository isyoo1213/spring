package user.userservice.repository;

import org.springframework.stereotype.Repository;
import user.userservice.domain.Member;

import java.util.*;

@Repository
public class MemoryMemberRepository implements MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();
    //동시성 문제로 공유되는 변수일 때는 quenqurer 뭐시기 써야하나 예시이므로 단순하게 hashMap
    private Long sequence = 0L;
    //이 변수도 동시성 문제를 고려해서 어텀 뭐시기 해야함

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }

}
