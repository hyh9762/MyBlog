package com.itomelet.blog.controller;

import com.itomelet.blog.po.Comment;
import com.itomelet.blog.po.User;
import com.itomelet.blog.servive.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentsByBlogId(blogId));
        return "blog :: commentList";
    }


    @PostMapping("/comments")
    public String postComment(Comment comment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAdminComment(true);
            comment.setAvatar(user.getAvatar());
            comment.setNickname(user.getNickname());
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + comment.getBlog().getId();
    }

    @PostMapping("/comments/delete")
    public String deleteComment(Comment comment, HttpSession session) {
        if (session.getAttribute("user") != null) {
            commentService.deleteComment(comment.getId());
        }
        return "redirect:/comments/" + comment.getBlog().getId();
    }


}
