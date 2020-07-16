package com.itomelet.blog.controller.admin;

import com.itomelet.blog.po.Comment;
import com.itomelet.blog.po.User;
import com.itomelet.blog.servive.comment.CommentService;
import com.itomelet.blog.util.MyBlogUtils;
import com.itomelet.blog.vo.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class CommentManagementController {

    @Resource
    private CommentService commentService;

    @GetMapping("/comments")
    public String Tags() {
        return "admin/comment";
    }

    /**
     * 分类管理页面数据展示
     *
     * @param page  页码
     * @param limit 分页条数
     * @param sidx  排序条件
     * @param order 排序规则
     * @return
     */
    @ResponseBody
    @GetMapping("/comments/list")
    public Page<Comment> listTags(Integer page, Integer limit, String sidx, String order) {
        Pageable pageable = MyBlogUtils.genPage(page, limit, sidx, order);
        return commentService.listAll(pageable);
    }

    @ResponseBody
    @PostMapping("/comments/checkDone")
    public Result checkComments(@RequestBody Long[] ids) {
        return commentService.checkComments(ids) ? new Result(true, "审核成功") : new Result(false, "审核失败");
    }


    /**
     * 删除选中的所有评论
     *
     * @param ids
     * @return
     */
    @PostMapping("/comments/delete")
    @ResponseBody
    public Result delete(@RequestBody Long[] ids) {
        return commentService.deleteTag(ids) ? new Result(true, "删除成功") : new Result(false, "删除失败");

    }

    /**
     * 管理员回复评论
     *
     * @param comment
     * @return
     */
    @PostMapping("/comments/reply")
    @ResponseBody
    public Result replyComment(Comment comment, Long parentCommentId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new Result(false, "登录已过期，请重新登录");
        }
        return commentService.replyComment(comment, parentCommentId, user) ? new Result(true, "回复成功") : new Result(false, "回复失败");
    }
}
