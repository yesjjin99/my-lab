package hellojpa;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable  // 값 타입 정의
public class Period {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Period() {  // 임베디드 타입을 사용하기 위해서는 기본 생성자 필수
    }

    public Period(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    private void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    private void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
