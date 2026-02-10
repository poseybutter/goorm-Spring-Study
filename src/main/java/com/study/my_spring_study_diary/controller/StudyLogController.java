package com.study.my_spring_study_diary.controller;

import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.request.StudyLogUpdateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.dto.response.StudyLogDeleteResponse;
import com.study.my_spring_study_diary.global.common.PageRequest;
import com.study.my_spring_study_diary.global.common.PageResponse;
import com.study.my_spring_study_diary.service.StudyLogService;
import com.study.my_spring_study_diary.global.common.ApiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 학습 일지 컨트롤러
 *
 * @RestController 어노테이션 설명:
 * - @Controller + @ResponseBody 의 조합
 * - 이 클래스의 모든 메서드 반환값을 JSON으로 변환하여 응답
 * - REST API 개발 시 사용
 *
 * @RequestMapping 어노테이션 설명:
 * - 이 컨트롤러의 기본 URL 경로를 설정
 * - 모든 메서드의 URL 앞에 "/api/v1/logs"가 붙음
 */
@RestController  // ⭐ REST API 컨트롤러로 등록!
@RequestMapping("/api/v1/logs")  // 기본 URL 경로 설정
public class StudyLogController {

    // ⭐ Logger 선언 (로그 출력용)
    private static final Logger log = LoggerFactory.getLogger(StudyLogController.class);

    // ⭐ 의존성 주입: Service를 주입받음
    private final StudyLogService studyLogService;

    /**
     * 생성자 주입
     * Spring이 StudyLogService Bean을 찾아서 자동으로 주입해줍니다.
     */
    public StudyLogController(StudyLogService studyLogService) {
        this.studyLogService = studyLogService;
    }

    // ==================== CREATE (Day 1) ====================
    /**
     * 학습 일지 생성 (CREATE)
     *
     * @PostMapping: POST 요청을 처리
     * @RequestBody: HTTP Body의 JSON을 객체로 변환
     *
     * POST /api/v1/logs
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudyLogResponse>> createStudyLog(
            @RequestBody StudyLogCreateRequest request) {

        log.info("📥 Controller: 학습 일지 생성 요청 받음 - 제목: {}", request.getTitle());

        // Service 호출하여 학습 일지 생성
        StudyLogResponse response = studyLogService.createStudyLog(request);

        log.info("📤 Controller: 학습 일지 생성 완료 - ID: {}", response.getId());

        // 201 Created 상태 코드와 함께 응답
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // ==================== READ (Day 2) ====================
    /**
     * 모든 학습 일지 조회 (READ - All)
     * @GetMapping: GET 요청을 처리
     * GET /api/v1/logs
     */
    @GetMapping
//    public ResponseEntity<ApiResponse<List<StudyLogResponse>>> getAllStudyLogs() {
//        List<StudyLogResponse> responses = studyLogService.getAllStudyLogs();
//        return ResponseEntity.ok(ApiResponse.success(responses));
//    }
    public List<StudyLogResponse> getAllStudyLogs() {

        // Service 호출하여 모든 학습 일지 조회
        return studyLogService.getAllStudyLogs();
    }

    /**
     * 특정 학습 일지 조회 (READ - Single)
     *
     * GET /api/v1/logs/{id}
     */
    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<StudyLogResponse>> getStudyLogById(@PathVariable Long id) {
//        StudyLogResponse response = studyLogService.getStudyLogById(id);
//        return ResponseEntity.ok(ApiResponse.success(response));
//    }
    public StudyLogResponse getStudyLogById(
            @PathVariable Long id) {

        // Service 호출하여 ID로 학습 일지 조회
        return studyLogService.getStudyLogById(id);

    }

    /**
     * 날짜별 학습 일지 조회 (READ - By Date)
     *
     * @GetMapping("/date/{date}"): GET 요청을 처리 (날짜 경로 변수 포함)
     * @PathVariable: URL 경로의 {date} 값을 매개변수로 받음
     *
     * GET /api/v1/logs/date/{date}
     * 예시: GET /api/v1/logs/date/2025-01-15
     */
    @GetMapping("/date/{date}")
    public List<StudyLogResponse> getStudyLogsByDate(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        // Service 호출하여 날짜로 학습 일지 조회
        return studyLogService.getStudyLogsByDate(date);
    }

    /**
     * 카테고리별 학습 일지 조회 (READ - By Category)
     *
     * @GetMapping("/category/{category}"): GET 요청을 처리 (카테고리 경로 변수 포함)
     * @PathVariable: URL 경로의 {category} 값을 매개변수로 받음
     *
     * GET /api/v1/logs/category/{category}
     * 예시: GET /api/v1/logs/category/SPRING
     *      GET /api/v1/logs/category/JAVA
     */
    @GetMapping("/category/{category}")
    public List<StudyLogResponse> getStudyLogsByCategory(
            @PathVariable String category) {

        // Service 호출하여 카테고리로 학습 일지 조회
        return studyLogService.getStudyLogsByCategory(category);
    }

    /**
     * 페이징 처리된 학습 일지 목록 조회
     * GET /api/v1/logs/page?page=0&size=10&sortBy=createdAt&sortDirection=DESC
     */
    @GetMapping("/page")
    public PageResponse<StudyLogResponse> getStudyLogsWithPaging(
            @ModelAttribute PageRequest pageRequest) {

        return studyLogService.getStudyLogsWithPaging(pageRequest);
    }

    /**
     * 카테고리별 페이징 조회
     * GET /api/v1/logs/category/{category}/page?page=0&size=5
     */
    @GetMapping("/category/{category}/page")
    public PageResponse<StudyLogResponse> getStudyLogsByCategoryWithPaging(
            @PathVariable String category,
            @ModelAttribute PageRequest pageRequest) {

        return studyLogService.getStudyLogsByCategoryWithPaging(category, pageRequest);
    }

    // ==================== UPDATE (Day 3 - 오늘!) ====================

    /**
     * 학습 일지 수정
     * PUT /api/v1/logs/{id}
     *
     * @PutMapping: PUT 요청을 처리하는 어노테이션
     *              리소스의 전체 또는 일부를 수정할 때 사용
     *
     * @PathVariable: URL의 {id} 부분을 파라미터로 받음
     * @RequestBody: HTTP Body의 JSON을 객체로 변환
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyLogResponse>> updateStudyLog(
            @PathVariable Long id,
            @RequestBody StudyLogUpdateRequest request) {

        StudyLogResponse response = studyLogService.updateStudyLog(id, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // ========== DELETE ==========

    /**
     * 학습 일지 삭제 API
     *
     * DELETE /api/v1/logs/{id}
     *
     * @param id 삭제할 학습 일지 ID
     * @return 삭제 결과
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyLogDeleteResponse>> deleteStudyLog(
            @PathVariable Long id) {

        StudyLogDeleteResponse response = studyLogService.deleteStudyLog(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
