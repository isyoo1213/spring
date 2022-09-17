package user.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import user.userservice.domain.Member;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
//구현체는 MemberRepository를 상속받아 스프링에서 알아서 만들어서 빈 등록 by proxy
// >> SpringConfig에서도 바로 MemberRepository 주입받으면 됨

    //JPQL select m from Member m(alias) where m.name=?
    @Override
    Optional<Member> findByName(String name);
}
