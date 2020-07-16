package com.itomelet.blog.controller;

import com.itomelet.blog.servive.blog.BlogService;
import com.itomelet.blog.servive.config.ConfigService;
import com.itomelet.blog.servive.tag.TagService;
import com.itomelet.blog.servive.type.TypeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @Resource
    private BlogService blogService;
    @Resource
    private TypeService typeService;
    @Resource
    private TagService tagService;
    @Resource
    private ConfigService configService;

    @GetMapping("/")
    public String index(HttpSession session, Model model, @PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("page", blogService.listAllPublished(pageable));
        model.addAttribute("types", typeService.listTypesTop(6));
        model.addAttribute("tags", tagService.listTagsTop(6));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        session.setAttribute("configuration", configService.findAll().get(0));
        return "index";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam String query,
                         @PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("page", blogService.searchBlogs(query, pageable));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model, HttpSession session) {
        /*Blog newBlog = new Blog();
        Blog blog = blogService.getBlog(id);
        //更改浏览数
        newBlog.setViews(blog.getViews() + 1);
        //更新数据库数据
        blogService.updateBlog(newBlog, blog);*/
        model.addAttribute("blog", blogService.getAndConvert(id));
        session.setAttribute("configuration", configService.findAll().get(0));
        return "blog";
    }


    @GetMapping("/footer/newBlog")
    public String newBlogs(Model model) {
        model.addAttribute("newBlogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newBlogList";
    }
}
