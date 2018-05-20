package com.example.model.services;

import com.example.model.Category;
import com.example.model.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * Created by Marina on 19.5.2018 г..
 */
@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Transactional
    public HashSet<Category> findAll() {
       return new HashSet<>(categoryRepository.findAll());
    }
}
