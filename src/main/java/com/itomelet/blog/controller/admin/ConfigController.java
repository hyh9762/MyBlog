package com.itomelet.blog.controller.admin;

import com.itomelet.blog.po.Config;
import com.itomelet.blog.servive.config.ConfigService;
import com.itomelet.blog.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ConfigController {

    @Resource
    private ConfigService configService;

    @GetMapping("/configurations")
    public String Tags(Model model) {
        List<Config> configs = configService.findAll();
        model.addAttribute("configuration", configs.get(0));
        return "admin/configuration";
    }

    @ResponseBody
    @PostMapping({"/configurations/website", "/configurations/userInfo", "/configurations/footer"})
    public Result editConfig(Config config, HttpSession session) {
        Config newConfig = configService.saveConfig(config);
        if (newConfig != null) {
            session.setAttribute("configuration", newConfig);
            return new Result(true, "保存成功");
        } else {
            return new Result(false, "保存失败");
        }
    }

}
