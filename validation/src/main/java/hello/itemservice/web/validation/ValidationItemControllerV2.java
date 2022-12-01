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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
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
    private final ItemValidator itemValidator; //생성자 1개이므로 @AutoWired 삭제 가능 + final --> 생성자 주입

    @InitBinder
    public void init(WebDataBinder dataBinder){
        //요청이 올 때마다 WebDataBinder가 내부적으로 새로 만들어짐 + validator를 가지고 있음
        // + 컨트롤러의 어느 메서드가 실행되든 validator를 들고 검증할 수 있음
        // + Global 설정은 application에서 SpringMVCConfigurer 구현해서 설정해야함 - 잘안쓰임
        dataBinder.addValidators(itemValidator);
    }

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
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            // *** FieldError의 2번째 생성자의 추가적인 arguments들
            //1. rejectedValue - 사용자가 입력한 binding에 실패한 값을 저장 -> 브라우저에서 사용자 입력값을 그대로 남겨둘 수 있음
            //2. bindingFailure - Type오류와 같이 쿼리스트링의 객체 binding 자체에 실패했는지의 여부 (검증 로직에서의 실패가 아닌)
            // ->  *** Type오류일 경우, 브라우저에서의 rejectedValue는 유지되지만
            // 오류 조건에 따른 FieldError의 defaultMessage가 아닌 Exception 오류 메세지를 뱉는 새 인스턴스 생성 후 컨트롤러 호출
            //3. codes, arguments - messageSource와 연동해 properties에서 값 가져오기

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

    // FieldErrors의 2번째 생성자의 arguments - codes, arguments 살펴보기
    // 1. codes - String 배열으로 값을 넣어줘야함 - properties에서 해당 문자열을 순차적으로 찾아서 메세지 탐색
    // (못찾을경우 + defaultMessage없을 경우에는 서버오류발생)
    // 2. arguments - Object 배열로 값을 넘겨줘야함
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //BindingResult는 대상 객체 파라미터 뒤에 오기 때문에 이미 대상을 알고있다
        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("targetName = {}", bindingResult.getTarget());

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }
        if(item.getQuantity() == null || item.getQuantity() <= 0|| item.getQuantity() > 9999){
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
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

    //BindingResult의 rejectValue() 사용하기 ( ObjectError는 reject() ) - 결론적으로는 FieldError나 ObjectError를 생성하긴 함
    //인자1 - 필드 이름
    //인자2 - errorCode - 기존 FieldError()의 codes에서 error.properties에 정의된 모든 경로를 적어야 했지만, 패키지처럼 구성되게끔 앞부분만 작성
    //-> errorCode.objectName.field 의 이름으로 기존의 codes처럼 구성하도록 설계됨 by *** MessageCodesResolver
    //-> *** 실제로는 낮은 우선순위의 범용성 매핑 (errorCode)과 높은 우선순위의 세밀한 매핑 (errorCode.objectName.field)이 모두 생성되어 적용
    // ex) new String[]{"range.item.price", "range"}
    // *** 타입 에러의 경우, field가 typeMismatch로 치환된 errorCodes가 자동으로 생성되어 스프링이 제공하는 타입에러 메시지와 함께 + ** Null이 바인딩 되면서 field 오류 메시지도 같이 불러온다
    // ex) codes [typeMismatch.item.price,typeMismatch.price,typeMismatch.java.lang.Integer,typeMismatch] 를 자동 생성해서 가지고 있음
    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //타입 에러시 binding과정에 Null로 인한 field 오류 메시지를 지우는 로직
        // -> 검증 로직을 거치지 않고, bindingResult가 자동으로 생성하는 typeMismatch 오류를 바로 반환함
        // *** 대신 검증 로직 전부 진행되지 않고 Type체크 오류만 진행함 - 아닐 경우 검증 로직에서 if 조건을 사용해야 함
        if(bindingResult.hasErrors()){
            log.info("errors ={}", bindingResult);
            return "validation/v2/addForm";
        }

        //BindingResult는 대상 객체 파라미터 뒤에 오기 때문에 이미 대상을 알고있다
        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("targetName = {}", bindingResult.getTarget());

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            //bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
            bindingResult.rejectValue("itemName", "required");
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            //bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        if(item.getQuantity() == null || item.getQuantity() <= 0|| item.getQuantity() > 9999){
            //bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                //bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
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

    //Validator 사용하기
    //@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

//        if(bindingResult.hasErrors()){
//            log.info("errors ={}", bindingResult);
//            return "validation/v2/addForm";
//        } //validate에 넘기는 @ModelAttribute Item item 파라미터는, 아직 Item 클래스로 캐스팅 되기 전의 인스턴스. 즉, target 형태

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("targetName = {}", bindingResult.getTarget());

        // *** 현재 Validator 인터페이스를 구현하고 있지만 supports() 메서드를 사용하고 있지 않고, validate()만 사용
        // -> Validator를 구현하지 않고 오버라이드 하지 않은 validate()메서드를 사용
        // + Bean등록하지 않고 독립적인 클래스로 new 인스턴스화해서 사용할 수도 있음 (ItemValidator 클래스 또한 인터페이스, 오버라이드 제거해야함)
        // ex) ItetmValidator itemValidator = new ItemValidator(item, bindingResult)
        // but, 스프링이 대신 validate()자체를 호출시키기 위해 인터페이스 적용한 상태로 클래스 구성함

        itemValidator.validate(item, bindingResult);

/*
        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.rejectValue("itemName", "required");
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        if(item.getQuantity() == null || item.getQuantity() <= 0|| item.getQuantity() > 9999){
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
*/

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

    //@Validated를 통한 dataBinder 사용
    @PostMapping("/add")
    public String addItemV6(
            @Validated //해당 어노테이션을 통해 Item ittem에 대한 validator가 자동으로 수행 -> 결과를 bindingResult에 담음
            // + dataBinder에 등록된 validator가 여러개일 경우 -> validator의 supports() 메서드를 통해 선택 -> Validator 인터페이스를 쓰는 이유
            @ModelAttribute Item item,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("targetName = {}", bindingResult.getTarget());

        //itemValidator.validate(item, bindingResult);

        //검증에 실패하면 다시 입력 폼으로 이동
        if(bindingResult.hasErrors()){
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