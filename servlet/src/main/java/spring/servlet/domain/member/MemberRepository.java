package spring.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */
public class MemberRepository {

    //static으로 설정함으로써, MemberRepository 인스턴스가 많아도 store, sequence는 하나만 존재
    // + 현재 MemberRepository를 Singleton으로 만들기 때문에 static이 없어도 됨
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    //Singleton으로 설정 - 현재는 Tomcat을 띄울 떄에만 Spring 사용되고, 그 외에는 Spring 사용하지 않음
    private static final MemberRepository instance = new MemberRepository();

    public static MemberRepository getInstance(){
        return instance;
    }

    //생성자를 막아서 외부에서 생성하지 못하도록 설정
    private MemberRepository(){
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values()); //store의 모든 값들을 꺼내서 새로운 ArrayList에 담아서 반환 + store 내부의 값들을 건드리지 않기 위해
    }

    public Member save(Member member){
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public void clearStore(){
        store.clear();
    }

}
