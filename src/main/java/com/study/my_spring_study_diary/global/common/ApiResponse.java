package com.study.my_spring_study_diary.global.common;

/**
 * 공통 API 응답 래퍼 클래스
 * 
 * 모든 API 응답을 통일된 형식으로 반환하기 위한 클래스
 * @param <T> 응답 데이터 타입
 */
public class ApiResponse<T> {
    private boolean success;    // 성공 여부
    private T data;             // 응답 데이터
    private String message;     // 응답 메시지

    // 기본 생성자
    public ApiResponse() {
    }

    // 전체 필드 생성자
    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    /**
     * 성공 응답 생성 (정적 팩토리 메서드)
     * @param data 응답 데이터
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "요청이 성공적으로 처리되었습니다.");
    }

    /**
     * 성공 응답 생성 (메시지 커스텀)
     * @param data 응답 데이터
     * @param message 커스텀 메시지
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }

    /**
     * 실패 응답 생성
     * @param message 에러 메시지
     * @return 실패 응답 객체
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }

    // Getter 메서드들
    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    // Setter 메서드들
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
