package com.study.my_spring_study_diary.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * "순환 참조(Circular Dependency)" 실험용 Bean.
 * circular 프로필에서만 활성화됩니다.
 */
@Profile("circular")
@Service
public class ServiceB {
    private final ServiceA serviceA;

    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
// 에러: Circular dependency detected
