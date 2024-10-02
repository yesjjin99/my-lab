package intro.spring_intro.service;

import intro.spring_intro.domain.Member;
import intro.spring_intro.repository.MemberRepository;
import intro.spring_intro.repository.MemoryMemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
- service : 비즈니스에 의존적으로 설계(네이밍)
- repository : 데이터를 넣고 빼는 용도에 맞도록 설계
 */
/*
- JPA를 사용하려면 항상 @Transactional 이 있어야 한다!
- JPA는 모든 데이터 변경이 항상 트랜잭션 안에서 실행되어야 한다
 */

//@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    /* Dependency Injection(DI) */
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
    public Long join(Member member) {
        // 중복 회원 검증
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
            .ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 회원입니다!");
            });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
