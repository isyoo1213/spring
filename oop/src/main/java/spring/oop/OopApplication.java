package spring.oop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//해당 어노테이션도 @ComponentScan을 기본적으로 포함 --> 프로젝트 최상단에 위치하는 것이 올바름
//즉, spring.oop 에서 시작해 하위 모든 패키지의 @Component를 스캔
//SpringBoot를 사용하면 실질적으로 @ComponentScan 어노테이션을 사용할 필요가 없음
@SpringBootApplication
public class OopApplication {

	public static void main(String[] args) {
		SpringApplication.run(OopApplication.class, args);
	}

}
