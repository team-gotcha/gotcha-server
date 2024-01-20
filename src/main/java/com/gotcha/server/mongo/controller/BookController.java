package com.gotcha.server.mongo.controller;

import com.gotcha.server.applicant.service.ApplicantService;
import com.gotcha.server.member.repository.MemberRepository;
import com.gotcha.server.mongo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/test")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<String> test() {
        bookService.test();
        return ResponseEntity.status(HttpStatus.CREATED).body("몽고 db에 잘 입력되었습니다.");
    }
}
