package com.example.model.services;

import com.example.model.Tag;
import com.example.model.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.util.*;
import java.util.stream.Collectors;

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

                Tag tag = null;
                if (!"".equals(tagName)) {
                    Set<String> servlContxtTags = (Set<String>) servletContext.getAttribute("tags");
                    if(servlContxtTags != null){
                        Boolean existsTag = servlContxtTags.contains(tagName);
                        if(existsTag){
                            tag = this.findByName(tagName);
                        }else{
                            tag = new Tag(tagName);
                        }
                    }else {
                        tag = new Tag(tagName);
                    }
                    tags.add(tag);
                }
            }

            if(tags.size() > 0){
                tagRepository.save(tags);
                tagRepository.flush();
                servletContext.setAttribute("tags", this.findAllTagNames());
            }
            return tags;
        }
        return null;
    }

    @Transactional
    public Tag findByName(String tagName){
        return tagRepository.findByName(tagName);
    }

    @Transactional
    public Set<String> findAllTagNames() {
        return tagRepository.findAllNames();
    }
}
