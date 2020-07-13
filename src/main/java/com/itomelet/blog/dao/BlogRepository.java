package com.itomelet.blog.dao;

import com.itomelet.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend=true ")
    List<Blog> findTop(Pageable pageable);

    Page<Blog> findAllByPublished(boolean published, Pageable pageable);

    @Query("select b from Blog b where b.title like ?1 or b.content like ?1 or b.description like ?1 or b.type.name like ?1 or b.user.nickname like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by function('date_format',b.updateTime,'%Y') desc ")
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y')=?1 and b.published=true ")
    List<Blog> findByYear(String year);

    Long countAllByPublished(boolean published);

    Page<Blog> findByPublishedIsTrueAndTypeId(Long typeId, Pageable pageable);
}
