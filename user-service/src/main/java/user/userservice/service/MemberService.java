package user.userservice.service;

import user.userservice.domain.Member;
import user.userservice.repository.MemberRepository;
import user.userservice.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    public Long join(Member member){
        //같은 이름 중복 처리
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
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
