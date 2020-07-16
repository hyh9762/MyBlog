package com.itomelet.blog.servive.tag;

import com.itomelet.blog.dao.TagRepository;
import com.itomelet.blog.exception.NotFoundException;
import com.itomelet.blog.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag Tag) {
        return tagRepository.save(Tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listAll(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTags(String[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        List<Tag> list = new ArrayList<>();
        for (String id : ids) {
            Optional<Tag> optional = tagRepository.findById(Long.valueOf(id));
            if (optional.isPresent()) {
                list.add(optional.get());
            } else {
                //不存在就新增
                Tag t = new Tag();
                t.setName(id);
                Tag newTag = tagRepository.save(t);
                list.add(newTag);
            }
        }
        return list;
    }

    /*private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                if (id.matches("-?[0-9]+.?[0-9]*")) {
                    //id是数字
                    list.add(Long.valueOf(id));
                } else {
                    //是新添加的标签
                    Tag t = new Tag();
                    t.setName(id);
                    Tag newTag = tagRepository.save(t);
                    list.add(newTag.getId());
                }

            }
        }
        return list;
    }*/


    @Override
    public List<Tag> listAll() {
        return tagRepository.findAll();
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag Tag) {
        Tag t = tagRepository.getOne(id);
        if (t == null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(Tag, t);
        return tagRepository.save(t);
    }

    @Override
    public List<Tag> listTagsTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Boolean deleteTag(Long[] ids) {
        try {
            for (Long id : ids) {
                tagRepository.deleteById(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Long getTotalTags() {
        return tagRepository.count();
    }
}
