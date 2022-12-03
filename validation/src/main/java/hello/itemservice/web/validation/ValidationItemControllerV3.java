package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

    //@PostMapping("/add")
    public String addItem(
            @Validated
            //현재 Item 필드에 Validator 관련 어노테이션이 적용됨
            // -> 스프링부트가 라이브러리 확인 후 자동으로 Bean Validator를 인지하고 스프링에 통합해버림
            // - 스프링 부트는 LocalValidatorFactoryBean를 글로벌 Validator로 등록
            // -> Item 필드의 @NotNull과 같은 어노테이션 감지하고 검증 수행 -> 검증 결과는 bindingResult에 담음
            // -> 이렇게 Global Validator가 적용되어있기 때문에 @Valid(자바표준), @Validated(스프링전용)만 적용하더라도 검증이 수행됨
            // *** 만약 직접 Global Validator를 적용한다면(by WebMvcConfigurer 구현) Bean Validator를 기본 글로벌 검증기로 적용하지 않음
            // -> 어노테이션 기반의 검증이 수행되지 않음

            //*** 검증 순서
            //1. @ModelAttribute의 각각 필드에 Type 변환 시도 -> 실패 시 typeMismatch로 FieldError 추가 -> *** Bean Validation 적용X
            //2. Validator 적용 -> ***** Binding에 성공한 필드만 Bean Validation 적용

            //*** 검증 원리
            //@NotNull 과 같이 설정된 어노테이션 이름을 기반으로 errorCode를 만듦( ex) NotNull.item.itemName )
            @ModelAttribute Item item,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("targetName = {}", bindingResult.getTarget());

        //특정 필드가 아닌 복합 룰 검증
        //객체 오류 검증은 @ScriptAssert보다 직접 이렇게 코드 짜는 것 + 메서드로 뽑는 방식이 더욱 바람직
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로 이동
        if(bindingResult.hasErrors()){
            log.info("errors ={}", bindingResult);
            return "validation/v3/addForm";
        }

        //검증 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    //Bean Validation (@Validated) 의 Groups 사용
    @PostMapping("/add")
    public String addItemV2(
            @Validated(value = SaveCheck.class)
            //사용할 Groups 지정 (Value = 은 생략 가능) -> Domain에 설정한 Group만 활성화
            @ModelAttribute Item item,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("targetName = {}", bindingResult.getTarget());

        //특정 필드가 아닌 복합 룰 검증
        //객체 오류 검증은 @ScriptAssert보다 직접 이렇게 코드 짜는 것 + 메서드로 뽑는 방식이 더욱 바람직
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로 이동
        if(bindingResult.hasErrors()){
            log.info("errors ={}", bindingResult);
            return "validation/v3/addForm";
        }

        //검증 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

    //@PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

    //확인할 부분 - 수정 시 itemId NotNull 적용했지만 -> 등록 시 itemId가 Null이어도 정상 작동하게 변경된 부분
    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId,
                         @Validated(UpdateCheck.class)
                         @ModelAttribute Item item,
                         BindingResult bindingResult) {

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}