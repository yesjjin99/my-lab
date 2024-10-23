package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional
    public void updateItem(Long itemId, UpdateItemDto itemDto) {
        Item findItem = itemRepository.findOne(itemId);  // 실제 DB에 있는 영속상태 엔티티
        findItem.setPrice(itemDto.getPrice());
        findItem.setName(itemDto.getName());
        findItem.setStockQuantity(itemDto.getStockQuantity());
        // itemRepository.save() 호출할 필요 없이 영속상태의 엔티티를 변경한 것이므로 dirty checking 이 일어나고 트랜잭션이 커밋되어 업데이트된다!
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
