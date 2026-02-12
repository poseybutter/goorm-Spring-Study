# My Spring Study Diary 📚

Goorm Backend BootCamp에서 Spring Boot를 학습하며 기록하는 프로젝트입니다.

## 프로젝트 소개

Spring Boot를 활용하여 **학습일지 API**를 개발하는 프로젝트입니다.
학습 내용을 기록하고 관리할 수 있는 REST API를 구현합니다.

## 기술 스택

- **Java**: 17
- **Spring Boot**: 4.0.3-SNAPSHOT
- **Build Tool**: Gradle
- **Database**: MySQL (기본), H2 (로컬 학습용)
- **Dependencies**:
  - Spring Data JPA
  - Spring Web MVC
  - Lombok
  - Spring DevTools

## 주요 기능

### 학습일지 API 엔드포인트

- ✅ `POST /api/v1/logs`: 학습일지 생성 (완료)
- ✅ `GET /api/v1/logs`: 학습일지 목록 조회
- ✅ `GET /api/v1/logs/{id}`: 학습일지 상세 조회
- ✅ `GET /api/v1/logs/date/{date}`: 날짜별 조회 (예: `2026-02-11`)
- ✅ `GET /api/v1/logs/category/{category}`: 카테고리별 조회
- ✅ `GET /api/v1/logs/page?page=0&size=10&sortBy=createdAt&sortDirection=DESC`: 페이징 조회
- ✅ `GET /api/v1/logs/category/{category}/page?page=0&size=10`: 카테고리별 페이징 조회
- ✅ `PUT /api/v1/logs/{id}`: 학습일지 수정
- ✅ `DELETE /api/v1/logs/{id}`: 학습일지 삭제

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

### 2. 데이터베이스 준비 (MySQL / Docker Compose)
현재 `src/main/resources/application.yml`은 기본적으로 **MySQL**을 바라봅니다.  
따라서 MySQL이 실행 중이 아니면 애플리케이션이 기동 중 DB 연결에 실패하면서 종료되어, API/H2 콘솔 접속이 불가능합니다.

```bash
# MySQL 컨테이너 실행(백그라운드)
docker compose up -d
```

#### MySQL 접속 정보(현재 레포 기준)
- **호스트**: `localhost`
- **포트**: `3306` (기본값. `.env`의 `DB_PORT`로 변경 가능)
- **DB 이름**: `diary_db`
- **계정(앱용)**: `app` / `apppass`
- **root 계정**: `root` / `rootpass`

> 참고: `.env`는 **Docker Compose 컨테이너(MySQL)** 설정에 사용됩니다.  
> Spring Boot 애플리케이션은 현재 `application.yml`에 적힌 값으로 DB에 연결합니다(별도 설정이 없으면 `.env`를 자동으로 읽지 않습니다).

#### 초기 스키마/데이터 자동 주입
`docker-compose.yml`에서 아래 파일들을 MySQL 초기화 디렉터리에 마운트하고 있어요.
- `src/main/resources/db/schema.sql` → 테이블 생성
- `src/main/resources/db/data.sql` → 샘플 데이터 삽입

MySQL 데이터는 Docker 볼륨(`mysql-data`)에 저장되기 때문에, **처음 컨테이너를 만들 때만** 초기 SQL이 실행됩니다.

#### “초기화 SQL이 다시 실행되게” 완전 초기화하기(주의)
```bash
# 컨테이너 + 볼륨까지 제거(데이터 삭제) → 다시 생성 시 schema/data.sql 재적용
docker compose down -v
docker compose up -d
```

#### MySQL이 제대로 떴는지 빠르게 확인
```bash
# 컨테이너 상태 확인
docker compose ps

# MySQL 로그 확인(종료: Ctrl+C)
docker compose logs -f mysql
```

### 3. 애플리케이션 실행
```bash
# 기본 실행(현재 application.yml 기준 포트: 8081)
./gradlew bootRun
```

포트가 이미 사용 중인 경우(기본 포트는 8081):
```bash
# 다른 포트로 실행
./gradlew bootRun --args='--server.port=8080'

# (선택) macOS에서 특정 포트 점유 프로세스 종료 후 실행
# lsof -ti :8081 | xargs kill -9
```

### 4. 접속
애플리케이션이 실행되면 기본적으로 `http://localhost:8081`에서 접근할 수 있습니다.

#### DB 연결 실패로 앱이 안 뜰 때(가장 흔한 케이스)
아래와 같은 로그가 보이면(MySQL `Connection refused` 등) MySQL이 안 떠있거나 포트/계정이 맞지 않는 상태입니다.
- 해결: `docker compose up -d`로 MySQL을 먼저 실행하고, `application.yml`의 `spring.datasource.username/password`가 `app/apppass`와 일치하는지 확인하세요.

#### API 테스트 예시

**학습일지 생성 (POST)** ✅
```bash
curl -X POST http://localhost:8081/api/v1/logs \
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
curl http://localhost:8081/api/v1/logs
```

**학습일지 상세 조회 (GET)** ⏳
```bash
curl http://localhost:8081/api/v1/logs/1
```

**브라우저에서 테스트:**
- Hello API: `http://localhost:8081/hello`
- Hello with name: `http://localhost:8081/hello/Spring`
- 학습일지 목록: `http://localhost:8081/api/v1/logs`

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

H2 콘솔은 **애플리케이션이 정상 기동된 상태**에서만 접속할 수 있습니다.
또한 현재 기본 설정은 MySQL이므로, H2 콘솔로 H2 DB를 보려면 datasource를 H2로 전환해야 합니다.

- **콘솔 URL(기본 포트 기준)**: `http://localhost:8081/h2-console`

#### 로컬에서 H2로 빠르게 실행(예시)
MySQL 없이 H2로만 띄우고 싶다면, 아래처럼 실행 인자로 datasource를 H2로 바꿔서 실행할 수 있습니다.

```bash
# H2(in-memory)로 실행 예시
./gradlew bootRun --args='--server.port=8081 --spring.datasource.url=jdbc:h2:mem:diary --spring.datasource.driverClassName=org.h2.Driver --spring.datasource.username=sa --spring.datasource.password='
```

## DB 스키마/데이터에 대한 현재 상태(중요)
현재 레포에는 DB 관련 코드/스키마가 “학습 과정 중”이라 일부가 동시에 존재합니다.

- `src/main/resources/db/schema.sql`에는 `users`, `diary_entries` 테이블이 정의되어 있습니다.
- 반면 `MySQLStudyLogDaoImpl`(JdbcTemplate 기반)은 `study_logs` 테이블을 사용합니다.

즉, **`MySQLStudyLogDaoImpl`로 학습일지 저장/조회 기능을 실제로 사용하려면 `study_logs` 테이블 DDL이 필요**합니다.  
학습 진행에 따라 스키마/DAO는 정리될 예정입니다.

## 테스트(Tests)
테스트는 `src/test/java`에 위치합니다.

```bash
./gradlew test
```

`@SpringBootTest`는 애플리케이션 컨텍스트를 띄우면서 DB 연결까지 시도하므로, MySQL이 안 떠있으면 실패할 수 있습니다.  
이 경우 MySQL을 실행하거나, H2로 실행하도록 테스트/프로파일을 분리하는 방식으로 해결할 수 있습니다.

## 프로젝트 구조

이 프로젝트는 “Controller → Service → (DAO/Repository)” 형태로 흐름을 잡고 학습하는 구조입니다.

- **controller**: HTTP 요청/응답 담당 (URL 매핑, Request/Response DTO 입출력)
- **service**: 비즈니스 로직 담당 (검증, 변환, 페이징 처리 등)
- **dao**: DB 접근 로직 담당 (현재는 `StudyLogDao` + `MySQLStudyLogDaoImpl(JdbcTemplate)` 형태)
- **repository**: 학습용 Map 저장소 (`StudyLogRepository`) — DB 없이도 CRUD 흐름을 연습하기 위한 용도
- **dto**: API 입출력 전용 객체 (Entity 직접 노출을 피하기 위한 계층)
- **entity**: 도메인 데이터 구조 (`StudyLog`, `Category`, `Understanding`)
- **global/common**: 공통 응답/페이징 객체 (`ApiResponse`, `PageRequest`, `PageResponse`)
- **exception**: 커스텀 예외 모음
- **resources/db**: Docker MySQL 초기 스키마/샘플 데이터 SQL
- **test**: 테스트 코드 (Spring 컨텍스트 기반 테스트 포함)

```
src/
├── main/
│   ├── java/com/study/my_spring_study_diary/
│   │   ├── MySpringStudyDiaryApplication.java   # 메인 클래스
│   │   ├── BeanScopeTest.java                   # Bean Scope 학습용(실행 시 출력)
│   │   │
│   │   ├── controller/
│   │   │   ├── HelloController.java             # 기본 테스트 컨트롤러
│   │   │   ├── StudyLogController.java          # 학습일지 REST API 컨트롤러
│   │   │   └── TestController.java              # Singleton 확인용 컨트롤러
│   │   │
│   │   ├── dao/
│   │   │   ├── StudyLogDao.java                 # DAO 인터페이스
│   │   │   └── MySQLStudyLogDaoImpl.java        # JdbcTemplate 기반 MySQL DAO
│   │   │
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   │   ├── StudyLogCreateRequest.java
│   │   │   │   └── StudyLogUpdateRequest.java
│   │   │   └── response/
│   │   │       ├── StudyLogDeleteResponse.java
│   │   │       └── StudyLogResponse.java
│   │   │
│   │   ├── entity/
│   │   │   ├── Category.java
│   │   │   ├── StudyLog.java
│   │   │   └── Understanding.java
│   │   │
│   │   ├── exception/
│   │   │   ├── DuplicateResourceException.java
│   │   │   ├── InvalidPageRequestException.java
│   │   │   └── ResourceNotFoundException.java
│   │   │
│   │   ├── global/common/
│   │   │   ├── ApiResponse.java
│   │   │   ├── PageRequest.java
│   │   │   └── PageResponse.java
│   │   │
│   │   ├── repository/
│   │   │   └── StudyLogRepository.java          # Map 기반 저장소(학습용)
│   │   │
│   │   └── service/
│   │       ├── StudyLogService.java             # 비즈니스 로직
│   │       └── (예제 서비스들)                  # ExampleService*, ServiceA/B 등
│   │
│   └── resources/
│       ├── application.yml
│       └── db/
│           ├── schema.sql
│           ├── data.sql
│           └── init.sql
│
└── test/
    └── java/com/study/my_spring_study_diary/
        ├── MySpringStudyDiaryApplicationTests.java
        └── dao/StudyLogDaoTest.java
```

## 개발 환경

- IDE: IntelliJ IDEA / VS Code
- OS: macOS
- Java Version: 17

## 트러블슈팅 기록

### Issue #1: BadSqlGrammarException - 테이블명 불일치 오류

#### 증상
```
500 Internal Server Error
org.springframework.jdbc.BadSqlGrammarException: 
Table 'diary_db.diary_logs' doesn't exist
```
POST 요청으로 데이터를 저장하려고 할 때 위와 같은 오류가 발생했습니다.

#### 원인

**비유로 이해하기:**
> 친구에게 "빨간 상자에 물건 넣어줘"라고 했는데, 실제로는 "파란 상자"만 있는 상황이에요.
> 코드는 `diary_logs` 테이블을 찾는데, 데이터베이스에는 `diary_entries` 테이블만 있었던 거죠.

**구체적인 원인:**
1. **DAO(데이터 접근 코드)**에서는 `diary_logs` 테이블에 데이터를 넣으려고 했습니다.
   ```java
   INSERT INTO diary_logs (title, content, ...) VALUES (?, ?, ...)
   ```

2. 하지만 **DB 초기화 스크립트**(schema.sql)는 `diary_entries` 테이블을 만들었습니다.
   ```sql
   CREATE TABLE diary_entries (...)
   ```

3. Spring Boot가 실행되면서 schema.sql이 실행되어 `diary_entries` 테이블은 만들어졌지만,  
   실제 코드는 `diary_logs`를 찾으니까 "테이블이 없다"는 오류가 발생한 것입니다.

**왜 이런 일이?**
- 개발 중에 테이블명을 변경하면서 **DAO 코드와 SQL 스크립트를 함께 수정하지 않아서** 발생했습니다.
- 즉, **코드와 DB 스키마의 불일치**가 원인이었습니다.

#### 해결 방법

모든 파일에서 테이블명을 **`diary_entries`** 로 통일했습니다.

**수정한 파일:**
1. `MySQLStudyLogDaoImpl.java` - 모든 SQL 쿼리의 테이블명 수정
2. `schema.sql` - 테이블 생성 및 인덱스 생성 구문 수정
3. `init.sql` - 초기화 스크립트의 테이블명 수정
4. `data.sql` - 테스트 데이터 삽입 구문의 테이블명 수정

#### 배운 점

1. **일관성이 중요하다**  
   코드와 데이터베이스 스키마는 항상 일치해야 합니다. 하나를 바꾸면 관련된 모든 곳을 함께 바꿔야 해요.

2. **에러 메시지를 잘 읽자**  
   `Table 'diary_db.diary_logs' doesn't exist`라는 메시지가 핵심 힌트였습니다.  
   "어떤 테이블을 찾았는데 없다"는 뜻이니, 실제 DB에 그 테이블이 있는지 확인하면 됩니다.

3. **변경 시 체크리스트**  
   - [ ] DAO/Repository의 SQL 쿼리
   - [ ] DB 초기화 스크립트 (schema.sql, init.sql)
   - [ ] 테스트 데이터 스크립트 (data.sql)
   - [ ] Entity 클래스 (JPA 사용 시 @Table 어노테이션)

4. **개발 중에는 DB를 자주 초기화하자**  
   Docker를 사용한다면 `docker-compose down -v && docker-compose up -d`로  
   볼륨까지 삭제하고 재시작하면 깨끗한 상태로 테스트할 수 있습니다.

---

## 라이센스

개인 학습 목적으로 만든 프로젝트입니다.

---

⭐ Spring Boot 학습 여정을 함께 응원해주세요!
