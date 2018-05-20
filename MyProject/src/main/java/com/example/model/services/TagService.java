package com.example.model.services;

import com.example.model.Tag;
import com.example.model.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    @Transactional
    public HashSet<Tag> findAll() {
        return new HashSet<>(tagRepository.findAll());
    }
}
