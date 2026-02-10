package com.study.my_spring_study_diary.repository;

import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.exception.InvalidPageRequestException;
import com.study.my_spring_study_diary.global.common.PageRequest;
import com.study.my_spring_study_diary.global.common.PageResponse;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 학습 일지 저장소
 *
 * @Repository 어노테이션 설명:
 * - 이 클래스를 Spring Bean으로 자동으로 등록합니다.다른 클래스에서 주입 받아 사용 가능함.
 * - 데이터 접근 계층임을 명시합니다.
 * - 데이터 접근 관련 예외를 Spring의 DataAccessException으로 변환해 줍니다.
 *
 * 실제 프로젝트에서는 JPA, MyBatis 등을 사용하지만,
 * 이번 강의에서는 Map을 사용하여 데이터를 저장합니다.
 */
@Repository  // ⭐ Spring Bean으로 등록!
public class StudyLogRepository {

    // 데이터 저장소 (실제 DB 대신 Map 사용)
    private final Map<Long, StudyLog> database = new HashMap<>();

    // ID 자동 증가를 위한 시퀀스
    private final AtomicLong sequence = new AtomicLong(1);

    // ========== 생명주기 콜백 ==========

    @PostConstruct
    public void initCallback() {
        System.out.println("========================================");
        System.out.println("📦 StudyLogRepository 초기화 완료!");
        System.out.println("   - 데이터 저장소(Map) 준비됨");
        System.out.println("   - ID 생성기 준비됨");
        System.out.println("========================================");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("========================================");
        System.out.println("🧹 StudyLogRepository 정리 중...");
        System.out.println("   - 저장된 데이터 수: " + database.size());
        System.out.println("   - 마지막 ID: " + (sequence.get() - 1));
        database.clear();  // 데이터 정리
        System.out.println("   - 데이터 정리 완료!");
        System.out.println("========================================");
    }

    // ========== CREATE ==========

    /**
     * 학습 일지 저장 (Create)
     * @param studyLog 저장할 학습 일지
     * @return 저장된 학습 일지 (ID 포함)
     */
    public StudyLog save(StudyLog studyLog) {
        // ID가 없으면 새로운 ID 부여
        if (studyLog.getId() == null) {
            studyLog.setId(sequence.getAndIncrement());
        }

        // Map에 저장
        database.put(studyLog.getId(), studyLog);
        return studyLog;
    }

    /**
     * 학습 일지 수정 (Update)
     * Map은 같은 키로 put하면 덮어쓰므로 save와 동일하게 동작
     * 하지만 의미를 명확히 하기 위해 별도 메서드로 분리
     */
    public StudyLog update(StudyLog studyLog) {
        if (studyLog.getId() == null) {
            throw new IllegalArgumentException("수정할 학습 일지의 ID가 없습니다.");
        }
        if (!database.containsKey(studyLog.getId())) {
            throw new IllegalArgumentException(
                "해당 학습 일지를 찾을 수 없습니다. (id: " + studyLog.getId() + ")");
        }
        database.put(studyLog.getId(), studyLog);
        return studyLog;
    }

    // ========== DELETE ==========

    /**
     * ID로 학습 일지를 삭제합니다.
     *
     * @param id 삭제할 학습 일지 ID
     * @return 삭제 성공 여부 (true: 삭제됨, false: 해당 ID 없음)
     */
    public boolean deleteById(Long id) {
        // Map.remove()는 삭제된 값을 반환, 없으면 null 반환
        StudyLog removed = database.remove(id);
        return removed != null;
    }

    /**
     * ID에 해당하는 학습 일지가 존재하는지 확인합니다.
     *
     * @param id 확인할 학습 일지 ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        return database.containsKey(id);
    }

    /**
     * 전체 학습 일지 조회 (최신순 정렬)
     */
    public List<StudyLog> findAll() {
        return database.values().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * ID로 학습 일지 조회
     * @return Optional: 값이 있을 수도, 없을 수도 있음을 명시
     * Optional: null일 수 있는 값을 감싸는 컨테이너
     */
    public Optional<StudyLog> findById(Long id) {
        return Optional.ofNullable(database.get(id));
        // database.get(id)가 null이면 Optional.empty() 반환
        // null이 아니면 Optional.of(값) 반환
    }

    /**
     * 날짜로 학습 일지 조회
     */
    public List<StudyLog> findByStudyDate(LocalDate date) {
        return database.values().stream()
                .filter(log -> log.getStudyDate().equals(date))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * 카테고리로 학습 일지 조회
     */
    public List<StudyLog> findByCategory(Category category) {
        return database.values().stream()
                .filter(log -> log.getCategory().equals(category))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * 저장된 데이터 개수 조회
     */
    public long count() {
        return database.size();
    }

    /**
     * 모든 학습 일지를 삭제합니다.
     * (테스트용)
     */
    public void deleteAll() {
        database.clear();
    }

    // ========== Soft Delete ==========

    /**
     * Soft Delete 처리 (deleted=true, deletedAt 기록)
     * @param id 삭제할 학습 일지 ID
     * @return 삭제 성공 여부
     */
    public boolean softDeleteById(Long id) {
        StudyLog studyLog = database.get(id);
        if (studyLog == null || studyLog.isDeleted()) {
            return false;
        }

        studyLog.setDeleted(true);
        studyLog.setDeletedAt(LocalDateTime.now());
        return true;
    }

    /**
     * 삭제되지 않은 데이터만 조회 (최신순 정렬)
     */
    public List<StudyLog> findAllActive() {
        return database.values().stream()
                .filter(log -> !log.isDeleted())
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * Soft Delete된 데이터 복구
     */
    public boolean restore(Long id) {
        StudyLog studyLog = database.get(id);
        if (studyLog == null || !studyLog.isDeleted()) {
            return false;
        }

        studyLog.setDeleted(false);
        studyLog.setDeletedAt(null);
        return true;
    }

    // ========== Paging ==========

    /**
     * 페이징 처리된 학습 일지 조회
     * @param pageRequest 페이징 요청 정보
     * @return 페이징 처리된 결과
     */
    public PageResponse<StudyLog> findAllWithPaging(PageRequest pageRequest) {

        // 1. 전체 데이터를 정렬
        List<StudyLog> allLogs = database.values().stream()
                .sorted((a, b) -> {
                    // 정렬 기준에 따라 정렬
                    int result = switch (pageRequest.getSortBy()) {
                        case "title" -> a.getTitle().compareTo(b.getTitle());
                        case "studyTime" -> a.getStudyTime().compareTo(b.getStudyTime());
                        case "studyDate" -> a.getStudyDate().compareTo(b.getStudyDate());
                        default -> a.getCreatedAt().compareTo(b.getCreatedAt());
                    };

                    // 정렬 방향 적용
                    return "ASC".equals(pageRequest.getSortDirection()) ? result : -result;
                })
                .collect(Collectors.toList());

        // 2. 전체 개수
        long totalElements = allLogs.size();

        // 3. 총 페이지 수 계산
        int totalPages = calculateTotalPages(totalElements, pageRequest.getSize());

        // 4. 요청한 페이지 번호 유효성 검증
        int requestedPage = pageRequest.getPage();

        if (requestedPage < 0) {
            throw new InvalidPageRequestException(requestedPage, totalPages);
        }

        if (totalElements > 0 && requestedPage >= totalPages) {
            throw new InvalidPageRequestException(requestedPage, totalPages);
        }

        // 5. 페이징 적용
        int start = pageRequest.getOffset();
        
        // start가 전체 크기보다 크면 빈 리스트 반환 (마지막 페이지를 넘어선 경우)
        if (start >= allLogs.size()) {
            return PageResponse.of(
                    new ArrayList<>(),
                    pageRequest.getPage(),
                    pageRequest.getSize(),
                    totalElements
            );
        }
        
        int end = Math.min(start + pageRequest.getSize(), allLogs.size());
        List<StudyLog> pagedLogs = allLogs.subList(start, end);

        // 6. PageResponse 생성
        return PageResponse.of(
                pagedLogs,
                pageRequest.getPage(),
                pageRequest.getSize(),
                totalElements
        );
    }

    /**
     * 카테고리별 페이징 조회
     * @param category 카테고리
     * @param pageRequest 페이징 요청 정보
     * @return 페이징 처리된 결과
     */
    public PageResponse<StudyLog> findByCategoryWithPaging(Category category,
                                                           PageRequest pageRequest) {

        // 1. 카테고리로 필터링 및 정렬
        List<StudyLog> filteredLogs = database.values().stream()
                .filter(log -> log.getCategory() == category)
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());

        // 2. 전체 개수
        long totalElements = filteredLogs.size();

        // 3. 총 페이지 수 계산
        int totalPages = calculateTotalPages(totalElements, pageRequest.getSize());

        // 4. 요청한 페이지 번호 유효성 검증
        int requestedPage = pageRequest.getPage();

        if (requestedPage < 0) {
            throw new InvalidPageRequestException(requestedPage, totalPages);
        }

        if (totalElements > 0 && requestedPage >= totalPages) {
            throw new InvalidPageRequestException(requestedPage, totalPages);
        }

        // 5. 페이징 적용
        int start = pageRequest.getOffset();
        
        // start가 전체 크기보다 크면 빈 리스트 반환 (마지막 페이지를 넘어선 경우)
        if (start >= filteredLogs.size()) {
            return PageResponse.of(
                    new ArrayList<>(),
                    pageRequest.getPage(),
                    pageRequest.getSize(),
                    totalElements
            );
        }
        
        int end = Math.min(start + pageRequest.getSize(), filteredLogs.size());
        List<StudyLog> pagedLogs = filteredLogs.subList(start, end);

        // 6. PageResponse 생성
        return PageResponse.of(
                pagedLogs,
                pageRequest.getPage(),
                pageRequest.getSize(),
                totalElements
        );
    }

    /**
     * 총 페이지 수 계산
     * @param totalElements 전체 데이터 개수
     * @param pageSize 페이지 크기
     * @return 총 페이지 수
     */
    private int calculateTotalPages(long totalElements, int pageSize) {
        return (int) Math.ceil((double) totalElements / pageSize);
    }
}

