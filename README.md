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

### 학습일지 API 엔드포인트

- ✅ `POST /api/v1/logs`: 학습일지 생성 (완료)
- ⏳ `GET /api/v1/logs`: 학습일지 목록 조회 (예정)
- ⏳ `GET /api/v1/logs/{id}`: 학습일지 상세 조회 (예정)
- ⏳ `PUT /api/v1/logs/{id}`: 학습일지 수정 (예정)
- ⏳ `DELETE /api/v1/logs/{id}`: 학습일지 삭제 (예정)

### 학습일지 데이터 구조

- **제목** (title): 학습 주제
- **내용** (content): 상세 학습 내용
- **카테고리** (category): `JAVA` ☕, `SPRING` 🌱, `JPA` 🗄️, `DATABASE` 💾, `ALGORITHM` 🧮, `CS` 💻, `NETWORK` 🌐, `GIT` 📂, `ETC` 📝
- **이해도** (understanding): `VERY_GOOD` 😎, `GOOD` 😊, `NORMAL` 😐, `BAD` 😥, `VERY_BAD` 😵
- **학습 시간** (studyTime): 분 단위
- **학습 날짜** (studyDate): YYYY-MM-DD 형식
- **작성일시** (createdAt): 자동 생성
- **수정일시** (updatedAt): 자동 생성

## 실행 방법

### 1. 프로젝트 클론
```bash
git clone https://github.com/poseybutter/goorm-Spring-Study.git
cd goorm-Spring-Study
```

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

포트가 이미 사용 중인 경우:
```bash
# 8080 포트 사용 중인 프로세스 종료 후 실행
lsof -ti :8080 | xargs kill -9
./gradlew bootRun

# 또는 다른 포트로 실행
./gradlew bootRun --args='--server.port=8081'
```

### 3. 접속
애플리케이션이 실행되면 `http://localhost:8080`에서 접근할 수 있습니다.

#### API 테스트 예시

**학습일지 생성 (POST)** ✅
```bash
curl -X POST http://localhost:8080/api/v1/logs \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot 기초",
    "content": "REST API 만들기를 학습했습니다",
    "category": "SPRING",
    "understanding": "GOOD",
    "studyTime": 120,
    "studyDate": "2026-02-04"
  }'
```

**응답 예시:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Spring Boot 기초",
    "content": "REST API 만들기를 학습했습니다",
    "category": "SPRING",
    "categoryIcon": "🌱",
    "understanding": "GOOD",
    "understandingEmoji": "😊",
    "studyTime": 120,
    "studyDate": "2026-02-04",
    "createdAt": "2026-02-04T23:13:56.241992",
    "updatedAt": "2026-02-04T23:13:56.242004"
  },
  "message": "요청이 성공적으로 처리되었습니다."
}
```

**학습일지 목록 조회 (GET)** ⏳
```bash
curl http://localhost:8080/api/v1/logs
```

**학습일지 상세 조회 (GET)** ⏳
```bash
curl http://localhost:8080/api/v1/logs/1
```

**브라우저에서 테스트:**
- Hello API: `http://localhost:8080/hello`
- Hello with name: `http://localhost:8080/hello/Spring`
- 학습일지 목록 (구현 후): `http://localhost:8080/api/v1/logs`

## 주요 구현 내용

### 1. Entity & Enum
- **StudyLog**: 학습일지 엔티티 (제목, 내용, 카테고리, 이해도, 학습시간, 날짜)
- **Category**: 9가지 카테고리 (JAVA, SPRING, JPA, DATABASE, ALGORITHM, CS, NETWORK, GIT, ETC)
- **Understanding**: 5단계 이해도 (VERY_GOOD, GOOD, NORMAL, BAD, VERY_BAD)

### 2. Repository Pattern
- Map 기반 메모리 저장소 구현
- 자동 ID 생성 기능

### 3. Service Layer
- 비즈니스 로직 처리
- 유효성 검증 (제목, 내용, 학습시간)
- DTO ↔ Entity 변환

### 4. API Response
- 통일된 응답 형식 (success, data, message)
- 성공/실패 응답 구분

### 5. Logging
- SLF4J를 사용한 로깅
- 요청/응답 로그 기록

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
- [x] Spring Boot 프로젝트 초기 설정
- [x] 기본 컨트롤러 테스트 (HelloController)
- [x] 프로젝트 패키지 구조 설계

### Day 1: 학습 일지 API 만들기 - CREATE ✅
- [x] StudyLog 엔티티 설계
- [x] Category, Understanding Enum 정의
- [x] StudyLogCreateRequest DTO 작성
- [x] StudyLogResponse DTO 작성
- [x] StudyLogRepository 구현 (Map 기반)
- [x] StudyLogService - 생성 로직 구현
- [x] StudyLogController - POST API 구현
- [x] ApiResponse 공통 응답 래퍼 작성
- [x] Logger 추가 (요청/응답 로깅)
- [x] 유효성 검증 로직 구현
- [x] API 테스트 완료

### Day 2: 학습 일지 API 만들기 - READ ⏳
- [ ] StudyLogService - 조회 로직 구현
  - [ ] 전체 목록 조회
  - [ ] 상세 조회 (ID 기반)
- [ ] StudyLogController - GET API 구현
  - [ ] GET /api/v1/logs (목록 조회)
  - [ ] GET /api/v1/logs/{id} (상세 조회)
- [ ] ResourceNotFoundException 예외 처리

### Day 3: 학습 일지 API 만들기 - UPDATE ⏳
- [ ] StudyLogUpdateRequest DTO 작성
- [ ] StudyLogService - 수정 로직 구현
- [ ] StudyLogController - PUT API 구현
  - [ ] PUT /api/v1/logs/{id}
- [ ] 수정 시 예외 처리 (존재하지 않는 리소스)

### Day 4: 학습 일지 API 만들기 - DELETE ⏳
- [ ] StudyLogService - 삭제 로직 구현
- [ ] StudyLogController - DELETE API 구현
  - [ ] DELETE /api/v1/logs/{id}
- [ ] 삭제 시 예외 처리
- [ ] 전체 API 테스트 및 검증

## 개발 환경

- IDE: IntelliJ IDEA / VS Code
- OS: macOS
- Java Version: 17

## 라이센스

This project is for personal learning purposes.

---

⭐ Spring Boot 학습 여정을 함께 응원해주세요!
