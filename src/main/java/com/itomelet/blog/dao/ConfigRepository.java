package com.itomelet.blog.dao;

import com.itomelet.blog.po.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, Long> {

    Config findByName(String name);
}
