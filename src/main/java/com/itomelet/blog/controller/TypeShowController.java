package com.itomelet.blog.controller;

import com.itomelet.blog.po.Type;
import com.itomelet.blog.servive.blog.BlogService;
import com.itomelet.blog.servive.type.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")

    public String types(Model model, @PathVariable Long id,
                        @PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        List<Type> types = typeService.listTypesTop(10000);
        if (id == -1) {
            id = types.get(0).getId();
        }
        model.addAttribute("types", types);
        model.addAttribute("page", blogService.listByType(id, pageable));
        model.addAttribute("activeTypeId", id);
        return "types";
    }

}
