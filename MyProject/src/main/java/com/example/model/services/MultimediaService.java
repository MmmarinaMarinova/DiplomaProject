package com.example.model.services;

import com.example.WebInitializer;
import com.example.model.Multimedia;
import com.example.model.repositories.MultimediaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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

    public Set<Multimedia> getImages(MultipartFile image1, MultipartFile image2, MultipartFile image3) {
        Set<Multimedia> images = new HashSet<>();
        if(image1.getSize() != 0){
            Multimedia multimedia1=readMultimedia(image1,false);
            images.add(multimedia1);
        }
        if(image2.getSize() != 0){
            Multimedia multimedia2=readMultimedia(image2,false);
            images.add(multimedia2);
        }
        if(image3.getSize() != 0){
            Multimedia multimedia3=readMultimedia(image3,false);
            images.add(multimedia3);
        }
        return images;
    }

    private Multimedia readMultimedia(MultipartFile file, boolean isVideo) {
        Multimedia transferredFile = null;

        String fileUrl = System.currentTimeMillis() + file.getOriginalFilename();
        try {
            if(!file.isEmpty()){
                File f = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION
                        + File.separator + fileUrl);
                file.transferTo(f);

                return new Multimedia(fileUrl,isVideo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transferredFile;
    }

    public Multimedia getVideo(MultipartFile input) {
        Multimedia video = null;
        if(input.getSize() != 0){
            video = readMultimedia(input,true);
        }
        return video;
    }
}
