package com.example.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.book.entity.Book;
import com.example.book.entity.QBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book> {

    Optional<Book> findByIsbn(String isbn); // = where isbn = 'A1000'

    // List<Book> findByTitleLike(String title); // => where title Like = '자바'

    List<Book> findByTitleContaining(String title); // => where title Like = '%자바%'

    // wehre author = ''
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    List<Book> findByAuthor(String author);

    // where author like '%영'
    List<Book> findByAuthorEndingWith(String author);

    // where author like '박%'
    List<Book> findByAuthorStartingWith(String author);

    // where author like '%진수%'
    List<Book> findByAuthorContaining(String author);

    // 도서가격이 12000 이상 35000 이하
    List<Book> findByPriceBetween(int startPrice, int endPrice);

    // 검색기능 부문
    public default Predicate makePredicate(String type, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();
        QBook book = QBook.book;

        builder.and(book.id.gt(0));
        return builder;
    }

}
