package com.itomelet.blog.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;


    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<Blog> blogs = new ArrayList<>();

    public Tag() {
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createTime) {
        this.createdTime = createTime;
    }

    @Override
    public String toString() {
        return "Tag{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", createTime=" + createdTime +
               ", blogs=" + blogs +
               '}';
    }
}
