package com.study.my_spring_study_diary.service;

import com.study.my_spring_study_diary.repository.StudyLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 실험 2: Setter 주입
@Service
public class ExampleService2 {
    private StudyLogRepository repository;

    @Autowired
    public void setRepository(StudyLogRepository repository) {
        this.repository = repository;
    }
}