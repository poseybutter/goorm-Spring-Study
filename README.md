# My Spring Study Diary 📚

Goorm Backend BootCamp에서 Spring Boot를 학습하며 기록하는 프로젝트입니다.

## 프로젝트 소개

Spring Boot를 활용하여 **학습일지 API**를 개발하는 프로젝트입니다.
학습 내용을 기록하고 관리할 수 있는 REST API를 구현합니다.

## 기술 스택

- **Java**: 17
- **Spring Boot**: 4.0.3-SNAPSHOT
- **Build Tool**: Gradle
- **Database**: H2 (in-memory), MySQL
- **Dependencies**:
  - Spring Data JPA
  - Spring Web MVC
  - Lombok
  - Spring DevTools

## 주요 기능

### 학습일지 API 엔드포인트 (예정)

- `POST /api/study-logs`: 학습일지 생성
- `GET /api/study-logs`: 학습일지 목록 조회
- `GET /api/study-logs/{id}`: 학습일지 상세 조회
- `PUT /api/study-logs/{id}`: 학습일지 수정
- `DELETE /api/study-logs/{id}`: 학습일지 삭제

### 학습일지 데이터 구조

- **제목**: 학습 주제
- **내용**: 상세 학습 내용
- **카테고리**: Spring, JPA, Database 등
- **이해도**: 상/중/하
- **작성일시**: 자동 생성

## 실행 방법

### 1. 프로젝트 클론
```bash
git clone https://github.com/[your-username]/my-spring-study-diary.git
cd my-spring-study-diary
```

### 2. 애플리케이션 실행
```bash
./mvnw spring-boot:run
# 또는
./gradlew bootRun
```

### 3. 접속
애플리케이션이 실행되면 `http://localhost:8080`에서 접근할 수 있습니다.

#### API 테스트 예시 (구현 후)
```bash
# 학습일지 생성
curl -X POST http://localhost:8080/api/study-logs \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot 기초",
    "content": "Spring Boot 프로젝트 구조를 학습했습니다.",
    "category": "SPRING",
    "understanding": "HIGH"
  }'

# 학습일지 목록 조회
curl http://localhost:8080/api/study-logs

# 학습일지 상세 조회
curl http://localhost:8080/api/study-logs/1
```

## H2 데이터베이스 콘솔

개발 중 H2 데이터베이스 콘솔에 접근할 수 있습니다:
- URL: `http://localhost:8080/h2-console`

## 프로젝트 구조

```
src/main/java/com/study/my_spring_study_diary/
├── MySpringStudyDiaryApplication.java    # 메인 클래스
│
├── controller/
│   └── StudyLogController.java          # REST API 컨트롤러
│
├── dto/
│   ├── request/
│   │   └── StudyLogCreateRequest.java   # 요청 DTO
│   └── response/
│       └── StudyLogResponse.java        # 응답 DTO
│
├── entity/
│   ├── Category.java                    # 카테고리 Enum
│   ├── StudyLog.java                    # StudyLog 엔티티
│   └── Understanding.java               # 이해도 Enum
│
├── exception/
│   ├── DuplicateResourceException.java  # 중복 예외
│   └── ResourceNotFoundException.java   # Not Found 예외
│
├── global/
│   └── common/
│       └── ApiResponse.java             # 공통 API 응답 래퍼
│
├── repository/
│   └── StudyLogRepository.java          # 저장소 (Map 기반)
│
└── service/
    └── StudyLogService.java             # 비즈니스 로직
```

## 구현 단계

### Day 0: Spring Boot 소개
### Day 1: 학습 일지 API 만들기 - CREATE
### Day 2: 학습 일지 API 만들기 - READ
### Day 3: 학습 일지 API 만들기 - UPDATE
### Day 4: 학습 일지 API 만들기 - DELETE

## 개발 환경

- IDE: IntelliJ IDEA / VS Code
- OS: macOS
- Java Version: 17

## 라이센스

This project is for personal learning purposes.

---

⭐ Spring Boot 학습 여정을 함께 응원해주세요!
