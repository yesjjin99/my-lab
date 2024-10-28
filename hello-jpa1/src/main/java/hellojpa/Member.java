package hellojpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
//@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq", initialValue = 1, allocationSize = 1)  // 데이터베이스 시퀀스 생성
//@TableGenerator(name = "MEMBER_SEQ_GENERATOR", table = "MY_SEQUENCES", pkColumnValue = "MEMBER_SEQ", allocationSize = 50)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)  // Sequence 전략
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")  // 데이터베이스 시퀀스와 매핑
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR")  // 테이블 전략 매핑
    private Long id;

    @Column(name = "name")  // 컬럼 매핑
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)  // enum 타입 매핑
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)  // 날짜 타입 매핑
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // 최신 Hibernate 에서는 @Temporal 사용할 필요 없이 LocalDate, LocalDateTime 을 사용하면 됨
//    private LocalDateTime createdDate;
//    private LocalDateTime lastModifiedDate;

    @Lob  // BLOB, CLOB 매핑 : varchar 를 넘는 큰 데이터
    private String description;

    @Transient  // 특정 필드를 컬럼에 매핑하지 않음 (매핑 무시) -> 메모리에서만 사용
    private int temp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
