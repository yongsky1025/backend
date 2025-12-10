package com.example.jpa.repository;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.jpa.entity.Item;
import com.example.jpa.entity.Member;
import com.example.jpa.entity.constant.ItemSellStatus;
import com.example.jpa.entity.constant.RoleType;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void insertTest() {
        for (int i = 1; i < 11; i++) {
            Item item = Item.builder()
                    .code("P00" + i)
                    .stockNumber(10)
                    .itemPrice(1000 * i)
                    .itemDetail("Item Detail" + i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .itemNm("Item" + i)
                    .build();
            itemRepository.save(item);
        }
    }

    @Test
    public void updateTest() {
        // item 상태
        Optional<Item> result = itemRepository.findById("P005");

        result.ifPresent(item -> {
            item.changeStatus(ItemSellStatus.SOLDOUT);

            itemRepository.save(item);
        });

    }

    @Test
    public void updateTest2() {
        // 재고수량 변경
        Optional<Item> result = itemRepository.findById("P0010");

        result.ifPresent(item -> {
            item.changeStockNumber(30);

            itemRepository.save(item);
        });

    }

    @Test
    public void deleteTest() {
        itemRepository.findById("P008");
        // itemRepository.deleteAll();
    }

    @Test
    public void readTest() {
        System.out.println(itemRepository.findById("P009").get());
    }

    @Test
    public void readTest2() {
        itemRepository.findAll().forEach(item -> System.out.println(item));

    }

    @Test
    public void aggrTest() {
        List<Object[]> result = itemRepository.aggr();

        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects)); // [10, 55000, 5500.0, 10000, 1000]

            System.out.println("아이템 수 " + objects[0]);
            System.out.println("합계 " + objects[1]);
            System.out.println("평균 " + objects[2]);
            System.out.println("최대 " + objects[3]);
            System.out.println("최소 " + objects[4]);

        }
    }
}
