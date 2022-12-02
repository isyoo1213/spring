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
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총 금액이 10,000원 이상이어야 합니다.")
//객체 검증시 @ScriptAssert를 사용할 수 있으나, 제약이 많고 실제로는 훨씬 더 까다로운 로직의 검증이 필요한 경우가 많음 -> 사용 X
// errorCode ScriptAssert.item 등을 사용할 수도 있긴 함
public class Item {

    private Long id;

    @NotBlank(message = "공백X") //이 오류 메시지 출력은 javax, 즉 Spring을 사용하지 않은 상태로 사용 가능
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
