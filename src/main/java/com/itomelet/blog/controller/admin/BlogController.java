package com.itomelet.blog.controller.admin;

import com.itomelet.blog.po.Blog;
import com.itomelet.blog.po.User;
import com.itomelet.blog.servive.blog.BlogService;
import com.itomelet.blog.servive.tag.TagService;
import com.itomelet.blog.servive.type.TypeService;
import com.itomelet.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Value("${file.uploadPath}")
    private String filePath;

    @RequestMapping("/blogs")
    public String blogs(Model model, @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("page", blogService.listAll(pageable));
        model.addAttribute("types", typeService.listTypesTop(6));
        model.addAttribute("tags", tagService.listTagsTop(10));
        return LIST;
    }


    @PostMapping("/blogs/search")
    public String search(Model model, BlogQuery blog, @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        blog.setContent(blog.getTitle());
        model.addAttribute("page", blogService.searchBlogs(pageable, blog));
        return "admin/blogs :: blogList";
    }


    @GetMapping("/blogs/input/{id}")
    public String input(Model model, @PathVariable Long id) {
        model.addAttribute("types", typeService.listAll());
        model.addAttribute("tags", tagService.listAll());
        if (id != -1) {
            Blog b = blogService.getBlog(id);
            b.init();
            model.addAttribute("blog", b);
        } else {
            model.addAttribute("blog", new Blog());
        }
        return INPUT;
    }


    @PostMapping("/blogs/save")
    public String saveBlog(Blog blog, RedirectAttributes attributes, HttpSession session, MultipartFile firstPictureBean) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTags(blog.getTagIds()));
        String firstPicture = savePicture(firstPictureBean);
        if (firstPicture != null) {
            blog.setFirstPicture(firstPicture);
        }
        Blog b = blogService.saveBlog(blog);
        if (b == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_LIST;
    }

    private String savePicture(MultipartFile picture) {
        if (picture.getSize() == 0) {
            return null;
        }
        try {
            //String filePath = "/usr/local/myapp/upload/firstPictures/";
            //String filePath = ResourceUtils.getURL("classpath:").getPath() + "static/upload/firstPictures/";
            //String filePath = "/root/usr/upload/firstPictures/";
            //判断：该路径是否存在
            File file = new File(filePath);
            if (!file.exists()) {
                //创建文件夹
                file.mkdirs();
            }
            String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + picture.getOriginalFilename();
            File dest = new File(filePath + fileName);
            picture.transferTo(dest);
            return filePath + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping("/blogs/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }

}
