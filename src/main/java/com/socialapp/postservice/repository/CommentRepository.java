package com.socialapp.postservice.repository;

import com.socialapp.postservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByPostIdOrderByTimestampDesc(Long postId);

}
