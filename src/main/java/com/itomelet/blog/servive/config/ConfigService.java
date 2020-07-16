package com.itomelet.blog.servive.config;


import com.itomelet.blog.po.Config;

import java.util.List;

public interface ConfigService {

    List<Config> findAll();

    Config saveConfig(Config config);
}
