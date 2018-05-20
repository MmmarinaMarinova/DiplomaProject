package com.example.model.services;

import com.example.model.Comment;
import com.example.model.User;
import com.example.model.repositories.CommRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

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
        return commentRepository.save(comment);
    }
}
