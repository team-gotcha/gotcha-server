package com.gotcha.server.mongo.repository;

import com.gotcha.server.mongo.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends MongoRepository<Book,String> {

}