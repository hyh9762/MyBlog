package com.itomelet.blog.servive.config;

import com.itomelet.blog.dao.ConfigRepository;
import com.itomelet.blog.po.Config;
import com.itomelet.blog.util.MyBlogUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigRepository configRepository;

    @Override
    public List<Config> findAll() {
        return configRepository.findAll();
    }

    @Override
    public Config saveConfig(Config config) {
        Config oldConfig = configRepository.findByName("ekko");
        BeanUtils.copyProperties(config, oldConfig, MyBlogUtils.getNullPropertyNames(config));
        return configRepository.save(oldConfig);

    }
}
