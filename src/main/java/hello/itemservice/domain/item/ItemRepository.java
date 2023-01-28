package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>(); // Thread-safe 하지않음. 여러 쓰레드가 동시에 접근하는 경우, HashMap이 아닌 ConcurrentHashMap을 사용해야 함.
    private static long sequence = 0L;  // Thread-safe 하게 사용하려면, AtomicLong 을 사용하자.

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values()); // Depp Copy 로 방어적 복사하여 return
    }

    // [중복이냐? 명확성이냐?를 고려할 땐 항상 명확성을 중시하는 쪽으로 설계하는 것이 낫다.]
    // 예제라서 Entity를 그대로 재사용했지만, 실제로는 updateParam 정보만 필드로 가지는 Dto를 생성해서 사용한다.
    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }

}
