package com.study.my_spring_study_diary.controller;

import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.service.StudyLogService;
import com.study.my_spring_study_diary.global.common.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }
}
