package com.study.my_spring_study_diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MySpringStudyDiaryApplication {
	public static void main(String[] args) {
		// SpringApplication.run(MySpringStudyDiaryApplication.class, args);
		ApplicationContext context = SpringApplication.run(MySpringStudyDiaryApplication.class, args);

		String[] beanNames = context.getBeanDefinitionNames();
		System.out.println("========== 등록된 Bean 목록 ==========");
		for (String beanName : beanNames) {
			if (beanName.contains("studyLog")) {
				System.out.println("✅ " + beanName);
			}
		}
		System.out.println("=====================================");
	}

}
