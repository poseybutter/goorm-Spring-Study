package com.study.my_spring_study_diary.service;

import com.study.my_spring_study_diary.repository.StudyLogRepository;
import org.springframework.stereotype.Service;

// 실험 1: 생성자 주입 (권장) ✅
@Service
public class ExampleService1 {
    private final StudyLogRepository repository;

    public ExampleService1(StudyLogRepository repository) {
        this.repository = repository;
    }
}
