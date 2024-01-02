package com.gotcha.server.question.repository;

import com.gotcha.server.question.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
