package com.study.my_spring_study_diary;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.study.my_spring_study_diary.service.StudyLogService;

@Component
public class BeanScopeTest implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) {
        // 같은 Bean을 두 번 가져오기
        StudyLogService service1 = context.getBean(StudyLogService.class);
        StudyLogService service2 = context.getBean(StudyLogService.class);

        // 같은 인스턴스인지 확인
        System.out.println("같은 인스턴스? " + (service1 == service2));  // true
        System.out.println("service1 hashCode: " + service1.hashCode());
        System.out.println("service2 hashCode: " + service2.hashCode());
    }
}

