package core.spring_core.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor  /*  final이 붙은 필드를 모아서 생성자를 자동으로 만들어준다 */
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    /* 테스트 용도 */
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
