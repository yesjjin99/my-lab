package hellojpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Member {

    @Id
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
