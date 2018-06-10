package com.example.model.services;

import com.example.model.Category;
import com.example.model.exceptions.CategoryException;
import com.example.model.repositories.CategoryRepository;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.util.*;

/**
 * Created by Marina on 19.5.2018 г..
 */
@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ServletContext servletContext;

    @Transactional
    public HashSet<Category> findAll() {
       return new HashSet<>(categoryRepository.findAll());
    }

    public Set<Category> getCategories(String categoryNames) throws CategoryException {
        HashSet<Category> categories = null;
        if(!"".equals(categoryNames)){
            categories = new HashSet<>();
            String[] splitCategories = categoryNames.split(",");
            for (String splitCategory : splitCategories) {
                String categoryName = splitCategory;
                if (!"".equals(categoryName)) {
                    categoryName = categoryName.trim();
                    categoryName = categoryName.replace("]", "");
                    categoryName = categoryName.replace("[", "");
                    Set<Category> applicationScopeCategories =
                            (Set<Category>) servletContext.getAttribute("categories");
                    if(applicationScopeCategories != null){
                        for (Category cat : applicationScopeCategories) {
                            if(categoryName.equals(cat.getName())){
                                categories.add(cat);
                            }
                        }
                    }

                }
            }
        }
        return categories;
    }

    @Transactional
    public Category findOne(Long id) {
        return categoryRepository.findOne(id);
    }
}
