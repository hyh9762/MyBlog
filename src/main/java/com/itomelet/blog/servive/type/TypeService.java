package com.itomelet.blog.servive.type;

import com.itomelet.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    Type saveType(Type type);

    Type getType(Long id);

    Type getTypeByName(String name);

    Page<Type> listAll(Pageable pageable);

    List<Type> listAll();

    List<Type> listTypesTop(Integer size);

    Type updateType(Long id, Type type);

    void deleteType(Long id);

    Long getTotalCategories();
}
