package com.study.my_spring_study_diary.dto.request;

import java.time.LocalDate;

/**
 * 학습 일지 수정 요청 DTO
 * 모든 필드가 Optional (null 가능)
 * null이 아닌 필드만 업데이트됨
 */
public class StudyLogUpdateRequest {

    private String title;           // 학습 주제
    private String content;         // 학습 내용
    private String category;        // 카테고리
    private String understanding;   // 이해도
    private Integer studyTime;      // 학습 시간 (분)
    private LocalDate studyDate;    // 학습 날짜

    // 기본 생성자
    public StudyLogUpdateRequest() {
    }

    // Getter 메서드들
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }

    public String getUnderstanding() {
        return understanding;
    }

    public Integer getStudyTime() {
        return studyTime;
    }

    public LocalDate getStudyDate() {
        return studyDate;
    }

    // Setter 메서드들
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUnderstanding(String understanding) {
        this.understanding = understanding;
    }

    public void setStudyTime(Integer studyTime) {
        this.studyTime = studyTime;
    }

    public void setStudyDate(LocalDate studyDate) {
        this.studyDate = studyDate;
    }

    /**
     * 모든 필드가 null인지 확인
     * @return 모든 필드가 null이면 true
     */
    public boolean hasNoUpdates() {
        return title == null 
            && content == null 
            && category == null 
            && understanding == null 
            && studyTime == null 
            && studyDate == null;
    }
}
