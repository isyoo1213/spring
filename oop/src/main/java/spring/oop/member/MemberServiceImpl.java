package spring.oop.member;

public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    } //생성자를 통한 주입을 통해 MemberRepository라는 추상화에만 의존가능

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    //테스트 용도 - AppConfig에서 MemberService와 OrderService 생성 시 MemberRepository가 싱글톤으로 유지되는지 확인하기 위함
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
