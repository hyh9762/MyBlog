package com.itomelet.blog.dao;

import com.itomelet.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findByBlogIdAndParentCommentIsNullAndPublishedIsTrue(Long blogId, Sort sort);

    void deleteByBlogId(Long id);


    @Modifying
    @Query("update Comment set published=true where id=?1")
    void checkComment(Long id);
}
