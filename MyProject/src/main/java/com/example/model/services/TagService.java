package com.example.model.services;

import com.example.model.Tag;
import com.example.model.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ServletContext servletContext;

    @Transactional
    public Set<Tag> findAll() {
        return new HashSet<>(tagRepository.findAll());
    }

    public Set<Tag> getTags(String tagNames) {
        Set<Tag> tags = null;
        if (!"".equals(tagNames)) {

            tags = new HashSet<>();
            String[] splitTags = tagNames.split(",");

            for (int i = 0; i < splitTags.length; i++) {
                String tagName = splitTags[i];
                tagName = tagName.trim();
                tagName = tagName.replace("]", "");
                tagName = tagName.replace("[", "");
                if (!"".equals(tagName)) {
                    Tag tag = null;
                    if (((HashSet<String>) servletContext.getAttribute("tags")).contains(tagName)) {
                        tag = this.findByName(tagName);
                    } else {
                        tag = new Tag(tagName);
                    }
                    tags.add(tag);
                }
            }
            return tags.size() > 0 ? tags : null;
        }
        return tags;
    }

    @Transactional
    public Tag findByName(String tagName){
        return tagRepository.findByName(tagName);
    }
}
