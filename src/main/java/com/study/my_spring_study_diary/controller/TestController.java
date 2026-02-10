package com.study.my_spring_study_diary.controller;

import com.study.my_spring_study_diary.service.StudyLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 테스트용 컨트롤러
 * Spring Bean의 Singleton 패턴을 확인하기 위한 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final StudyLogService service1;
    private final StudyLogService service2;

    /**
     * 생성자 주입으로 같은 StudyLogService를 두 번 주입받음
     * Spring은 Singleton이므로 같은 인스턴스를 주입함
     */
    public TestController(StudyLogService service1, StudyLogService service2) {
        this.service1 = service1;
        this.service2 = service2;
    }

    /**
     * Singleton 패턴 확인
     * GET /api/v1/test/singleton
     * 
     * @return 두 서비스가 같은 인스턴스인지 확인 결과
     */
    @GetMapping("/singleton")
    public String checkSingleton() {
        // 두 참조가 같은 인스턴스를 가리키는지 확인
        boolean isSame = (service1 == service2);

        return "service1 == service2 ? " + isSame + "\n" +
               "service1 hashCode: " + service1.hashCode() + "\n" +
               "service2 hashCode: " + service2.hashCode();
    }
}
