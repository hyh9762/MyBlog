package com.itomelet.blog.controller.admin;

import com.itomelet.blog.po.Blog;
import com.itomelet.blog.po.User;
import com.itomelet.blog.servive.blog.BlogService;
import com.itomelet.blog.servive.tag.TagService;
import com.itomelet.blog.servive.type.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String EDIT = "admin/edit";
    private static final String LIST = "admin/blog";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    /*@Value("${file.uploadPath}")
    private String filePath;*/


    /**
     * 博客管理页面映射
     *
     * @return
     */
    @RequestMapping("/blogs")
    public String blogs() {
        /*model.addAttribute("page", blogService.listAll(pageable));
        model.addAttribute("types", typeService.listTypesTop(6));
        model.addAttribute("tags", tagService.listTagsTop(10));*/
        return LIST;
    }

    /**
     * 博客管理页面数据展示
     *
     * @param page  页码
     * @param limit 分页条数
     * @param sidx  排序条件
     * @param order 排序规则
     * @return
     */
    @GetMapping("/blogs/list")
    public @ResponseBody
    Page<Blog> listBlogs(Integer page, Integer limit, String sidx, String order, String keyword) {
        page = page == null || page < 0 ? 0 : page - 1;
        sidx = sidx == null ? "updateTime" : sidx;
        order = order == null ? "desc" : order;
        keyword = keyword == null ? "" : keyword;
        Sort sort;
        if (order.equals("desc")) {
            sort = Sort.by(Sort.Direction.DESC, sidx);
        } else {
            sort = Sort.by(Sort.Direction.ASC, sidx);
        }
        return blogService.listBlogsInAdmin(PageRequest.of(page, limit, sort), keyword);
    }


    /*@PostMapping("/blogs/search")
    public String search(Model model, BlogQuery blog, @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        blog.setContent(blog.getTitle());
        model.addAttribute("page", blogService.searchBlogs(pageable, blog));
        return "admin/blogs :: blogList";
    }*/


    @GetMapping("/blogs/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        model.addAttribute("types", typeService.listAll());
        model.addAttribute("tags", tagService.listAll());
        if (id != -1) {
            Blog b = blogService.getBlog(id);
            //b.init();
            model.addAttribute("blog", b);
        } else {
            model.addAttribute("blog", new Blog());
        }
        return EDIT;
    }


    @PostMapping("/blogs/save")
    @ResponseBody
    public boolean saveBlog(@RequestBody Blog blog, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTags(blog.getTagIds()));
        /*String firstPicture = savePicture(firstPictureBean);
        if (firstPicture != null) {
            blog.setFirstPicture(firstPicture);
        }*/
        Blog b = blogService.saveBlog(blog);
        if (b == null) {
            return false;
        } else {
            return true;
        }
    }

    @PostMapping("/blogs/update")
    @ResponseBody
    public boolean updateBlog(@RequestBody Blog blog, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTags(blog.getTagIds()));
        Blog oldBlog = blogService.getBlog(blog.getId());
        Blog b = blogService.updateBlog(blog, oldBlog);
        if (b == null) {
            return false;
        } else {
            return true;
        }
    }

    /*private String savePicture(MultipartFile picture) {
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
    }*/


    @PostMapping("/blogs/delete")
    @ResponseBody
    public boolean delete(@RequestBody Integer[] ids) {
        return blogService.deleteBlog(ids);

    }

}
