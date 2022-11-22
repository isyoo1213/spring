package spring.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();
    //Multi Thread 환경에서 static 인스턴스에 동시에 접근할 경우 HashMap으로 생성한 경우 동시성 문제 생김
    //ConcurrentHashMap<> 사용해야함

    private static long sequence = 0L;
    //Long, long - Wrapper vs Primative
    //동시성 이슈 - Atomic Long과 같이 다른 자료형 사용해야함

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values()); //ArrayList<> 컬렉션으로 감싸서 반환하면 static인 store 인스턴스에 부작용 감소
    }

    public void update(Long id, Item updateParam){
        Item findItem = findById(id);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
        //실제로는 Update용 클래스(ItemParamDto)를 만들어 활용하는 것이 편리 - id는 update에 사용되지 않고 변경 부작용 감소
        // -> 중복 vs 명확성 -> 명확성이 더 중요함
    }

    //Test용 데이터 날리기
    public void clearStore(){
        store.clear();
    }
}
