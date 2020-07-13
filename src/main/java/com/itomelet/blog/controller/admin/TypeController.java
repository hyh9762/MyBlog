package com.itomelet.blog.controller.admin;

import com.itomelet.blog.po.Type;
import com.itomelet.blog.servive.type.TypeService;
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
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
                        Model model) {
        model.addAttribute("page", typeService.listAll(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String addInput(Model model) {
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }


    @PostMapping({"/types/addTypes", "/types/editType"})
    public String saveType(@Valid Type type, BindingResult result, Model model, RedirectAttributes attributes) {
        //如果校验不通过
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        if (typeService.getTypeByName(type.getName()) != null) {
            result.rejectValue("name", "nameError", "该分类已存在");
            return "admin/types-input";
        }

        if (type.getId() == null) {
            Type t = typeService.saveType(type);
            if (t == null) {
                attributes.addFlashAttribute("message", "新增失败");
            } else {
                attributes.addFlashAttribute("message", "新增成功");

            }
        } else {
            Type t = typeService.updateType(type.getId(), type);
            if (t == null) {
                attributes.addFlashAttribute("message", "更新失败");
            } else {
                attributes.addFlashAttribute("message", "更新成功");

            }
        }


        return "redirect:/admin/types";
    }


    @GetMapping("/types/input/{id}")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }

    @GetMapping("/types/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }

}
