package com.example.model.services;

import com.example.model.Comment;
import com.example.model.User;
import com.example.model.repositories.CommRepository;

import com.example.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;


/**
 * Created by Marina on 12.5.2018 г..
 */
@Service
public class CommService {
    @Autowired
    CommRepository commentRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public Comment saveComment(Comment comment) {
        comment.setDatetime(new Timestamp(System.currentTimeMillis()));
        return commentRepository.saveAndFlush(comment);
    }

    @Transactional
    public Comment findOne(long commentId) {
        return commentRepository.findOne(commentId);
    }

    @Transactional
    public boolean existsReaction(Comment comment, User user) {
        return commentRepository.existsReaction(comment.getId(), user);
    }

    @Transactional
    public Comment reactToComment(Comment comment, User user) {
        //user = userRepository.findOne(user.getUserId());
        if (existsReaction(comment, user)) {
            comment.getPeopleLiked().remove(user);
        } else {
            comment.getPeopleLiked().add(user);
        }
        return commentRepository.saveAndFlush(comment);
    }
}
