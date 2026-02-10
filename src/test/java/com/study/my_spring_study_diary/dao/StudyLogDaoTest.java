package com.study.my_spring_study_diary.dao;

import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.entity.Understanding;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional  // 테스트 후 롤백
class StudyLogDaoTest {

    @Autowired
    private StudyLogDao studyLogDao;

    @Test
    @DisplayName("학습 일지 저장 테스트")
    void saveTest() {
        // given
        StudyLog studyLog = createTestStudyLog();

        // when
        StudyLog saved = studyLogDao.save(studyLog);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("테스트 제목");
    }

    @Test
    @DisplayName("ID로 조회 테스트")
    void findByIdTest() {
        // given
        StudyLog studyLog = createTestStudyLog();
        StudyLog saved = studyLogDao.save(studyLog);

        // when
        Optional<StudyLog> found = studyLogDao.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo(saved.getTitle());
    }

    private StudyLog createTestStudyLog() {
        StudyLog studyLog = new StudyLog();
        studyLog.setTitle("테스트 제목");
        studyLog.setContent("테스트 내용");
        studyLog.setCategory(Category.SPRING);
        studyLog.setUnderstanding(Understanding.GOOD);
        studyLog.setStudyTime(60);
        studyLog.setStudyDate(LocalDate.now());
        studyLog.setCreatedAt(LocalDateTime.now());
        studyLog.setUpdatedAt(LocalDateTime.now());
        return studyLog;
    }
}

