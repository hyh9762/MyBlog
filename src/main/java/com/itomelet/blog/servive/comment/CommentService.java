package com.itomelet.blog.servive.comment;


import com.itomelet.blog.po.Comment;
import com.itomelet.blog.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentsByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    void deleteComment(Long id);

    Long getTotalComments();

    Page<Comment> listAll(Pageable pageable);

    Boolean checkComments(Long[] ids);

    Boolean deleteTag(Long[] ids);

    Boolean replyComment(Comment comment, Long parentCommentId, User user);
}
