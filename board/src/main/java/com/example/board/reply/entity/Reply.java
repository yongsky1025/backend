package com.example.board.reply.entity;

import com.example.board.post.entity.BaseEntity;
import com.example.board.post.entity.Board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = { "board" })
@Getter
@Table(name = "replytbl")
@Entity
public class Reply extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long rno;

    @Column(nullable = false)
    private String text;

    private String replyer;

    // Board
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bno")
    private Board board;

}
