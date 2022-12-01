package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

class BeanValidationTest {

    //Spring을 사용하지 않는 javax만 사용한 순수 테스트

    @Test
    void beanValidationTest(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //Validation은 javax.validation의 클래스 - 생성자에서 static으로 생성하므로 바로 사용 가능
        //ValidatorFactory 는 javax.validation의 인터페이스 - getValidator() 메서드를 가지고 있음
        // ** Spring 사용 시, 별도로 생성해줄 필요 없음

        Validator validator = factory.getValidator();
        // *** 여기에서의 Validator는 javax.valdation의 인터페이스
        // cf) ItemValidator가 구현하는 Validator는 springframework.validation의 인터페이스

        //Item 객체에 오류값 설정
        Item item = new Item(); //default 생성자 존재
        item.setItemName(" "); //공백
        item.setPrice(0); //가격 0원
        item.setQuantity(10000); //수량 10000개

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        for (ConstraintViolation<Item> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation.getMessage() = " + violation.getMessage());
        }
    }
}
