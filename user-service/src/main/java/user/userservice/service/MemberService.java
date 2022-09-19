package user.userservice.service;

import user.userservice.domain.Member;
import user.userservice.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

//@Service
public class MemberService {

    private final MemberRepository memberRepository;

//    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    } //for Dependency Injection
    //MemberService 객체 생성 시, 스프링 빈에 등록된 repository 객체를 주입해줌 >> 이것도 DI


    public Long join(Member member){

        long start = System.currentTimeMillis();

        try{
            //같은 이름 중복 처리
            validateDuplicateMember(member);

            memberRepository.save(member);
            return member.getId();

        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("Join's timeMs = " + timeMs + "ms");
        }

    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
            .ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 이름입니다.");
            });
    }

    //전체 회원 조회
    public List<Member> findAllMember(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOneMember(Long memberId){
        return memberRepository.findById(memberId);
    }

}
