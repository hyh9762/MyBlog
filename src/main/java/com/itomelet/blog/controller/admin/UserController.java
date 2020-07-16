package com.itomelet.blog.controller.admin;

import com.itomelet.blog.po.User;
import com.itomelet.blog.servive.user.UserService;
import com.itomelet.blog.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class UserController {
    @Resource
    private UserService userService;


    /**
     * 映射到用户信息修改的界面
     */
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "admin/login";
        }
        model.addAttribute("username", user.getUsername());
        model.addAttribute("nickname", user.getNickname());
        return "admin/profile";
    }

    /**
     * 修改用户名和昵称
     *
     * @param username
     * @param nickname
     * @return
     */
    @PostMapping("/profile/name")
    @ResponseBody
    public Result nameUpdate(HttpSession session, String username, String nickname) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(nickname)) {
            return new Result(false, "参数不能为空");
        }
        User user = (User) session.getAttribute("user");
        if (userService.updateName(user, username, nickname)) {
            return new Result(true, "修改成功");
        } else {
            return new Result(false, "修改失败");
        }
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public Result passwordUpdate(HttpSession session, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return new Result(false, "参数不能为空");
        }
        User user = (User) session.getAttribute("user");
        if (userService.updatePassword(user, originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            session.removeAttribute("user");
            return new Result(true, "修改成功,请重新登录");
        } else {
            return new Result(false, "修改失败,密码错误");
        }
    }

}
