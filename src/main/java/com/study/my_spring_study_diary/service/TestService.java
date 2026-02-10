package com.study.my_spring_study_diary.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * "Bean을 찾을 수 없는 경우"를 보기 위한 실험용 서비스.
 * 예시 1: Bean을 찾을 수 없는 경우
 * broken 프로필에서만 활성화됩니다.
 */
@Profile("broken")
@Service
public class TestService {
    private final NonExistentService service; // 존재하지 않는 서비스

    public TestService(NonExistentService service) {
        this.service = service;
    }
}
// 에러: No qualifying bean of type 'NonExistentService' available
