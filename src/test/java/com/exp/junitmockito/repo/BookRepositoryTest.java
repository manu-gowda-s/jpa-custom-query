package com.exp.junitmockito.repo;

import com.exp.junitmockito.entity.Book;
import com.exp.junitmockito.repositrory.BookRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("this should all the books list")
    public void findAllShouldReturnAllTheBooks(){

        Book book = Book.builder()
                .bookId(1)
                .bookName("Wings of Fire")
                .summary("the book written by APJ")
                .ratings(6)
                .build();
        Book book1 = bookRepository.save(book);

        Assertions.assertEquals(book1.getBookName(),"Wings of Fire");
        assertThat(book1.getBookId()).isGreaterThan(0);

    }
}
