package com.itomelet.blog.controller.admin;

import com.itomelet.blog.po.User;
import com.itomelet.blog.servive.blog.BlogService;
import com.itomelet.blog.servive.comment.CommentService;
import com.itomelet.blog.servive.config.ConfigService;
import com.itomelet.blog.servive.tag.TagService;
import com.itomelet.blog.servive.type.TypeService;
import com.itomelet.blog.servive.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Resource
    private UserService userService;
    @Resource
    private TypeService typeService;
    @Resource
    private BlogService blogService;
    @Resource
    private TagService tagService;
    @Resource
    private CommentService commentService;
    @Resource
    private ConfigService configService;


    @GetMapping()
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/index")
    public String indexPage(Model model) {
        model.addAttribute("categoryCount", typeService.getTotalCategories());
        model.addAttribute("blogCount", blogService.getTotalBlogs());
        model.addAttribute("tagCount", tagService.getTotalTags());
        model.addAttribute("commentCount", commentService.getTotalComments());
        return "admin/index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        @RequestParam String verifyCode, HttpSession session, RedirectAttributes attributes) {
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            attributes.addFlashAttribute("message", "验证码错误");

            return "redirect:/admin";
        }
        User user = userService.checkUser(username, password);
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            session.setAttribute("configuration", configService.findAll().get(0));
            return "redirect:/admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名和密码错误");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }


}
