package com.itomelet.blog.servive.comment;


import com.itomelet.blog.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentsByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    void deleteComment(Long id);
}
