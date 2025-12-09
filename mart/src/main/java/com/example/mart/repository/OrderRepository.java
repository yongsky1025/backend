package com.example.mart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mart.entity.Member;
import com.example.mart.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // select * from orders where member_id = 2;
    Optional<Order> findByMember(Member member);
}
