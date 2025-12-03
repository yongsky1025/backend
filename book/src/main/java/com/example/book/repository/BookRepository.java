package com.example.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn); // = where isbn = 'A1000'

    // List<Book> findByTitleLike(String title); // => where title Like = '자바'

    List<Book> findByTitleContaining(String title); // => where title Like = '%자바%'
}
