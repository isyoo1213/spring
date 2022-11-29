package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    //@PostMapping("/add")
    //점진적으로 V1부터 바꿔갈 예정
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // *** BindingResult는 위치가 중요 - @ModelAttribute 뒤에 와야함 - 즉, 쿼리스트링이 매핑되는 객체에 에러를 바인딩함
        // *** BindingResult 입장에서의 오류 2가지
        // 1. 객체 바인딩 자체에서의 실패 오류
        // 2. 비즈니스와 관련된 검증 로직에서의 오류

        // *** BindingResult 인터페이스는 Errors 인터페이스를 상속받음
        // 실제 구현체 - BeanPropertyBindingResult -> BindingResult와 Errors 둘 모두 구현
        // -> BindingResult 대신 Errors 사용도 가능 but, addErrors()와 같은 몇몇 메서드가 누락되어있음
        // *** Errors 인터페이스는 단순한 오류 저장과 조회 기능만 제공 -> 관례상 BindingResult를 많이 사용

        //검증 오류 결과를 보관 -> 이제 Error를 관리하는 방식이 달라짐
        //Map<String, String> errors = new HashMap<>();

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            //errors.put("itemName", "상품 이름은 필수입니다.");
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
            //Item 클래스의 필드 단위의 정보에서의 error -> FieldError (ObjectError의 자식)
            //인자1 - ObjectName - ModelAttribute에 담기는 객체의 이름 == 주로 Parameter로 전달되는 변수 이름
            //인자2 - FieldName - error를 바인딩 할 객체 내 필드 이름
            //인자3 - defaultMessage - 오류 시 출력할 기본 내용 - 이후 다르게 설정하는 것 존재
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            //errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() <= 0|| item.getQuantity() > 9999){
            //errors.put("quantity", "수량은 1 ~ 9,9999까지 허용합니다.");
            bindingResult.addError(new FieldError("item", "quantity", "수량은 1 ~ 9,9999까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                //errors.put("globalError", "총 금액이 10,000원 이상이어야 합니다. 현재값 = " + resultPrice);
                //복합 룰 검증의 경우 -> ObjectError (특정 필드가 아닌 Object의 단위에서의 오류)
                //인자1 - 복합적으로 사용되는 객체의 이름
                //인자2 - Message
                bindingResult.addError(new ObjectError("item", "총 금액이 10,000원 이상이어야 합니다. 현재값 = " + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로 이동
        if(
                //!errors.isEmpty()
                //기존의 Map에 담긴 errors를 bindingTest 인스턴스에서 탐색
                bindingResult.hasErrors()
        ){
            //log.info("errors = {}", errors);
            log.info("errors ={}", bindingResult); //bindingResult 자체를 로그로 출력

            //이제 model에 errors를 담아서 View에 넘겨준 후 출력할 필요 없음 -> BindingResult는 자동으로 View로 오류를 넘김
            //model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        //검증 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //FieldError와 ObjectError의 구체적 내용
    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            // *** FieldError의 2번째 생성자의 추가적인 arguments들
            //1. rejectedValue - 사용자가 입력한 binding에 실패한 값을 저장 -> 브라우저에서 사용자 입력값을 그대로 남겨둘 수 있음
            //2. bindingFailure - Type오류와 같이 쿼리스트링의 객체 binding 자체에 실패했는지의 여부 (검증 로직에서의 실패가 아닌)
            // ->  *** Type오류일 경우, 브라우저에서의 rejectedValue는 유지되지만
            // 오류 조건에 따른 FieldError의 defaultMessage가 아닌 Exception 오류 메세지를 뱉는 새 인스턴스 생성 후 컨트롤러 호출
            //3. codes, arguments

            //FieldError는 사용자가 입력한 값을 들고 있는 이유
            // - Request를 통한 요청 파라미터 가져오면 -> '컨트롤러에서 로직을 실행하기 전에, 즉 Item 객체 생성 전에'
            // -> bindingResult 객체 생성해서 rejectedValue에 값을 보존 -> 이후에 컨트롤러 호출
            // ex 타입에러일 경우) new FieldError("item", "itemName", "qqqq", true, null, null, "상품 이름은 필수입니다.")
            // -> rejectedValue에 "qqq", bindingFailure에 true를 통해 기존의 검증 로직 에러와 다른 객체를 만듦

            //Thymeleaf에서도 2번째 생성자 사용 + 오류 로직일 경우 th:field="*{필드명}"에서 rejecttedValue 값을 불러들이도록 설계 됨
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() <= 0|| item.getQuantity() > 9999){
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 1 ~ 9,9999까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                // *** ObjectError의 2번째 생성자의 추가적인 arguments들
                //1. codes, arguments
                bindingResult.addError(new ObjectError("item", null, null, "총 금액이 10,000원 이상이어야 합니다. 현재값 = " + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로 이동
        if(
                bindingResult.hasErrors()
        ){
            log.info("errors ={}", bindingResult); //bindingResult 자체를 로그로 출력
            return "validation/v2/addForm";
        }

        //검증 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}