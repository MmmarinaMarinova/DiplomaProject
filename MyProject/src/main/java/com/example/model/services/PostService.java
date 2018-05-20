package com.example.model.services;

import com.example.model.Post;
import com.example.model.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    @Transactional
    public Post findOne(long postId) {
        return postRepository.findOne(postId);
    }

    @Transactional
    public boolean existsReaction(long postId, long userId) {
        return postRepository.existsReaction(postId, userId);
    }

    public void updateReaction(boolean isLike, long postId, long userId) {
    }
}
