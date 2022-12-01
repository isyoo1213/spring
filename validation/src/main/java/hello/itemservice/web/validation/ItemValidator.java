package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component //Bean으로 등록해버리고 Controller가 존재하는 클래스에 의존성 주입시켜버림
public class ItemValidator implements Validator {

    // *** 현재 Validator 인터페이스를 구현하고 있지만 supports() 메서드를 사용하고 있지 않고, validate()만 사용
    // -> Validator를 구현하지 않고 오버라이드 하지 않은 validate()메서드를 사용 + Bean등록하지 않고 독립적인 클래스로 new 인스턴스화해서 사용할 수도 있음

    //Parameter로 넘어오는 클래스가 Item 클래스인지 검증하고 통과시키는 역할
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        // item == clazz
        // item을 상속받는 subItem - Item 클래스의 자식 클래스도 검증 통과가능 하도록
    }

    //검증 로직
    @Override
    public void validate(Object target, Errors errors) { //validate는 인터페이스
    //인자1 - bindingResult의 target이 되는 클래스, 즉 @ModelAttribute로 받아오는 Data 클래스 형태로 기대되는 인스턴스화된 Data
    //인자2 - Errors -> BindingResult의 부모 클래스 - bindingResult 할당 가능 + rejectValue() 메서드도 가지고 있음

        Item item = (Item) target; // *** 캐스팅 필요함 + 타입에러를 일으키는 데이터 입력시에도 Null로 매핑하고 typeMismatch처리하므로 안전

        if(!errors.hasErrors()){

            //검증 로직
            if(!StringUtils.hasText(item.getItemName())){
                errors.rejectValue("itemName", "required");
            }
            if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
                errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
            }
            if(item.getQuantity() == null || item.getQuantity() <= 0|| item.getQuantity() > 9999){
                errors.rejectValue("quantity", "max", new Object[]{9999}, null);
            }

            //특정 필드가 아닌 복합 룰 검증
            if(item.getPrice() != null && item.getQuantity() != null){
                int resultPrice = item.getPrice() * item.getQuantity();
                if(resultPrice < 10000){
                    errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
                }
            }

        }

    }
}
