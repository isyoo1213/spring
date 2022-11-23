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
        return "basic/item";
    }

    //등록 Form과 등록 처리(save)를 같은 URL로 처리할 예정
    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    //위의 상품등록 단순 Form html의 뷰 네임을 반환하는 GET 메서드와 URL주소는 같고, 요청 메서드만 다르게 구성
    @PostMapping("/add")
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
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("ItemA", 10000, 10));
        itemRepository.save(new Item("ItemB", 20000, 20));
    }
}
