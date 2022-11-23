package spring.itemservice.web.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.itemservice.domain.item.Item;
import spring.itemservice.domain.item.ItemRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

//    @Autowired //생성자 1개일 경우 생략가능
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    } //@RequiredArgsConstructor일 경우 final로 선언된 변수에 의존관계 주입해주는 생성자 생성해줌

    @GetMapping("")
    public String items(Model model){
        List<Item> items =  itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model){
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);

// model 싱글톤인지 검증용 코드
//        Map<String, Object> modelMap =  model.asMap();
//        modelMap.forEach((key, value) -> {
//            System.out.println("key = " + key + ", value = " + value);
//        });

        return "basic/item";
    }

    //등록 Form과 등록 처리(save)를 같은 URL로 처리할 예정
    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    /**
     * AddItem V1
     */
    //위의 상품등록 단순 Form html의 뷰 네임을 반환하는 GET 메서드와 URL주소는 같고, 요청 메서드만 다르게 구성
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam Integer price,
                       @RequestParam Integer quantity,
                       Model model){
        //Item item = new Item(itemName, price, quantity);
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item", item);
        //Item 조회에서 model에 추가한 "item"과 key값이 겹침
        //-> 조회시 model에 담기는 "item"과 상품등록시 model에 담기는 "item"은 어떻게 구별하는것일까?
        //-> request 단위로 model 인스턴스가 생성되는 것 vs Singleton으로 관리되는 것 vs Singleton이지만 request단위로 전달되는것

        return "basic/item";
    }

    /**
     * AddItem V2
     */
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model){

        //***** @ModelAttribute
        ////스프링이 관리하는 Model 가져와서 알아서 사용 -> Model model을 Parameter로 넘길 필요 없음
        //1. Model 객체 생성 + setMethod() 호출로 쿼리스트링으로 받아온 값 할당 by 프로퍼티 접근법 (form에서 넘어오는 이름을 사용)
        //2. Model 사용시 다음 View에서 Model이 사용될 가능성이 큼 -> 설정한 String 값으로 model 객체에 addAttribute()로 저장
        //Item Data 클래스에 맞는 인스턴스 생성 후 setMethod() 호출 + 쿼리 스트링 key에 맞는 변수에 값 할당

        itemRepository.save(item);
        //model.addAttribute("item", item); //@ModelAttribute에서 자동추가 -> 생략가능

// model 싱글톤인지 검증용 코드
//        Map<String, Object> modelMap =  model.asMap();
//        modelMap.forEach((key, value) -> {
//            System.out.println("key = " + key + ", value = " + value);
//        });

        return "basic/item";
    }

    /**
     * AddItem V3
     */
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){//Model 파라미터 생략
        //@ModelAttritube의 key 밸류 지정하지 않을 경우, Data 클래스 이름의 앞글자를 소문자로 바꾼 문자가 key 값이 된다
        //@ModelAttritube가 생략된 것이므로, Data 클래스의 앞글자 소문자 문자열이 클래스 이름으로 model에 자동 등록

        itemRepository.save(item);

        return "basic/item";
    }

    /**
     * AddItem V4
     */
    @PostMapping("/add")
    public String addItemV4(Item item){
        // *** String과 같은 단순타입 -> @RequestParam 적용!

        itemRepository.save(item);

        return "basic/item";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("ItemA", 10000, 10));
        itemRepository.save(new Item("ItemB", 20000, 20));
    }
}
