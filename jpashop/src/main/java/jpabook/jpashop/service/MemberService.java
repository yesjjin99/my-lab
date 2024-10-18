package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)  // JPA의 모든 데이터 변경이나 로직들은 가급적이면 트랜잭션 안에서 실행되어야 한다 -> 그래야 lazy loading 등이 된다
/*
-> readonly 옵션을 주면 JPA가 조회하는 곳에서는 조금 더 성능을 최적화한다
   : 영속성 컨텍스트를 플러시하지 않거나 더티 체킹을 안하므로 약간의 성능 향상
   : 데이터베이스 드라이버가 지원하면 DB에서 성능 향상 (읽기 전용 모드)
*/
@RequiredArgsConstructor  // final 이 붙은 필드만 가지고 생성자 생성
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional  // 읽기가 아닌 쓰기에는 readonly 옵션 넣으면 데이터 변경 안 됨
    public Long join(Member member) {
        validateDuplicateMember(member);  // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
        /*
        JPA에서 em.persist() 를 하면 이 순간에 영속성 컨텍스트에 멤버 객체를 올린다
        -> 영속성 컨텍스트는 key-value 값으로 되어있고, 여기서 DB와 pk로 매핑한 member 의 id 값이 key가 된다
        -> @GeneratedValue 를 세팅하면 영속성 컨텍스트에 pk 값을 넣어줌과 동시에 Member 클래스의 id 값도 채워준다
        => 즉, @GeneratedValue가 있으면 아직 DB에 들어간 시점이 아니어도 id 값이 항상 생성되어 있는 게 보장된다
         */
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
