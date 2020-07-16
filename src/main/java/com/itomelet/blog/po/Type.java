package com.itomelet.blog.po;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_type")
public class Type {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String icon;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @OneToMany(mappedBy = "type")  //被维护
    @JsonIgnore
    private List<Blog> blogs = new ArrayList<>();

    public Type() {
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

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String categoryIcon) {
        this.icon = categoryIcon;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createTime) {
        this.createdTime = createTime;
    }

    @Override
    public String toString() {
        return "Type{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", categoryIcon='" + icon + '\'' +
               ", createTime=" + createdTime +
               ", blogs=" + blogs +
               '}';
    }
}
