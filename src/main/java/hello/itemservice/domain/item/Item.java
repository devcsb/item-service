package hello.itemservice.domain.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Data // 도메인 모델에 @Data 사용 금지. DTO에서만 선택적으로 사용하도록 하자. https://kwonnam.pe.kr/wiki/java/lombok/pitfall
@Getter
@Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price; // null로 들어갈 가능성이 있는 상황이므로, int가 아닌 Integer 타입을 사용
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
