package com.itomelet.blog.servive.tag;

import com.itomelet.blog.dao.TagRepository;
import com.itomelet.blog.exception.NotFoundException;
import com.itomelet.blog.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
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
    public List<Tag> listTags(String ids) {
        List<Long> idArray = convertToList(ids);
        List<Tag> list = new ArrayList<>();
        for (Long id : idArray) {
            Optional<Tag> optional = tagRepository.findById(id);
            if (optional.isPresent()) {
                list.add(optional.get());
            } else {
                Tag t = new Tag();
                t.setName(id.toString());
                Tag newTag = tagRepository.save(t);
                list.add(newTag);
            }
        }
        return list;
    }

    private List<Long> convertToList(String ids) {
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
    }


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
}
