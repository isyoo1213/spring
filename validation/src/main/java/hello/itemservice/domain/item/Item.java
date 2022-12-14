package hello.itemservice.domain.item;

import lombok.Data;

// hibernate 어노테이션은 구현체인 hibernate 구현체에 종속적으로 동작
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

// javax, 즉 java 표준 기술에 의한 것으로 어느 구현체에서도 동작 가능
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {

//    @NotNull(groups = UpdateCheck.class)
    private Long id;

//    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class},
//            message = "공백X") //이 오류 메시지 출력은 javax, 즉 Spring을 사용하지 않은 상태로 사용 가능
    private String itemName;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
//    @Range(groups = {SaveCheck.class, UpdateCheck.class},
//            min = 1000, max = 1000000)
    private Integer price;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
//    @Max(value = 9999, groups = SaveCheck.class)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
