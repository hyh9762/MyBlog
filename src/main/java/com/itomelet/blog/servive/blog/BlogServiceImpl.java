package com.itomelet.blog.servive.blog;

import com.itomelet.blog.dao.BlogRepository;
import com.itomelet.blog.dao.CommentRepository;
import com.itomelet.blog.po.Blog;
import com.itomelet.blog.po.Tag;
import com.itomelet.blog.po.Type;
import com.itomelet.blog.util.MarkdownUtils;
import com.itomelet.blog.util.MyBeanUtils;
import com.itomelet.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.io.File;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Value("${file.uploadDic}")
    private String fileUploadDic;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    /**
     * 处理显示内容，并修改浏览数
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Blog getAndConvert(Long id) {
        //修改浏览数
        Blog blog = blogRepository.getOne(id);
        blog.setViews(blog.getViews() + 1);
        blogRepository.save(blog);

        //copy newBlog
        Blog newBlog = new Blog();
        BeanUtils.copyProperties(blog, newBlog);

        //改变newBlog的内容显示格式
        newBlog.setContent(MarkdownUtils.markdownToHtmlExtensions(newBlog.getContent()));
        return newBlog;
    }

    @Override
    public Page<Blog> searchBlogs(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll((Specification<Blog>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //根据标题或内容查询
            List<Predicate> titleOrContentPredicates = new ArrayList<>();
            if (!StringUtils.isEmpty(blog.getTitle())) {
                titleOrContentPredicates.add(criteriaBuilder.like(root.get("title"), "%" + blog.getTitle() + "%"));
            }
            if (!StringUtils.isEmpty(blog.getContent())) {
                titleOrContentPredicates.add(criteriaBuilder.like(root.get("content"), "%" + blog.getContent() + "%"));
            }
            if (titleOrContentPredicates.size() != 0) {
                predicates.add(criteriaBuilder.or(titleOrContentPredicates.toArray(new Predicate[0])));
            }

            //类型
            if (blog.getTypeId() != null) {
                predicates.add(criteriaBuilder.equal(root.<Type>get("type").<Long>get("id"), blog.getTypeId()));
            }
            if (blog.isRecommend()) {
                predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
            }
            //query.where(predicates.toArray(new Predicate[0]));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        }, pageable);

    }

    @Override
    public Page<Blog> searchBlogs(String query, Pageable pageable) {
        return blogRepository.findByQuery("%" + query + "%", pageable);
    }

    @Override
    public Long getTotalBlogs() {
        return blogRepository.count();
    }

    @Override
    public Page<Blog> listAll(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlogsInAdmin(Pageable pageable, String keyword) {
        return blogRepository.listBlogsInAdmin(pageable, "%" + keyword + "%");
    }

    @Override
    public Page<Blog> listByTag(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join<Tag, Blog> join = root.join("tags");
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(join.get("id"), tagId));
                predicates.add(criteriaBuilder.equal(root.get("published"), true));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listByType(Long typeId, Pageable pageable) {
        return blogRepository.findByPublishedIsTrueAndTypeId(typeId, pageable);
    }

    @Override
    public Page<Blog> listAllPublished(Pageable pageable) {
        return blogRepository.findAllByPublished(true, pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.countAllByPublished(true);
    }

    @Override
    @Transactional
    public Blog saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        return blogRepository.save(blog);
    }

    /**
     * @param srcBlog    前端传递的数据封装
     * @param targetBlog 数据库查询的旧数据
     * @return
     */
    @Override
    @Transactional
    public Blog updateBlog(Blog srcBlog, Blog targetBlog) {
        Blog oldBlog = blogRepository.getOne(srcBlog.getId());
        if (!oldBlog.getFirstPicture().equals(srcBlog.getFirstPicture())) {
            deleteOldFirstPicture(oldBlog.getFirstPicture());
        }
        srcBlog.setUpdateTime(new Date());
        BeanUtils.copyProperties(srcBlog, targetBlog, MyBeanUtils.getNullPropertyNames(srcBlog));
        return blogRepository.save(targetBlog);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }


    @Override
    @Transactional
    public Boolean deleteBlog(Integer[] ids) {
        try {
            for (Integer id : ids) {
                //删除首图
                String firstPicture = blogRepository.getOne(Long.valueOf(id)).getFirstPicture();
                deleteOldFirstPicture(firstPicture);
                commentRepository.deleteByBlogId(Long.valueOf(id));
                blogRepository.deleteById(Long.valueOf(id));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void deleteOldFirstPicture(String firstPicture) {
        String[] split = firstPicture.split("/");
        File file = new File(fileUploadDic + split[split.length - 1]);
        file.delete();
    }


}
