package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
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
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

    @PostMapping("/add")
    public String addItem(
            @Validated
            @ModelAttribute("item") ItemSaveForm form,
            //Model에 담길 때, Data 클래스의 앞글자를 소문자로 한 itemSaveForm으로 설정되므로, 기존의 뷰 템플릿에서 처리할 수 있도록 설정
            //ex) model.addAttrtibute("itemSaveForm", form);
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("targetName = {}", bindingResult.getTarget());

        //특정 필드가 아닌 복합 룰 검증
        //객체 오류 검증은 @ScriptAssert보다 직접 이렇게 코드 짜는 것 + 메서드로 뽑는 방식이 더욱 바람직
        if(form.getPrice() != null && form.getQuantity() != null){
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로 이동
        if(bindingResult.hasErrors()){
            log.info("errors ={}", bindingResult);
            return "validation/v4/addForm";
        }

        //검증 성공 로직

        //data용 객체를 Repository가 사용할 수 있도록 domain객체를 생성해서 변환
        Item item = new Item();

        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    //확인할 부분 - 수정 시 itemId NotNull 적용했지만 -> 등록 시 itemId가 Null이어도 정상 작동하게 변경된 부분
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,
                         @Validated
                         @ModelAttribute("item") ItemUpdateForm form, //data를 받아올 객체와 model에 저장할 이름 설정
                         BindingResult bindingResult) {

        //특정 필드가 아닌 복합 룰 검증
        if(form.getPrice() != null && form.getQuantity() != null){
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v4/editForm";
        }

        //Repository에서 update메서드를 위한 updateParam 파라미터(Item 클래스형)으로 data 객체 변환
        Item updateParam = new Item();

        updateParam.setItemName(form.getItemName());
        updateParam.setPrice(form.getPrice());
        updateParam.setQuantity(form.getQuantity());

        itemRepository.update(itemId, updateParam);
        return "redirect:/validation/v4/items/{itemId}";
    }

}