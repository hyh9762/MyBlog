package com.itomelet.blog.servive.blog;

import com.itomelet.blog.po.Blog;
import com.itomelet.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

    Page<Blog> searchBlogs(Pageable pageable, BlogQuery blog);

    Page<Blog> listAll(Pageable pageable);

    Page<Blog> listBlogsInAdmin(Pageable pageable, String keyword);

    Page<Blog> listByTag(Long tagId, Pageable pageable);

    Page<Blog> listByType(Long typeId, Pageable pageable);

    Page<Blog> listAllPublished(Pageable pageable);

    Map<String, List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Blog srcBlog, Blog targetBlog);

    List<Blog> listRecommendBlogTop(Integer size);

    Boolean deleteBlog(Long[] ids);

    Page<Blog> searchBlogs(String query, Pageable pageable);

    Long getTotalBlogs();
}
