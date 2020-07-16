package com.itomelet.blog.controller.admin;

import com.itomelet.blog.po.Tag;
import com.itomelet.blog.servive.tag.TagService;
import com.itomelet.blog.util.MyBlogUtils;
import com.itomelet.blog.vo.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    private TagService tagService;

    @GetMapping("/tags")
    public String Tags() {
        return "admin/tag";
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
    @GetMapping("/tags/list")
    public @ResponseBody
    Page<Tag> listTags(Integer page, Integer limit, String sidx, String order) {
        Pageable pageable = MyBlogUtils.genPage(page, limit, sidx, order);
        return tagService.listAll(pageable);
    }


    @PostMapping("/tags/save/{tagName}")
    @ResponseBody
    public Result saveTag(@PathVariable String tagName) {
        if (tagService.getTagByName(tagName) != null) {
            return new Result(false, "该分类已存在");
        }
        Tag tag = new Tag();
        tag.setName(tagName);
        tag.setCreatedTime(new Date());
        return tagService.saveTag(tag) == null ? new Result(false, "添加失败") : new Result(true, "添加成功");
    }


    /**
     * 删除选中的所有标签
     *
     * @param ids
     * @return
     */
    @PostMapping("/tags/delete")
    @ResponseBody
    public Result delete(@RequestBody Long[] ids) {
        return !tagService.deleteTag(ids) ? new Result(false, "删除失败") : new Result(true, "删除成功");

    }

}
