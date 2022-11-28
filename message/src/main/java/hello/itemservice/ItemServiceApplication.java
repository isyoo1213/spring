package hello.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}
	/*
	//스프링이 제공하는 MessageSource 인터페이스
	//SpringBoot는 MessageSource를 자동으로 Bean등록한다 + application.properties에서 세부 설정 가능
	//ex) spring.messages.basename=messages,config.i18n.messages
	// 기본경로 - Resources -> 아래의 config / i18n / messages.properties를 파일로 사용한다는 의미
	// * 별도의 Bean등록이나 스프링부트의 설정을 해주지 않는 경우의 Default 설정
	// -> spring.messages.basename=messages
	// -> 파일명_국가.properties 로 설정 정보 추가해주면 자동으로 국제화 설정 인식
	//ex) messages_en.properties
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new
				ResourceBundleMessageSource();
		messageSource.setBasenames("messages", "errors");
		//각각의 설정파일의 이름 설정 - 설정파일.properties 파일을 읽어들임
		messageSource.setDefaultEncoding("utf-8");
		return messageSource;
	}
*/
}
