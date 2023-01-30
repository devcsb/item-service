package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

//@RequiredArgsConstructor // final, @NonNull이 붙은 필드에 대한 생성자를 만들어줌.
@Controller
@RequestMapping("/basic/items")
public class BasicItemController {

    private final ItemRepository itemRepository;

    /*    @Autowired  // 생성자가 하나만 있는 경우, @Autowired 생략 가능. 스프링이 자동으로 생성자에 @Autowired 를 붙여 의존성 주입을 해준다. */
    public BasicItemController(ItemRepository itemRepository) {   // Lombok 의 @RequiredArgsConstructor 사용으로 대체 가능
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * [ @ModelAttribute 의 역할]
     * 1. @ModelAttribute 는 지정한 인자 타입으로 된 객체(여기서는 Item)를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력해준다.
     * 2. 모델(Model)에 1에서 만든 객체를 자동으로 넣어준다.(model.addAttribute()).
     * 이 때, @ModelAttribute("item")과 같이, 선언할 때 지정한 Name 속성의 이름으로 값을 저장한다.
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {
        itemRepository.save(item);
        //model.addAttribute("item", item); // 자동으로 추가해주므로, 생략 가능
        return "basic/item";
    }

    /**
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item  (Argument 의 이름이 아니라 클래스명 기준으로 저장됨을 반드시 숙지!)
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {

        // ex) @ModelAttribute HelloData data   <-- 이런 식일 때,
        // model.addAttribute("helloData", data); 와 같이 데이터가 담긴다.

        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * @ModelAttribute 자체 생략 가능.
     * @ModelAttribute나 @RequestParam은 모두 생략 가능하다.
     * 이 때, 넘겨받은 인자의 타입이 Primitive Type이면 @ReqeustParam을 적용하고, 나머지 타입은 @ModelAttribute를 적용한다.(argument resolver 로 지정해둔 타입 외)
     */
    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}"; // 컨트롤러에 매핑된 @PathVariable 의 값은 redirect 에도 사용 할 수 있다.
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
