package com.itomelet.blog.controller.admin;

import com.itomelet.blog.po.Tag;
import com.itomelet.blog.servive.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String Tags(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
                       Model model) {
        model.addAttribute("page", tagService.listAll(pageable));
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String addInput(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }


    @PostMapping({"/tags/addTags", "/tags/editTag"})
    public String saveTags(@Valid Tag Tag, BindingResult result, Model model, RedirectAttributes attributes) {
        //如果校验不通过
        if (result.hasErrors()) {
            return "admin/tags-input";
        }
        if (tagService.getTagByName(Tag.getName()) != null) {
            result.rejectValue("name", "nameError", "该分类已存在");
            return "admin/tags-input";
        }

        if (Tag.getId() == null) {
            Tag t = tagService.saveTag(Tag);
            if (t == null) {
                attributes.addFlashAttribute("message", "新增失败");
            } else {
                attributes.addFlashAttribute("message", "新增成功");

            }
        } else {
            Tag t = tagService.updateTag(Tag.getId(), Tag);
            if (t == null) {
                attributes.addFlashAttribute("message", "更新失败");
            } else {
                attributes.addFlashAttribute("message", "更新成功");

            }
        }


        return "redirect:/admin/tags";
    }


    @GetMapping("/tags/input/{id}")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }

    @GetMapping("/tags/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }

}
