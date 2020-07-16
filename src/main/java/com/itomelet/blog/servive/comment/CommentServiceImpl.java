package com.itomelet.blog.servive.comment;

import com.itomelet.blog.dao.BlogRepository;
import com.itomelet.blog.dao.CommentRepository;
import com.itomelet.blog.po.Comment;
import com.itomelet.blog.po.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentRepository commentRepository;

    @Resource
    private BlogRepository blogRepository;

    @Value("${comment.avatar}")
    private String avatar;

    @Override
    public List<Comment> listCommentsByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentIsNullAndPublishedIsTrue(blogId, sort);
        return eachComment(comments);
    }

    /**
     * 保存评论操作，为评论设置父级评论，博客，发布时间等基础属性
     *
     * @param comment
     * @return
     */
    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            Comment parentComment = commentRepository.getOne(parentCommentId);
            comment.setParentComment(parentComment);
            comment.setBlog(blogRepository.getOne(parentComment.getBlog().getId()));
        } else {
            comment.setParentComment(null);
            comment.setBlog(blogRepository.getOne(comment.getBlog().getId()));
        }
        if (!comment.isAdminComment()) {
            comment.setAvatar(avatar);
        } else {
            //管理员评论不用审核
            comment.setPublished(true);
        }
        comment.setCreatedTime(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public Boolean replyComment(Comment comment, Long parentCommentId, User user) {
        //为管理员的回复设置默认属性
        comment.setNickname(user.getNickname());
        comment.setAvatar(user.getAvatar());
        comment.setAdminComment(true);
        comment.setEmail(user.getEmail());

        //设置父级评论信息
        Comment parentComment = new Comment();
        parentComment.setId(parentCommentId);
        comment.setParentComment(parentComment);
        return saveComment(comment) != null;
    }


    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Long getTotalComments() {
        return commentRepository.count();
    }

    @Override
    public Page<Comment> listAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Boolean checkComments(Long[] ids) {
        try {
            for (Long id : ids) {
                checkComment(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private void checkComment(Long id) {
        commentRepository.checkComment(id);
    }

    @Override
    public Boolean deleteTag(Long[] ids) {
        try {
            for (Long id : ids) {
                deleteComment(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 循环每个顶级的评论节点
     *
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replies = comment.getReplyComments();
            for (Comment reply : replies) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplies);
            //清除临时存放区
            tempReplies = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplies = new ArrayList<>();

    /**
     * 递归迭代，剥洋葱
     *
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplies.add(comment);//顶节点添加到临时存放集合
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                recursively(reply);
            }
        }
    }

}
