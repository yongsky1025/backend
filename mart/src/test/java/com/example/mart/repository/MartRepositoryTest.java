package com.example.mart.repository;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.mart.entity.Category;
import com.example.mart.entity.CategoryItem;
import com.example.mart.entity.Delivery;
import com.example.mart.entity.Item;
import com.example.mart.entity.Member;
import com.example.mart.entity.Order;
import com.example.mart.entity.OrderItem;
import com.example.mart.entity.constant.DeliveryStatus;
import com.example.mart.entity.constant.OrderStatus;

// @Disabled
@SpringBootTest
public class MartRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryItemRepository categoryItemRepository;

    @Test
    public void insertMemberTest() {
        // 5 명의 Member 삽입
        IntStream.rangeClosed(1, 5).forEach(i -> {

            Member member = Member.builder()
                    .name("맴버 " + i)
                    .city("서울")
                    .street("724-1" + i)
                    .zipcode("1650" + i)
                    .build();

            memberRepository.save(member);
        });
    }

    @Test
    public void insertItemTest() {
        // 5 명의 Item 삽입
        IntStream.rangeClosed(1, 5).forEach(i -> {

            Item item = Item.builder()
                    .name("item " + i)
                    .price(250000)
                    .quantity(i * 5)
                    .build();

            itemRepository.save(item);
        });
    }

    @Test
    public void orderTest() {
        // 주문

        // 1번 상품을 2번 고객이 주문

        Member member = memberRepository.findById(2L).get();
        Item item = itemRepository.findById(1L).get();

        Order order = Order.builder()
                .member(member)
                .OrderStatus(OrderStatus.ORDER)
                .build();
        orderRepository.save(order);

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .order(order)
                .orderPrice(200000)
                .count(1)
                .build();
        orderItemRepository.save(orderItem);
    }

    @Transactional(readOnly = true)
    @Test
    public void orderReadTest() {
        // 2번 고객 주문 내역 조회
        Member member = Member.builder().id(2L).build();
        Order order = orderRepository.findByMember(member).get();
        System.out.println(order);

        // 주문상품 정보
        // OrderItem(id=1, orderPrice=200000, count=1)
        order.getOrderItems().forEach(i -> {
            System.out.println(i);
            // 주문 상품 상세 정보 조회
            // Item(id=1, name=item 1, price=250000, quantity=5)
            System.out.println(i.getItem());
        });

        // 고객 상세 정보
        // Member(id=2, name=맴버 2, city=서울, street=724-12, zipcode=16502)
        System.out.println(order.getMember());
    }

    @Commit
    @Transactional
    @Test
    public void orderCascadeTest() {
        // 3번 고객이 2번 제품을 구매한다
        Member member = memberRepository.findById(3L).get();
        Item item = itemRepository.findById(2L).get();

        Order order = Order.builder()
                .member(member)
                .OrderStatus(OrderStatus.ORDER)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .order(order)
                .orderPrice(200000)
                .count(1)
                .build();

        // order.getOrderItems().add(orderItem);
        order.addOrderItem(orderItem);
        orderRepository.save(order);

        // orderItemRepository.save(orderItem);
    }

    @Commit
    @Transactional
    @Test
    public void testUpdate() {

        // 3번 고객의 city 변경
        Member member = memberRepository.findById(3L).get();
        member.changeCity("부산"); // managed entity 는 변경사항 감지 기능(dirty checking)

        // memberRepository save(member)

        // 3번 item 수량 변경 => 10
        Item item = itemRepository.findById(3L).get();
        item.changeQuantity(10);

        // Order 주문 상태 변경 => cancel
        Order order = orderRepository.findById(3L).get();
        order.changeOrderStatus(OrderStatus.CANCEL);
    }

    @Commit
    @Transactional
    @Test
    public void testDelete() {

        // 주문 제거
        // order, order_item 제거

        // 방법 1 : 자식삭제 => 부모삭제
        orderItemRepository.deleteById(3L);
        orderRepository.deleteById(3L);

    }

    @Commit
    @Transactional
    @Test
    public void testCascadeDelete() {

        // 주문 제거
        // order, order_item 제거

        // 방법 2 : 부모 삭제 시 자식 같이 삭제(sql = ON DELETE CASCADE)
        orderRepository.deleteById(6L);
    }

    @Commit
    @Transactional
    @Test
    public void testOrphanDelete() {

        // 주문 조회
        Order order = orderRepository.findById(7L).get();
        System.out.println(order); // Order(id=7, OrderStatus=ORDER)

        // 주문 아이템 조회
        // [OrderItem(id=7, orderPrice=200000, count=1)]
        System.out.println(order.getOrderItems());

        // 리스트에서
        order.getOrderItems().remove(0);
        System.out.println("삭제 후 " + order.getOrderItems()); // 삭제 후 []
        // orderRepository.save(order);
    }

    @Commit
    @Transactional
    @Test
    public void testDelivery() {
        // order
        // 3번 고객이 2번 제품을 구매한다
        Member member = memberRepository.findById(3L).get();
        Item item = itemRepository.findById(2L).get();

        Delivery delivery = Delivery.builder()
                .city("서울")
                .street("114")
                .zipcode("11061")
                .deliveryStatus(DeliveryStatus.COMP)
                .build();
        deliveryRepository.save(delivery);

        Order order = Order.builder()
                .member(member)
                .OrderStatus(OrderStatus.ORDER)
                .delivery(delivery)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                // .order(order)
                .orderPrice(230000)
                .count(1)
                .build();

        // order.getOrderItems().add(orderItem);
        order.addOrderItem(orderItem);
        orderRepository.save(order);

    }

    @Commit
    @Transactional
    @Test
    public void testCascadeDelivery() {
        // order
        // 3번 고객이 2번 제품을 구매한다
        Member member = memberRepository.findById(3L).get();
        Item item = itemRepository.findById(2L).get();

        Delivery delivery = Delivery.builder()
                .city("서울")
                .street("114")
                .zipcode("11061")
                .deliveryStatus(DeliveryStatus.COMP)
                .build();
        // deliveryRepository.save(delivery);

        Order order = Order.builder()
                .member(member)
                .OrderStatus(OrderStatus.ORDER)
                .delivery(delivery)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                // .order(order)
                .orderPrice(230000)
                .count(1)
                .build();

        // order.getOrderItems().add(orderItem);
        order.addOrderItem(orderItem);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    @Test
    public void orderReadTest2() {

        Order order = orderRepository.findById(11L).get();

        // order 조회
        System.out.println(order);

        // 고객 조회
        System.out.println(order.getMember());
        System.out.println(order.getMember().getName());

        // 주문제품 조회
        System.out.println(order.getOrderItems());
        System.out.println(order.getOrderItems().get(0));

        // 배송 조회
        System.out.println(order.getDelivery());
    }

    @Transactional(readOnly = true)
    @Test
    public void memberReadTest2() {

        Member member = memberRepository.findById(3L).get();

        // member 조회
        System.out.println(member);

        // 주문 조회
        System.out.println(member.getOrders());

        List<Order> orders = member.getOrders();
        orders.forEach(order -> {
            System.out.println(order.getDelivery());
            System.out.println(order.getMember());
            System.out.println(order.getOrderItems());
        });

    }

    @Transactional(readOnly = true)
    @Test
    public void orderItemReadTest2() {

        OrderItem orderItem = orderItemRepository.findById(8L).get();

        // orderItem 조회
        System.out.println(orderItem);

        // 주문 조회
        System.out.println(orderItem.getOrder());
        Order order = orderItem.getOrder();
        // 배송 조회
        System.out.println(order.getDelivery());
        // 고객 조회
        System.out.println(order.getMember());
        // 주문 상품 조회
        System.out.println(order.getOrderItems());

        // 상품 조회
        System.out.println(orderItem.getItem());
    }

    // ---------------------------------------
    // ManyToMany 설정을 JPA 에게 시킨 경우
    // 테스트 구문
    // ---------------------------------------

    @Test
    public void categoryTest() {

        Item item = itemRepository.findById(3L).get();

        Category category = Category.builder().name("가전제품").build();
        // category.getItems().add(item);
        categoryRepository.save(category);

        category = Category.builder().name("생활용품").build();
        // category.getItems().add(item);
        categoryRepository.save(category);

    }

    @Transactional(readOnly = true)
    @Test
    public void categoryReadTest() {

        Category category = categoryRepository.findById(1L).get();

        // 카테고리 조회
        System.out.println(category);

        // 카테고리에 속한 아이템 조회
        // System.out.println(category.getItems());
    }

    @Transactional(readOnly = true)
    @Test
    public void itemReadTest() {

        Item item = itemRepository.findById(3L).get();

        // 아이템 조회
        System.out.println(item);

        // 카테고리 조회
        // System.out.println(item.getCategories());
    }

    // ---------------------------------------
    // ManyToMany 설정을 ManyToOne 관계로 작성 후
    // 테스트 구문
    // ---------------------------------------

    @Test
    public void categoryTest2() {

        Item item = itemRepository.findById(4L).get();

        Category category = Category.builder().name("신혼용품").build();
        categoryRepository.save(category);

        CategoryItem categoryItem = CategoryItem.builder()
                .category(category)
                .item(item)
                .build();

        categoryItemRepository.save(categoryItem);

        category = categoryRepository.findById(1L).get();
        categoryItem = CategoryItem.builder()
                .category(category)
                .item(item)
                .build();

        categoryItemRepository.save(categoryItem);
    }

    @Transactional(readOnly = true)
    @Test
    public void categoryItemReadTest2() {

        CategoryItem categoryItem = categoryItemRepository.findById(1L).get();

        // 카테고리 아이템 조회
        System.out.println(categoryItem);

        // 카테고리 정보 조회
        System.out.println(categoryItem.getCategory());

        // 아이템 정보 조회
        System.out.println(categoryItem.getItem());

        // 양방향 연 뒤
        Category category = categoryRepository.findById(1L).get();
        System.out.println(category);
        System.out.println(category.getCategoryItems());

        Item item = itemRepository.findById(3L).get();
        System.out.println(item);
        System.out.println(item.getCategoryItems());
    }

}
