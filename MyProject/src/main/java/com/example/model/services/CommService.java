package com.example.model.services;

import com.example.model.repositories.CommRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Service
public class CommService {
    @Autowired
    CommRepository commentRepository;
}