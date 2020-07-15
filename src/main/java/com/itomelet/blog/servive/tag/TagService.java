package com.itomelet.blog.servive.tag;

import com.itomelet.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag saveTag(Tag Tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listAll(Pageable pageable);

    List<Tag> listTags(String[] ids);

    List<Tag> listAll();

    Tag updateTag(Long id, Tag Tag);

    List<Tag> listTagsTop(Integer size);

    void deleteTag(Long id);

    Long getTotalTags();
}
