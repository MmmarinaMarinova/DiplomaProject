package com.example.model.services;

import com.example.model.Multimedia;
import com.example.model.repositories.MultimediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Service
public class MultimediaService {
    @Autowired
    MultimediaRepository multimediaRepository;

    @Transactional
    public Multimedia save(Multimedia profilePic) {
        multimediaRepository.save(profilePic);
        multimediaRepository.flush();
        return profilePic;
    }

    @Transactional
    public Multimedia findOne(long id) {
        return multimediaRepository.findOne(id);
    }
}
