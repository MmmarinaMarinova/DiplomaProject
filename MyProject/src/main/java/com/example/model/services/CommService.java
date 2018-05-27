package com.example.model.services;

import com.example.model.Comment;
import com.example.model.User;
import com.example.model.repositories.CommRepository;

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

    @Transactional
    public Comment saveComment(Comment comment) {
        //todo needs additional logic for datetime insert
        comment.setDatetime(new Timestamp(System.currentTimeMillis()));
        return commentRepository.saveAndFlush(comment);
    }

    @Transactional
    public Comment findOne(long commentId) {
        return commentRepository.findOne(commentId);
    }

    @Transactional
    public boolean existsReaction(long postId, long userId) {
        return commentRepository.existsReaction(postId, userId);
    }

    @Transactional
    public Comment reactToComment(Comment comment, User user) {
        if (existsReaction(comment.getId(), user.getUserId())) {
            comment.getPeopleLiked().remove(user);
        } else {
            comment.getPeopleLiked().add(user);
        }
        return commentRepository.saveAndFlush(comment);
    }
}
