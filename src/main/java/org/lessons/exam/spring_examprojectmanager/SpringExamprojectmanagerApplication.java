package org.lessons.exam.spring_examprojectmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class SpringExamprojectmanagerApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()  //read all file.env
			.ignoreIfMissing()  //evita err se file .env non esiste
			.load();
   		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
   		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		   //System.setProperty x set vars leggibili solo da within jvm/application, read w System.getProperty(...)
		
		SpringApplication.run(SpringExamprojectmanagerApplication.class, args);
	}
}
