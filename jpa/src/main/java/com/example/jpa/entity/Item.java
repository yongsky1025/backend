package com.example.jpa.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.jpa.entity.constant.ItemSellStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 테이블명 : itemtbl
// 컬럼 : 상품코드: Id(code - P0001), 상품명(item_nm), 가격(item_price), 재고수량(stock_number)
// 상세설명(item_detail), 판매상태(item_sell_status) : SELL, SOLDOUT
// 등록시간, 수정시간


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString

@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "itemtbl")
@Entity
public class Item {

    @Id
    private String code;

    @Column(nullable = false)
    private String itemNm;

    @Column(nullable = false)
    private int itemPrice;

    @Column(nullable = false)
    private int stockNumber;

    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    // 메서드
    // 판매상태변경
    public void changeStatus(ItemSellStatus itemSellStatus){
        this.itemSellStatus = itemSellStatus;
    }
    // 재고수량변경
    public void changeStockNumber(int stockNumber){
        this.stockNumber = stockNumber;
    }
    
}
