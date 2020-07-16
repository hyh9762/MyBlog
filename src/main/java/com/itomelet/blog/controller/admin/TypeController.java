package com.itomelet.blog.controller.admin;

import com.itomelet.blog.po.Type;
import com.itomelet.blog.servive.type.TypeService;
import com.itomelet.blog.util.MyBlogUtils;
import com.itomelet.blog.vo.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Resource
    private TypeService typeService;

    /**
     * 分类管理页面映射
     *
     * @return
     */
    @GetMapping("/types")
    public String types() {
        return "admin/type";
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
    @GetMapping("/types/list")
    public @ResponseBody
    Page<Type> listTypes(Integer page, Integer limit, String sidx, String order) {
        Pageable pageable = MyBlogUtils.genPage(page, limit, sidx, order);
        return typeService.listAll(pageable);
    }





    /*@GetMapping("/types/input")
    public String addInput(Model model) {
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }*/


    @PostMapping({"/types/save", "/types/update"})
    @ResponseBody
    public Result saveType(Type type) {
        Result result = new Result();
        if (type.getId() == null) {
            if (typeService.getTypeByName(type.getName()) != null) {
                result.setStatus(false);
                result.setMessage("该分类已存在");
                return result;
            }
            Type t = typeService.saveType(type);
            if (t == null) {
                result.setStatus(false);
                result.setMessage("新增失败");
            } else {
                result.setStatus(true);
                result.setMessage("新增成功");
            }
        } else {
            Type t = typeService.updateType(type.getId(), type);
            if (t == null) {
                result.setStatus(false);
                result.setMessage("更新失败");
            } else {
                result.setStatus(true);
                result.setMessage("更新成功");

            }
        }
        return result;

    }


    /*@GetMapping("/types/input/{id}")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }*/

    /**
     * 删除选中的所有分类
     *
     * @param ids
     * @return
     */
    @PostMapping("/types/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        return !typeService.deleteType(ids) ? new Result(false, "删除失败") : new Result(true, "删除成功");


    }

}
