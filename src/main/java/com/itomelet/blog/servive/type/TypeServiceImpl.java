package com.itomelet.blog.servive.type;

import com.itomelet.blog.dao.TypeRepository;
import com.itomelet.blog.po.Type;
import com.itomelet.blog.util.MyBlogUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Resource
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        type.setCreatedTime(new Date());
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.getOne(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Type> listAll(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listAll() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypesTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return typeRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.getOne(id);
        BeanUtils.copyProperties(type, t, MyBlogUtils.getNullPropertyNames(type));
        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    @Override
    public Boolean deleteType(Long[] ids) {
        try {
            for (Long id : ids) {
                deleteType(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Long getTotalCategories() {
        return typeRepository.count();
    }
}
