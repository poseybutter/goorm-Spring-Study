package com.study.my_spring_study_diary.service;

import com.study.my_spring_study_diary.repository.StudyLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 실험 3: 필드 주입 (권장하지 않음) ⚠️
@Service
public class ExampleService3 {
    @Autowired
    private StudyLogRepository repository;
}