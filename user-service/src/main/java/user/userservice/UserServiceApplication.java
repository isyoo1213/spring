package user.userservice;
//해당 패키지 내의 Component들만 스캔함 - 아래 @SpringBootApplication 어노테이션을 들어가보면 @ComponentScan 확인 가능

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
