package com.example.book.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.example.book.dto.BookDTO;
import com.example.book.entity.Book;
import com.example.book.entity.QBook;

import jakarta.persistence.EntityNotFoundException;

@Disabled
@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testInsert() {

        // Book book = new Book();

        Book book = Book.builder()
                .isbn("A101010")
                .title("파워 자바")
                .author("천인국")
                .price(36000)
                .build();

        bookRepository.save(book);
    }

    @Test
    public void testInsert2() {

        IntStream.rangeClosed(0, 9).forEach(i -> {
            Book book = Book.builder()
                    .isbn("A101010" + i)
                    .title("파워 자바" + i)
                    .author("천인국" + i)
                    .price(36000)
                    .build();

            bookRepository.save(book);
        });
    }

    @Test
    public void testRead() {
        // bookRepository.findById(1L).orElse(null);
        Book book = bookRepository.findById(1L).orElseThrow();
        // bookRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        // Optional<Book> result = bookRepository.findById(1L);
        // if (result.isPresent()) {
        // Book book = result.get();
        // }
        System.out.println(book);
    }

    @Test
    public void testRead2() {

        Book book = bookRepository.findByIsbn("A1010103").orElseThrow();
        System.out.println(book);

        List<Book> list = bookRepository.findByTitleContaining("파워");
        System.out.println(list);
    }

    @Test
    public void testModify() {

        Book book = bookRepository.findById(1L).orElseThrow();

        book.changePrice(30000);

        System.out.println(bookRepository.save(book));
    }

    @Test
    public void testDelete() {

        bookRepository.deleteById(10L);

    }

    @Test
    public void testFindBy() {

        List<Book> list = bookRepository.findByAuthor("천인국1");
        System.out.println("findByAuthor(천인국1)" + list);

        list = bookRepository.findByAuthorEndingWith("4");
        System.out.println("findByAuthorEndingWith(%4)" + list);

        list = bookRepository.findByAuthorStartingWith("천인");
        System.out.println("findByAuthorStartingWith(천인%)" + list);

        list = bookRepository.findByAuthorContaining("인");
        System.out.println("findByAuthorContaining(%인%)" + list);

        list = bookRepository.findByPriceBetween(12000, 35000);
        System.out.println("findByPriceBetween(12000~35000)" + list);
    }

    @Test
    public void pageTest() {
        // bookRepository.findAll(pageable pageable);
        // limit ?, ? : 특정 범위만 가져오기
        // select count(b1_0.id) : 전체 행의 개수
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Book> result = bookRepository.findAll(pageRequest);

        System.out.println("page size " + result.getSize());
        System.out.println("TotalPages " + result.getTotalPages());
        System.out.println("page Elements(전체 행 개수) " + result.getTotalElements());
        System.out.println("page Content " + result.getContent());
    }

    // -----------------------
    // querydsl 라이브러리 추가 / QuerydslPredicate
    // -----------------------

    @Test
    public void querydslTest() {

        QBook book = QBook.book;

        // eq()
        // where b1_0.title=?
        System.out.println(bookRepository.findAll(book.title.eq("파워 자바1")));
        // contains()
        // where b1_0.title like ? escape '!'
        System.out.println(bookRepository.findAll(book.title.contains("파워")));
        // contains().and
        // where b1_0.title like ? escape '!'
        // and b1_0.id>?
        System.out.println(bookRepository.findAll(book.title.contains("파워").and(book.id.gt(3))));

        // contains().and, Sort.by().descending()
        // where
        // b1_0.title like ? escape '!'
        // and b1_0.id>?
        // order by
        // b1_0.id desc
        System.out.println(bookRepository.findAll(book.title.contains("파워")
                .and(book.id.gt(3)), Sort.by("id").descending()));

        // contains().or
        // where
        // b1_0.title like ? escape '!'
        // or b1_0.author like ? escape '!'
        // where author'%천%' or title = '%파워%'
        System.out.println(bookRepository.findAll(book.title.contains("파워").or(book.author.contains("천"))));

        // bookRepository.findAll(pageable pageable);
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Book> result = bookRepository.findAll(book.id.gt(0L), pageRequest);

    }

    @Test
    public void querydslTest2() {

        bookRepository.makePredicate("t", "파워");

    }
}
