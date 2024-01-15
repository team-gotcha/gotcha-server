package com.gotcha.server.mongo.service;

import com.gotcha.server.mongo.domain.Book;
import com.gotcha.server.mongo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public void test() {
        Book book = new Book();
        book.setId("1");
        book.setData("test");
        book.setTime("2022-04-17");
        bookRepository.insert(book);
    }
}
